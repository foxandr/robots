package fox.alex.robots.model.robot;

import fox.alex.robots.model.task.TypeTask;

/**
 * Created by fox on 16.01.17.
 */

public class ScienceRobot extends Robot {

    private volatile static Integer counter = 0;

    public ScienceRobot() {
        super("ScienceRobot-" + counter++);
    }

    protected void perform() throws InterruptedException {
        if (TypeTask.DISCOVER_NEW.equals(this.task)){
            super.logQueue.add(this.name + ":msg.discover.good");
            Thread.sleep(7000L);
            super.logQueue.add(this.name + ":msg.all.goodresult");
        } else {
            super.logQueue.add(this.name + ":msg.discover.bad");
            Thread.sleep(10000L);
            super.logQueue.add(this.name + ":msg.all.badresult");
        }
    }
}
