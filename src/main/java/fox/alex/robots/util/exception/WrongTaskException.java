package fox.alex.robots.util.exception;

/**
 * Created by fox on 01.03.17.
 */
public class WrongTaskException extends RuntimeException {

    public WrongTaskException() {
        super("Task with current ID doesn't exist.");
    }
}
