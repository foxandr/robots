package fox.alex.robots.util.exception;

/**
 * Created by fox on 17.01.17.
 */

public class TooManyRobotsException extends RuntimeException {

    public TooManyRobotsException() {
        super("Too many robots are in our world. Sorry.");
    }
}
