package fox.alex.robots.web.ajax;

import fox.alex.robots.model.task.Task;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.web.AbstractRobotController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fox on 18.01.17.
 */

@RestController
@RequestMapping(AjaxRobotController.AJAX_URL)
public class AjaxRobotController extends AbstractRobotController {

    public static final String AJAX_URL = "/ajax";

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

    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> logs(){
        return  logQueue.stream()
                .map(s -> s.split(":"))
                .map(e -> e[0] + " : " + messageSource.getMessage(e[1], null, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());
    }
}
