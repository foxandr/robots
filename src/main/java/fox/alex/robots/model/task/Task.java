package fox.alex.robots.model.task;

/**
 * Created by fox on 16.01.17.
 */

public class Task {

    public final String robotName;
    public final TypeTask typeTask;

    public Task(String robotName, TypeTask typeTask) {
        this.robotName = robotName;
        this.typeTask = typeTask;
    }

    @Override
    public int hashCode() {
        char[] chars = robotName.toCharArray();
        int result = 0;
        for (char c : chars){
            result = 31 * result + c;
        }
        result = 31 * result + typeTask.ordinal();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task that = (Task) o;
        if (typeTask == null || that.typeTask == null) return false;
        if (!typeTask.equals(that.typeTask)) return false;
        if (robotName == null && that.robotName == null) return true;
        if (robotName == null || that.robotName == null) return false;
        return robotName.equals(that.robotName);
    }

    @Override
    public String toString() {
        return "Task{" +
                "robotName='" + robotName + '\'' +
                ", typeTask=" + typeTask +
                '}';
    }
}
