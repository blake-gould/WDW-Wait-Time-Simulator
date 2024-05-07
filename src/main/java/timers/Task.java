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
public class Task extends TimerTask {
            Runnable runnable;
            public Task(Runnable runnable) {
                this.runnable = runnable;
            }
            @Override
            public void run() {
                runnable.run();
            }
        }