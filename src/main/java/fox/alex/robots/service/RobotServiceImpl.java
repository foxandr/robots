package fox.alex.robots.service;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Created by fox on 16.01.17.
 *
 * Сервис, который управляет запуском задач работов и хранением бездействующих роботов.
 * Информация о роботах находится в мапе. Периодически из мапы удаляются мертвые роботы и их место занимают новые.
 * Так же, если робот получил новое задание, то он отправляется в robotsPool (CachedThreadPool)
 * Если роботов очень много (MAP_SIZE) то, выбрасыается исключение и новые роботы не добавляются.
 *
 */

@Service
public class RobotServiceImpl implements RobotService {

    @Autowired
    private ExecutorService robotsPool;

    private volatile ConcurrentHashMap<String, Robot> robotsMap = new ConcurrentHashMap<String, Robot>();

    public final static int MAP_SIZE = 100;

    public void addRobot(Robot robot) throws TooManyRobotsException {
        if (robotsMap.size() >= MAP_SIZE) throw new TooManyRobotsException();
        robotsMap.putIfAbsent(robot.name, robot);
    }

    public void addRobotWithTask(Robot robot) throws TooManyRobotsException {
        addRobot(robot);
        robotsPool.submit(robot);
    }

    public void removeRobot(Robot robot) throws RobotNotFoundException {
        if (!robotsMap.containsValue(robot)) throw new RobotNotFoundException(robot.name);
        robotsMap.remove(robot.name);
    }

    public void removeRobotByName(String robotName) throws RobotNotFoundException {
        if (!robotsMap.containsKey(robotName)) throw new RobotNotFoundException(robotName);
        robotsMap.remove(robotName);
    }

    public void addPersonalTask(String name, TypeTask task) throws BusyRobotException, RobotNotFoundException {
        Robot robot = getRobotByName(name);
        if (!robot.isAlive()) throw new RobotNotFoundException(name);
        robot.setTask(task);
        robotsPool.submit(robot);
    }

    public void addBroadcastTask(TypeTask task) throws BusyAllRobotsException, NoRobotException {
        Collection<Robot> robots = getAllRobots();
        int size = robots.size();
        if (size == 0) throw new NoRobotException();
        final int[] busy = {0};
        robots.stream()
                .forEach(r -> {
                            try {
                                r.setTask(task);
                                robotsPool.submit(r);
                            } catch (BusyRobotException e) {
                                busy[0]++;
                            }
                        }
                );
        if (busy[0] == size) throw new BusyAllRobotsException();
    }

    public Collection<Robot> checkDeadRobots() {
        Collection<Robot> deadRobots = getAllRobots().stream()
                .filter(r -> !r.isAlive())
                .collect(Collectors.toList());
        deadRobots.stream()
                .forEach(r -> robotsMap.remove(r.name));
        return deadRobots;
    }

    public Robot getRobotByName(String name) throws RobotNotFoundException {
        if (!robotsMap.containsKey(name)) throw new RobotNotFoundException(name);
        return robotsMap.get(name);
    }

    public int getNumberOfRobots() {
        return robotsMap.size();
    }

    public Collection<Robot> getAllRobots() {
        return robotsMap.values();
    }
}
