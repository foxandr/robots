package fox.alex.robots.web.rest;

import fox.alex.robots.model.task.Task;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.web.AbstractRobotController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/personal", method = RequestMethod.POST)
    public void personalTask(@RequestParam("name") String robotName, @RequestParam("id") int taskId){
        Task task = new Task(robotName, TypeTask.values()[taskId]);
        super.sendTask(task);
    }

    @RequestMapping(value = "/broadcast", method = RequestMethod.POST)
    public void broadcastTask(@RequestParam("id") int taskId){
        Task task = new Task(null, TypeTask.values()[taskId]);
        super.sendTask(task);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestParam("id") int taskId) {
        super.addNewRobot(TypeTask.values()[taskId]);
    }

    @RequestMapping(value = "/kill", method = RequestMethod.POST)
    public void kill(@RequestParam("suicide") String robotName){
        super.killRobot(robotName);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> get(){
        return super.getAllRobots().stream().map(r -> r.name).collect(Collectors.toList());
    }

    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> logs(@RequestParam("num") int num, @RequestParam("lang") String lang){
        int skipMgs = (num > logQueue.size()) ? 0 : logQueue.size() - num;
        if (lang == null) lang = "en";
        Locale locale = (lang.equals("ru")) ? new Locale("ru") : new Locale("en");
        return logQueue.stream()
                .skip(skipMgs)
                .map(str -> str.split(":"))
                .map(e -> e[0] + " : " + messageSource.getMessage(e[1], null, locale))
                .collect(Collectors.toList());
    }

}
