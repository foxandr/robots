package fox.alex.robots.util.exception;

/**
 * Created by fox on 17.01.17.
 */

public class NoRobotException extends RuntimeException {

    public NoRobotException() {
        super("There are no robots, sorry.");
    }
}
