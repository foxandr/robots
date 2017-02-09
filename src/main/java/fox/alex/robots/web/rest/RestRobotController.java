package fox.alex.robots.web.rest;

import fox.alex.robots.model.task.Task;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.web.AbstractRobotController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by fox on 19.01.17.
 */

@RestController
@RequestMapping(RestRobotController.REST_URL)
public class RestRobotController extends AbstractRobotController {

    public static final String REST_URL = "/rest";

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/robots/{name}/tasks/{taskId}", method = RequestMethod.PUT)
    public void personalTask(@PathVariable("name") String robotName, @PathVariable("taskId") int taskId){
        Task task = new Task(robotName, TypeTask.values()[taskId]);
        super.sendTask(task);
    }

    @RequestMapping(value = "/robots/all/tasks/{taskId}", method = RequestMethod.PUT)
    public void broadcastTask(@PathVariable("taskId") int taskId){
        Task task = new Task(null, TypeTask.values()[taskId]);
        super.sendTask(task);
    }

    @RequestMapping(value = "/robots", method = RequestMethod.POST)
    public void add(@RequestParam("id") int taskId) {
        super.addNewRobot(TypeTask.values()[taskId]);
    }

    @RequestMapping(value = "/robots/{name}", method = RequestMethod.DELETE)
    public void kill(@PathVariable("name") String robotName){
        super.killRobot(robotName);
    }

    @RequestMapping(value = "/robots/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> get(){
        return super.getAllRobots().stream().map(r -> r.name).collect(Collectors.toList());
    }

    @RequestMapping(value = "/logs/{lines}/language/{lang}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> logs(@PathVariable("lines") int lines, @PathVariable("lang") String lang){
        int skipMgs = (lines > logQueue.size()) ? 0 : logQueue.size() - lines;
        if (lang == null) lang = "en";
        Locale locale = (lang.equals("ru")) ? new Locale("ru") : new Locale("en");
        return logQueue.stream()
                .skip(skipMgs)
                .map(str -> str.split(":"))
                .map(e -> e[0] + " : " + messageSource.getMessage(e[1], null, locale))
                .collect(Collectors.toList());
    }

}
