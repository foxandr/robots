package fox.alex.robots.web.rest;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.util.RobotGenerator;
import fox.alex.robots.web.AbstractRobotControllerTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collection;

import static fox.alex.robots.testData.RobotTestData.CLASS_NAME;
import static fox.alex.robots.testData.RobotTestData.DISCOVER;
import static fox.alex.robots.testData.RobotTestData.ROBOTS_LIST;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by fox on 02.03.17.
 */
public class RestRobotControllerTest extends AbstractRobotControllerTest {

    private static final String REST_URL = RestRobotController.REST_URL;

    @Test
    public void personalTask() throws Exception {

    }

    @Test
    public void broadcastTask() throws Exception {

    }

    @Test
    public void add() throws Exception {
        int taskId = DISCOVER.typeTask.ordinal();
        ResultActions actions = mockMvc.perform(post(REST_URL + "/robots")
                .param("id", String.valueOf(taskId)))
                .andExpect(status().isOk());
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

    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void logs() throws Exception {

    }

}