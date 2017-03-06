package fox.alex.robots.web.ajax;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.web.AbstractRobotControllerTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static fox.alex.robots.testData.RobotTestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by fox on 06.03.17.
 */
public class AjaxRobotControllerTest extends AbstractRobotControllerTest {

    private static final String AJAX_URL = AjaxRobotController.AJAX_URL;

    @Test
    public void personalTask() throws Exception {
        Robot robot = ROBOTS_LIST.stream().findFirst().get();
        String robotName = robot.name;
        int taskId = TASK_ID;
        mockMvc.perform(put(AJAX_URL + "/robots/" + robotName + "/tasks/" + taskId))
                .andExpect(status().isOk())
                .andDo(print());
        Assert.assertTrue(logQueue.contains(robotName + ":msg.wc.personal"));
    }

    @Test
    public void broadcastTask() throws Exception {
        int taskId = TASK_ID;
        mockMvc.perform(put(AJAX_URL + "/robots/all/tasks/" + taskId))
                .andExpect(status().isOk())
                .andDo(print());
        Assert.assertTrue(logQueue.contains("WorldController:msg.wc.broadcast"));
    }

    @Test
    public void add() throws Exception {
        int taskId = DISCOVER.typeTask.ordinal();
        mockMvc.perform(post(AJAX_URL + "/robots")
                .param("id", String.valueOf(taskId)))
                .andExpect(status().isOk())
                .andDo(print());
        Assert.assertEquals(ROBOTS_LIST.size() + 1, robotService.getAllRobots().size());
        boolean rightType = false;
        for (Robot r : robotService.getAllRobots()){
            if (r.getClass().getSimpleName().equals(CLASS_NAME)) {
                rightType = true;
                break;
            }
        }
        Assert.assertTrue(rightType);
    }

    @Test
    public void kill() throws Exception {
        Robot robot = ROBOTS_LIST.stream().findFirst().get();
        String robotName = robot.name;
        Collection<Robot> expected = robotService.getAllRobots();
        expected.remove(robot);
        mockMvc.perform(delete(AJAX_URL + "/robots" + robotName))
                .andExpect(status().isOk())
                .andDo(print());
        Collection<Robot> actual = sortRobots(robotService.getAllRobots());
        MATCHER.assertCollectionEquals(sortRobots(expected), actual);
    }

    @Test
    public void getAll() throws Exception {
        Collection<String> expectedNames = ROBOTS_LIST.stream()
                .map(r -> r.name)
                .sorted()
                .collect(Collectors.toList());
        String[] expected = expectedNames.toArray(new String[expectedNames.size()]);
        ResultActions actions = mockMvc.perform(get(AJAX_URL + "/robots/all"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        String jsonResult = actions.andReturn().getResponse().getContentAsString();
        String[] actual = jsonResult.substring(1, jsonResult.length() - 1).replace("\"", "").split(",");
        Arrays.sort(actual);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void logs() throws Exception {
        List<String> expectedLogs = logQueue.stream()
                .map(str -> str.split(":"))
                .map(e -> e[0] + " : " + messageSource.getMessage(e[1], null, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());
        String[] expected = expectedLogs.toArray(new String[expectedLogs.size()]);
        ResultActions actions = mockMvc.perform(get(AJAX_URL + "/logs"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        String jsonResult = actions.andReturn().getResponse().getContentAsString();
        String[] actual = jsonResult.substring(1, jsonResult.length() - 1).replace("\"", "").split(",");
        Assert.assertArrayEquals(expected, actual);
    }

}