package fox.alex.robots.testData;

import fox.alex.robots.matcher.ModelMatcher;
import fox.alex.robots.model.robot.BuildRobot;
import fox.alex.robots.model.robot.ProgRobot;
import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.robot.SingRobot;
import fox.alex.robots.model.task.Task;
import fox.alex.robots.model.task.TypeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by fox on 21.01.17.
 */

public class RobotTestData {

    private static final Logger LOG = LoggerFactory.getLogger(RobotTestData.class);

    public static final Robot BUILDER = new BuildRobot();
    public static final Robot SINGER = new SingRobot();
    public static final Robot PROGRAMMER = new ProgRobot();

    public static final Task BUILD = new Task(BUILDER.name, TypeTask.BUILD_HOUSE);
    public static final Task SING = new Task(SINGER.name, TypeTask.SING_SONG);
    public static final Task CODE = new Task(PROGRAMMER.name, TypeTask.WRITE_CODE);

    public static final String CLASS_NAME = "ScienceRobot";

    public static final Task DISCOVER = new Task(null, TypeTask.DISCOVER_NEW);

    public static final Collection<Robot> ROBOTS_LIST = Arrays.asList(BUILDER, SINGER, PROGRAMMER);

    public static final ModelMatcher<Robot> MATCHER = new ModelMatcher<>(Robot.class,
            (expected, actual) -> {
                if (expected == actual) {
                    return true;
                }
                boolean cmp = Objects.equals(expected.name, actual.name)
                        && Objects.equals(expected.isAlive(), actual.isAlive())
                        && Objects.equals(expected.getTask(), actual.getTask());
                return cmp;
            }
    );

    private static Comparator<Robot> comparator  = new Comparator<Robot>() {
        @Override
        public int compare(Robot robot, Robot t1) {
            return robot.name.compareTo(t1.name);
        }
    };

    public static void setState(Robot robot, boolean state) {
        Field fld = Arrays.stream(robot.getClass().getSuperclass().getDeclaredFields())
                .filter(field -> Modifier.isPrivate(field.getModifiers()))
                .filter(field -> field.getName().equals("alive"))
                .findFirst()
                .get();
        fld.setAccessible(true);
        try {
            fld.setBoolean(robot, state);
        } catch (IllegalAccessException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static void setStateForAll(Collection<Robot> robots, boolean state){
        robots.stream().forEach(robot -> setState(robot, state));
    }

    public static void setTaskForAll(Collection<Robot> robots, TypeTask task){
        robots.stream().forEach(robot -> {
            Field fld = Arrays.stream(robot.getClass().getSuperclass().getDeclaredFields())
                    .filter(field -> field.getName().equals("task"))
                    .findFirst()
                    .get();
            fld.setAccessible(true);
            try {
                fld.set(robot, task);
            } catch (IllegalAccessException e) {
                LOG.error(e.getMessage(), e);
            }
        });
    }

    public static Collection<Robot> sortRobots(Collection<Robot> robots){
        return robots.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
