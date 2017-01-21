package fox.alex.robots.model.robot;

import fox.alex.robots.model.task.TypeTask;

/**
 * Created by fox on 16.01.17.
 */

public class ProgRobot extends Robot {

    private volatile static Integer counter = 0;

    public ProgRobot() {
        super("ProgRobot-" + counter++);
    }

    protected void perform() throws InterruptedException {
        if (TypeTask.WRITE_CODE.equals(this.task)){
            super.logQueue.add(this.name + ":msg.code.good");
            Thread.sleep(1000L);
            super.logQueue.add(this.name + ":msg.all.goodresult");
        } else {
            super.logQueue.add(this.name + ":msg.code.bad");
            Thread.sleep(5000L);
            super.logQueue.add(this.name + ":msg.all.badresult");
        }
    }
}
