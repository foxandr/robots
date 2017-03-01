package fox.alex.robots.web;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.task.Task;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.service.RobotService;
import fox.alex.robots.util.RobotGenerator;
import fox.alex.robots.util.exception.RobotNotFoundException;
import fox.alex.robots.util.exception.TooManyRobotsException;
import org.springframework.beans.factory.annotation.Autowired;

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

    public void sendTask(Task task) throws IllegalStateException {
        taskQueue.add(task);
    }

    public void addNewRobot(TypeTask task) {
        Robot newRobot = RobotGenerator.getRobot(task);
        newRobot.setLogQueue(logQueue);
        try {
            robotService.addRobot(newRobot);
            logQueue.add(newRobot.name + ":msg.wc.newrobot");
        } catch (TooManyRobotsException e){
            logQueue.add("WorldController:msg.wc.over");
        }
    }

    public void killRobot(String name) throws IllegalStateException {
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
