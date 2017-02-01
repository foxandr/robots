package fox.alex.robots.service;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.util.exception.*;

import java.util.Collection;

/**
 * Created by fox on 16.01.17.
 */

public interface RobotService {

    void addRobot(Robot robot) throws TooManyRobotsException;

    void addRobotWithTask(Robot robot) throws TooManyRobotsException;

    Collection<Robot> checkDeadRobots();

    void removeRobot(Robot robot) throws RobotNotFoundException;

    void removeRobotByName(String robotName) throws RobotNotFoundException;

    int getNumberOfRobots();

    Collection<Robot> getAllRobots();

    Robot getRobotByName(String name) throws RobotNotFoundException;

    void addPersonalTask(String name, TypeTask task) throws BusyRobotException, RobotNotFoundException;

    void addBroadcastTask(TypeTask task) throws BusyAllRobotsException, NoRobotException;

}
