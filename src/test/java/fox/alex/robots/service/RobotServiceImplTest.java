package fox.alex.robots.service;

import fox.alex.robots.model.robot.BuildRobot;
import fox.alex.robots.model.robot.ProgRobot;
import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.robot.SingRobot;
import fox.alex.robots.util.exception.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static fox.alex.robots.testData.RobotTestData.*;


/**
 * Created by fox on 20.01.17.
 */

@ContextConfiguration(
        "classpath:spring/spring-test.xml"
)
@RunWith(SpringJUnit4ClassRunner.class)
public class RobotServiceImplTest {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private RobotService robotService;

    @Autowired
    private ExecutorService robotsPool;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        private void logInfo(Description description, long nanos) {
            LOG.info(String.format("+++ Test %s spent %d microseconds",
                    description.getMethodName(), TimeUnit.NANOSECONDS.toMicros(nanos)));
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, nanos);
        }
    };

    @Before
    public void setUp() throws Exception {
        robotsPool.submit(new Runnable() {
            @Override
            public void run() {
                while (true){}
            }
        });
        Field fld = Arrays.stream(robotService.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPrivate(field.getModifiers()))
                .filter(field -> field.getName().equals("robotsMap"))
                .findFirst()
                .get();
        fld.setAccessible(true);
        ConcurrentHashMap<String, Robot> robotsMap = (ConcurrentHashMap<String, Robot>) fld.get(robotService);
        robotsMap.clear();
        setStateForAll(ROBOTS_LIST, true);
        setTaskForAll(ROBOTS_LIST, null);
        ROBOTS_LIST.stream()
                .forEach(r -> robotService.addRobot(r));
    }

    @Test
    public void addRobot() throws Exception {
        Robot newRobot = new BuildRobot();
        robotService.addRobot(newRobot);
        MATCHER.assertEquals(newRobot, robotService.getRobotByName(newRobot.name));
    }

    @Test (expected = TooManyRobotsException.class)
    public void addRobotTooManyException() {
        for (int i = 0; i < RobotServiceImpl.MAP_SIZE + 10; i++) robotService.addRobot(new BuildRobot());
    }

    @Test
    public void addRobotWithTask() throws Exception {
        Robot newRobot = new BuildRobot();
        newRobot.setTask(CODE.typeTask);
        robotService.addRobotWithTask(newRobot);
        MATCHER.assertEquals(newRobot, robotService.getRobotByName(newRobot.name));
    }

    @Test (expected = TooManyRobotsException.class)
    public void addRobotWithTaskTooManyException() {
        for (int i = 0; i < RobotServiceImpl.MAP_SIZE + 10; i++) robotService.addRobotWithTask(new BuildRobot());
    }

    @Test
    public void removeRobot() throws Exception {
        robotService.removeRobot(SINGER);
        Collection<Robot> expected = sortRobots(Arrays.asList(PROGRAMMER, BUILDER));
        Collection<Robot> actual = sortRobots(robotService.getAllRobots());
        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test (expected = RobotNotFoundException.class)
    public void removeRobotNotFound() {
        Robot robot = new SingRobot();
        robotService.removeRobot(robot);
    }

    @Test
    public void removeRobotByName() throws Exception {
        robotService.removeRobotByName(PROGRAMMER.name);
        Collection<Robot> expected = sortRobots(Arrays.asList(SINGER, BUILDER));
        Collection<Robot> actual = sortRobots(robotService.getAllRobots());
        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test (expected = RobotNotFoundException.class)
    public void removeRobotByNameNotFound() {
        Robot robot = new ProgRobot();
        robotService.removeRobotByName(robot.name);
    }

    @Test
    public void addPersonalTask() throws Exception {
        robotService.addPersonalTask(SINGER.name, CODE.typeTask);
        Assert.assertEquals(CODE.typeTask, robotService.getRobotByName(SINGER.name).getTask());
    }

    @Test (expected = BusyRobotException.class)
    public void addPersonalTaskBusy() {
        SINGER.setTask(SING.typeTask);
        robotService.addPersonalTask(SINGER.name, CODE.typeTask);
    }

    @Test
    public void addBroadcastTask() throws Exception {
        robotService.addBroadcastTask(DISCOVER.typeTask);
        robotService.getAllRobots().stream()
                .forEach(robot -> Assert.assertEquals(DISCOVER.typeTask, robot.getTask()));
    }

    @Test (expected = BusyAllRobotsException.class)
    public void addBroadcastTaskAllBusy() {
        robotService.addBroadcastTask(DISCOVER.typeTask);
        robotService.addBroadcastTask(BUILD.typeTask);
    }

    @Test (expected = NoRobotException.class)
    public void addBroadcastTaskNoRobots() {
        ROBOTS_LIST.stream()
                .forEach(robot -> robotService.removeRobot(robot));
        robotService.addBroadcastTask(DISCOVER.typeTask);
    }

    @Test
    public void checkDeadRobots() throws Exception {
        setState(robotService.getRobotByName(SINGER.name), false);
        setState(robotService.getRobotByName(PROGRAMMER.name), false);
        Collection<Robot> acctual = sortRobots(robotService.checkDeadRobots());
        Collection<Robot> expected = sortRobots(ROBOTS_LIST
                .stream()
                .filter(robot -> !robot.isAlive())
                .collect(Collectors.toList())
        );
        MATCHER.assertCollectionEquals(expected, acctual);
    }

    @Test
    public void getRobotByName() throws Exception {
        Robot actual = robotService.getRobotByName(BUILDER.name);
        MATCHER.assertEquals(BUILDER, actual);
    }

    @Test (expected = RobotNotFoundException.class)
    public void getRobotByNameNotFound() {
        Robot actual = robotService.getRobotByName("NotFound-0");
    }

    @Test
    public void getNumberOfRobots() throws Exception {
        int expected = ROBOTS_LIST.size();
        int actual = robotService.getNumberOfRobots();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllRobots() throws Exception {
        Collection<Robot> expected = sortRobots(ROBOTS_LIST);
        Collection<Robot> actual = sortRobots(robotService.getAllRobots());
        MATCHER.assertCollectionEquals(expected, actual);
    }

}