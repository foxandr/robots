package fox.alex.robots.controller;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.task.Task;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.service.RobotService;
import fox.alex.robots.util.RobotGenerator;
import fox.alex.robots.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

/**
 * Created by fox on 16.01.17.
 *
 * Этот контроллер почти полностью отвечает за все процессы в Мире Роботов (Ajax и REST контроллеры также могут добавить работа или убить его)
 * WorldController запускается отделным потоком и периодически выполняет следующие задачи:
 * - замещает погибших роботов новыми, такого же типа;
 * - чистит логи активности;
 * - раздает задачи роботам на выполнение;
 * - может удалить без его замещения или добавить без выполняемой задачи;
 * - если задача отправляется лично роботу, а он занят, то создается новый робот такого же типа и ему отправляется эта задача;
 * - если задача отправляется всем роботам и кто-то из них занят, то он игнорирует ее, остальные начинают выполнять,
 * если же все заняты, тогда создается новый робот для этой задачи;
 * - при инициализации создает несколько первых роботов (INT_NUM_OF_ROBOTS);
 * - количество логов активности ограничено числом MAX_LOG;
 * - если роботов стало очень много, сообщает об этом и перестает создавать новых.
 */

@Controller
public class WorldController implements Runnable {

    public final static int MAX_LOG = 50;
    public final static int INT_NUM_OF_ROBOTS = 10;

    private final String simpleName = getClass().getSimpleName();

    @Autowired
    private RobotService robotService;

    @Resource
    private Queue<Task> taskQueue;

    @Autowired
    private ExecutorService singleExecutorService;

    @Resource
    private Queue<String> logQueue;

    public void run() {
        logQueue.add(simpleName + ":msg.wc.run");
        while (true){
            checkDeadRobots();
            checkLogActivity();
            Task task = taskQueue.poll();
            if (task != null) {
                if (task.robotName == null) {
                    sendBroadcastTask(task);
                } else {
                    sendPersonalTask(task);
                }
            }
        }
    }

    private void sendBroadcastTask(Task task) {
        try {
            robotService.addBroadcastTask(task.typeTask);
            logQueue.add(simpleName + ":msg.wc.broadcast");
        } catch (NoRobotException e) {
            logQueue.add(simpleName + ":msg.wc.norobots");
            addNewRobotWithTask(task);
        } catch (BusyAllRobotsException e1){
            logQueue.add(simpleName + ":msg.wc.allbusy");
            addNewRobotWithTask(task);
        }
    }

    private void sendPersonalTask(Task task) {
        try {
            robotService.addPersonalTask(task.robotName, task.typeTask);
            logQueue.add(task.robotName + ":msg.wc.personal");
        } catch (RobotNotFoundException e){
            logQueue.add(simpleName + ":msg.wc.nfd");
        } catch (BusyRobotException e){
            logQueue.add(task.robotName + ":msg.wc.busy");
            if (!TypeTask.SUICIDE.equals(task.typeTask)) addNewRobotWithTask(task);
        }
    }

    private void addNewRobot(Task task) {
        try {
            robotService.addRobot(createNewRobot(task));
        } catch (WrongTaskException e){
            logQueue.add("WorldController:task.wrong");
        } catch (TooManyRobotsException e1){
            logQueue.add(simpleName + ":msg.wc.over");
        }
    }

    private void addNewRobotWithTask(Task task) {
        try {
            Robot newRobot = createNewRobot(task);
            newRobot.setTask(task.typeTask);
            robotService.addRobotWithTask(newRobot);
        } catch (WrongTaskException e){
            logQueue.add("WorldController:task.wrong");
        } catch (TooManyRobotsException e1){
            logQueue.add(simpleName + ":msg.wc.over");
        }
    }

    public void removeRobotByName(String robotName){
        try {
            robotService.removeRobotByName(robotName);
            logQueue.add(robotName + ":msg.wc.kill");
        } catch (RobotNotFoundException e){
            logQueue.add(robotName + ":msg.wc.nfd");
        }
    }

    private void checkDeadRobots() {
        Collection<Robot> deadRobots = robotService.checkDeadRobots();
        if (deadRobots.size() > 0) {
            for (Robot r : deadRobots) {
                Robot newRobot = RobotGenerator.getRobot(r.getClass().getSimpleName());
                newRobot.setLogQueue(logQueue);
                robotService.addRobot(newRobot);
            }
            logQueue.add(simpleName + ":msg.wc.replace");
        }
    }

    private void checkLogActivity(){
        while (logQueue.size() > MAX_LOG){
            logQueue.poll();
        }
    }

    private Collection<Robot> initRobots() {
        return RobotGenerator.getRandomRobots(INT_NUM_OF_ROBOTS);
    }

    private Robot createNewRobot(Task task) throws WrongTaskException {
        Robot newRobot = RobotGenerator.getRobot(task.typeTask);
        newRobot.setLogQueue(logQueue);
        logQueue.add(newRobot.name + ":msg.wc.newrobot");
        return newRobot;
    }

    public void init(){
        initRobots().stream()
                .forEach(r -> {
                    r.setLogQueue(logQueue);
                    robotService.addRobot(r);
                });
        logQueue.add(simpleName + ":msg.wc.first");
        singleExecutorService.submit(this);
    }

    public void destroy(){
        singleExecutorService.shutdownNow();
        robotService.getAllRobots().stream()
                .forEach(r -> robotService.removeRobot(r));
        logQueue.add(simpleName + ":msg.wc.last");
    }
}
