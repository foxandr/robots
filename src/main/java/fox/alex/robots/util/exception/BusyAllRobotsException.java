package fox.alex.robots.util.exception;

/**
 * Created by fox on 17.01.17.
 */

public class BusyAllRobotsException extends RuntimeException {

    public BusyAllRobotsException() {
        super("Can't execute broadcast task. All robots are busy.");
    }
}
