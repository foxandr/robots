package fox.alex.robots.web;

import fox.alex.robots.model.robot.Robot;
import fox.alex.robots.model.task.Task;
import fox.alex.robots.service.RobotService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static fox.alex.robots.testData.RobotTestData.ROBOTS_LIST;
import static fox.alex.robots.testData.RobotTestData.setStateForAll;
import static fox.alex.robots.testData.RobotTestData.setTaskForAll;

/**
 * Created by fox on 02.03.17.
 */

@ContextConfiguration({
        "classpath:spring/spring-test-for-mvc.xml",
        "classpath:spring/spring-mvc.xml"
})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractRobotControllerTest {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected RobotService robotService;

    @Resource
    protected Queue<Task> taskQueue;

    @Resource
    protected Queue<String> logQueue;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        private void logInfo(Description description, long nanos) {
            LOG.info(String.format("+++ Test %s spent %d microseconds",
                    description.getMethodName(), TimeUnit.NANOSECONDS.toMicros(nanos)));
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, nanos);
        }
    };

    @PostConstruct
    private void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void setUp() throws Exception {
        Field fld = Arrays.stream(robotService.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPrivate(field.getModifiers()))
                .filter(field -> field.getName().equals("robotsMap"))
                .findFirst()
                .get();
        fld.setAccessible(true);
        ConcurrentHashMap<String, Robot> robotsMap = (ConcurrentHashMap<String, Robot>) fld.get(robotService);
        robotsMap.clear();
        setStateForAll(ROBOTS_LIST, true);
        setTaskForAll(ROBOTS_LIST, null);
        ROBOTS_LIST.stream()
                .forEach(r -> robotService.addRobot(r));
    }

    @After
    public void tearDown() throws Exception {

    }



}
