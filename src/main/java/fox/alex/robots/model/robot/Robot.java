package fox.alex.robots.model.robot;

import fox.alex.robots.model.task.TypeTask;
import fox.alex.robots.util.exception.BusyRobotException;

import java.util.Queue;

/**
 * Created by fox on 16.01.17.
 *
 * Базовый класс для Роботов. Я не использовал интерфейс Callable так как задумывал,
 * что роботы в процессе выполнения будут писать сообщения в WebUI и больше им ничего не надо возвращать.
 *
 * alive - показывает, что робот впорядке и может выполнять задания, если же false, то робот уничтожается.
 *
 * logQueue - очередь, куда работ пишет свои сообщения в процессе работы, чтобы они отображались в WebUI
 *
 * task - задание, которое должен выполнить работ, после его выполнения, задание удаляется. это также
 * признак занятости робота.
 *
 */

public abstract class Robot implements Runnable {

    private volatile boolean alive = true;

    protected Queue<String> logQueue;

    public final String name;

    protected volatile TypeTask task = null;

    public Robot(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setTask(TypeTask task) throws BusyRobotException {
        if (this.task != null) throw new BusyRobotException(name);
        this.task = task;
    }

    public TypeTask getTask() {
        return task;
    }

    public void setLogQueue(Queue<String> logQueue) {
        this.logQueue = logQueue;
    }

    protected abstract void perform() throws InterruptedException;

    public void run() {
        try {
            if (this.task.equals(TypeTask.SUICIDE)){
                alive = false;
                logQueue.add(name + ":msg.suicide");
            } else {
                perform();
            }
        } catch (InterruptedException e) {
            alive = false;
            logQueue.add(name + ":msg.all.error");
        } finally {
            this.task = null;
        }
    }

    @Override
    public String toString() {
        return "My name is " + name;
    }

    @Override
    public int hashCode() {
        char[] chars = name.toCharArray();
        int result = 0;
        for (char c : chars){
            result = 31 * result + c;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot that = (Robot) o;
        if (name == null || that.name == null) return false;
        return name.equals(that.name);
    }
}
