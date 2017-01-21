package fox.alex.robots.web;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.task.Task;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.service.RobotService;
import fox.alex.robots.util.RobotGenerator;
import fox.alex.robots.util.exception.RobotNotFoundException;
import fox.alex.robots.util.exception.TooManyRobotsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public void sendTask(Task task) {
        try {
            taskQueue.add(task);
        } catch (IllegalStateException e){
            LOG.debug(e.getMessage(), e);
        }
    }

    public void addNewRobot(TypeTask task) {
        Robot newRobot = RobotGenerator.getRobot(task);
        newRobot.setLogQueue(logQueue);
        try {
            robotService.addRobot(newRobot);
            logQueue.add(newRobot.name + ":msg.wc.newrobot");
        } catch (TooManyRobotsException e){
            logQueue.add("WorldController:msg.wc.over");
            LOG.debug(e.getMessage(), e);
        }
    }

    public void killRobot(String name) throws IllegalStateException {
        try {
            robotService.removeRobotByName(name);
            logQueue.add(name + ":msg.wc.kill");
        } catch (RobotNotFoundException e){
            logQueue.add(name + ":msg.wc.nfd");
            LOG.debug(e.getMessage(), e);
        }
    }

    public Collection<Robot> getAllRobots(){
        return robotService.getAllRobots();
    }
}
