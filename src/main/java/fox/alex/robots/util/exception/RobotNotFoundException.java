package fox.alex.robots.util.exception;

/**
 * Created by fox on 17.01.17.
 */

public class RobotNotFoundException extends RuntimeException {

    public RobotNotFoundException(String name) {
        super("Sorry, Robot with name \'" + name + "\' wasn't found.");
    }
}
