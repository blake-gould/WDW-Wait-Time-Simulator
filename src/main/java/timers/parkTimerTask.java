package timers;


import java.util.TimerTask;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blake
 */
public class parkTimerTask {
    public TimerTask task;
    public int delay;
    public int interval;
    public Runnable runnable;
    
    public parkTimerTask(TimerTask task, int delay, int interval, Runnable runnable) {
        this.task = task;
        this.delay = delay;
        this.interval = interval;
        this.runnable = runnable;
    }
    
    public parkTimerTask(TimerTask task, int delay, Runnable runnable) {
        this.task = task;
        this.delay = delay;
        this.interval = -1;
        this.runnable = runnable;
    }
}
