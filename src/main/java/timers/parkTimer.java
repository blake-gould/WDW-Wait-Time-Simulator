package timers;


import timers.parkTimerTask;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;
import java.util.TreeMap;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blake
 */
public class parkTimer extends Timer{
    
    public int multiplier;
    public ArrayList<parkTimerTask> tasks;

    
    
    
    
    
    public parkTimer(int multiplier, ArrayList<parkTimerTask> tasks) {
        this.multiplier = multiplier;
        this.tasks = tasks;
    }
    
    public parkTimer changeTimer(int multiplier) {
        this.multiplier = multiplier;
        this.pause();
        return this.resume();
        
    }
    
    public void pause() {
        this.cancel();
    }
    
    public parkTimer resume() {
        parkTimer replacement = new parkTimer(multiplier, tasks);
        replacement.restart();
        return replacement;          
    }
    
    public void start() {
        for (parkTimerTask p : tasks) {
            Task t = new Task(p.runnable);
            if (p.interval == -1) {
                this.schedule(t, p.delay );    
            } else {
                this.schedule(t, p.delay, p.interval / multiplier);
            }
            
        }

    }
    
    private void restart() {
        for (parkTimerTask p : tasks) {
            Task t = new Task(p.runnable);
            if (p.interval == -1) {
                // do nothing   
            } else {
                this.schedule(t, p.delay, p.interval / multiplier);
            }
            
        }    
    }

    
    
    
}
