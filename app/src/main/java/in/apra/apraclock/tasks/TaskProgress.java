package in.apra.apraclock.tasks;

/**
 * Created by mamigo on 11/1/2016.
 */

public class TaskProgress {
    private int questions;
    private int attempts;
    private int corrects;
    private int goal;
    private RandomMathTask curTask;

    public TaskProgress(int goal) {
        this.goal = goal;
    }

    public int getCorrects() {
        return corrects;
    }

    public int getGoal() {
        return goal;
    }

    public boolean isDone() {
        return goal == corrects;
    }

    public boolean check(int answer) {
        attempts++;
        if (curTask.checkAnswer(answer)) {
            corrects++;
            return true;
        }
        return false;
    }

    public String ask() {
        questions++;
        curTask = new RandomMathTask();
        return curTask.toString();
    }
}
