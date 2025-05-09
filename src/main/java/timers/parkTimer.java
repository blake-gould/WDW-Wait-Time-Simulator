package timers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class parkTimer {
    
    private int multiplier;
    private final ArrayList<parkTimerTask> tasks;
    private Timer timer;

    public parkTimer(int multiplier, ArrayList<parkTimerTask> tasks) {
        this.multiplier = multiplier;
        this.tasks = new ArrayList<>(tasks); // defensive copy
        this.timer = new Timer();
    }

    public parkTimer changeTimer(int multiplier) {
        this.multiplier = multiplier;
        this.pause();
        return this.resume();
    }

    public void pause() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public parkTimer resume() {
        this.timer = new Timer();
        scheduleAllTasks();
        return this;
    }

    public void start() {
        scheduleAllTasks();
    }

    private void scheduleAllTasks() {
        for (parkTimerTask p : tasks) {
            Task t = new Task(p.runnable);
            if (p.interval == -1) {
                timer.schedule(t, p.delay);
            } else {
                long adjustedInterval = Math.max(1, p.interval / multiplier); // prevent 0 or negative
                timer.schedule(t, p.delay, adjustedInterval);
            }
        }
    }

    private static class Task extends TimerTask {
        private final Runnable runnable;

        public Task(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
        }
    }
}
