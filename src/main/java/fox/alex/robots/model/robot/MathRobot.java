package fox.alex.robots.model.robot;

import fox.alex.robots.model.task.TypeTask;

/**
 * Created by fox on 16.01.17.
 */

public class MathRobot extends Robot {

    private volatile static Integer counter = 0;

    public MathRobot() {
        super("MathRobot-" + counter++);
    }

    protected void perform() throws InterruptedException {
        if (TypeTask.COUNT_NUMBERS.equals(this.task)){
            super.logQueue.add(this.name + ":msg.calc.good");
            Thread.sleep(5000L);
            super.logQueue.add(this.name + ":msg.all.goodresult");
        } else {
            super.logQueue.add(this.name + ":msg.calc.bad");
            Thread.sleep(10000L);
            super.logQueue.add(this.name + ":msg.all.badresult");
        }
    }
}
