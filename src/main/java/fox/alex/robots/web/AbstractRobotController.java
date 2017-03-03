package fox.alex.robots.web;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.task.Task;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.service.RobotService;
import fox.alex.robots.util.RobotGenerator;
import fox.alex.robots.util.exception.RobotNotFoundException;
import fox.alex.robots.util.exception.TooManyRobotsException;
import fox.alex.robots.util.exception.WrongTaskException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Queue;

/**
 * Created by fox on 18.01.17.
 */

public abstract class AbstractRobotController {

    @Autowired
    private RobotService robotService;

    @Resource
    private Queue<Task> taskQueue;

    @Resource
    protected Queue<String> logQueue;

    @Autowired
    private WebApplicationContext webApplicationContext;

    public void sendTask(Task task) {
        try {
            taskQueue.add(task);
        } catch (NullPointerException e){
            logQueue.add("WorldController:task.wrong");
        }
    }

    public void addNewRobot(TypeTask task) {
        try {
            Robot newRobot = RobotGenerator.getRobot(task);
            newRobot.setLogQueue(logQueue);
            robotService.addRobot(newRobot);
            logQueue.add(newRobot.name + ":msg.wc.newrobot");
        } catch (WrongTaskException e) {
            logQueue.add("WorldController:task.wrong");
        } catch (TooManyRobotsException e1){
            logQueue.add("WorldController:msg.wc.over");
        }
    }

    public void killRobot(String name) {
        try {
            robotService.removeRobotByName(name);
            logQueue.add(name + ":msg.wc.kill");
        } catch (RobotNotFoundException e){
            logQueue.add(name + ":msg.wc.nfd");
        }
    }

    public Collection<Robot> getAllRobots(){
        return robotService.getAllRobots();
    }
}
