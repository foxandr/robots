package fox.alex.robots.util;

import fox.alex.robots.model.robot.*;
import fox.alex.robots.model.task.TypeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Created by fox on 16.01.17.
 */

public class RobotGenerator {

    private static Class[] classes = {BuildRobot.class, MathRobot.class, ProgRobot.class, ScienceRobot.class, SingRobot.class};
    private final static Logger LOG = LoggerFactory.getLogger(RobotGenerator.class.getSimpleName());

    public static Robot getRandomRobot() {
        Random random = new Random();
        int number = random.nextInt(classes.length);
        try {
            return (Robot) classes[number].newInstance();
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            return new BuildRobot();
        }
    }

    public static Collection<Robot> getRandomRobots(int number) {
        Collection<Robot> collection = new ArrayList<Robot>();
        for (int i = 0; i < number; i++){
            collection.add(getRandomRobot());
        }
        return collection;
    }

    public static Robot getRobot(TypeTask task) {
        switch (task){
            case BUILD_HOUSE: return new BuildRobot();
            case COUNT_NUMBERS: return new MathRobot();
            case DISCOVER_NEW: return new ScienceRobot();
            case SING_SONG: return new SingRobot();
            case WRITE_CODE: return new ProgRobot();
            default: return getRandomRobot();
        }
    }

    public static Robot getRobot(String className) {
        switch (className){
            case "BuildRobot": return new BuildRobot();
            case "MathRobot": return new MathRobot();
            case "ScienceRobot": return new ScienceRobot();
            case "SingRobot": return new SingRobot();
            default: return new ProgRobot();
        }
    }

}
