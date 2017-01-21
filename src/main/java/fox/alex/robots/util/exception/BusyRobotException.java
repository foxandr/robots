package fox.alex.robots.util.exception;

/**
 * Created by fox on 17.01.17.
 */

public class BusyRobotException extends RuntimeException {

    public BusyRobotException(String name) {
        super(name + ": Sorry, am so busy robot.");
    }
}
