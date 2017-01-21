package fox.alex.robots.model.robot;

import fox.alex.robots.model.task.TypeTask;

/**
 * Created by fox on 16.01.17.
 */

public class SingRobot extends Robot {

    private volatile static Integer counter = 0;

    public SingRobot() {
        super("SingRobot-" + counter++);
    }

    protected void perform() throws InterruptedException {
        if (TypeTask.SING_SONG.equals(this.task)){
            super.logQueue.add(this.name + ":msg.sing.good");
            Thread.sleep(2000L);
            super.logQueue.add(this.name + ":msg.all.goodresult");
        } else {
            super.logQueue.add(this.name + ":msg.sing.bad");
            Thread.sleep(6000L);
            super.logQueue.add(this.name + ":msg.all.badresult");
        }
    }
}
