package fox.alex.robots.model.robot;

import fox.alex.robots.model.task.TypeTask;

/**
 * Created by fox on 16.01.17.
 */

public class BuildRobot extends Robot {

    private volatile static Integer counter = 0;

    public BuildRobot() {
        super("BuildRobot-" + counter++);
    }

    protected void perform() throws InterruptedException {
        if (TypeTask.BUILD_HOUSE.equals(this.task)){
            super.logQueue.add(this.name + ":msg.build.good");
            Thread.sleep(10000L);
            super.logQueue.add(this.name + ":msg.all.goodresult");
        } else {
            super.logQueue.add(this.name + ":msg.build.bad");
            Thread.sleep(5000L);
            super.logQueue.add(this.name + ":msg.all.badresult");
        }
    }
}
