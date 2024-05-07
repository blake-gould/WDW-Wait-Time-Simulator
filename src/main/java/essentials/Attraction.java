package essentials;


import graphics.Flag;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blake
 */
public class Attraction {
    
    public String name;
    
    // The time it takes to unload someone from the ride 
    // This value is based off of the ride hourly capacity
    public int outFlowTime;
    
    // The time it takes for the Family to experience the attraction
    public double expTime;
    
    // The amount of people that can be waiting in the line at one time
    public int lineCapacity = 2000;
    
    // The amount of people that can be on the ride at one time
    public int rideCapacity = 0;
    public String targetAudience;
    
    public boolean fpswitch;
    
    
    public int totalWaitTime = 0;
    
    // Determines if the attraction is open to the public
    public boolean closed = false;
    
    public Queue<Family> line;
    public Queue<Family> fline;
    public Queue<Family> ride;
   
    public int x;
    public int y;


    public Color color;
    public Color textColor;
    
    public Point exitPoint;
    public Point earlyExitPoint;
    public int priority;
    
    public Flag flag;
    
    // Ride Constructor
    public Attraction(String name, double outFlowTime,  String targetAudience, double expTime, int priority, int x, int y, Color color) {
        this.priority = priority;
        this.outFlowTime = (int)(13 * outFlowTime);
        this.rideCapacity = 5000;
        this.expTime = expTime;
        line = new ArrayBlockingQueue(lineCapacity);
        fline = new ArrayBlockingQueue(lineCapacity);
        ride = new ArrayBlockingQueue(rideCapacity);
        this.name = name;
        this.targetAudience = targetAudience;
        
        setGraphics(x,y,color);
    }
    
    
    /*
    This method sets the basic graphics for the attraction,
    location and colors. This method should be called shortly after the constructor
    parameters: int x - x location
                int y - y location
                Color color - main color the attraction is associated with
    Values Altered: x location,
                    y location,
                    main Color
                    text Color - the color of the text associated w/ the attraction (blk or wht)
                    this.ExitPoint - where the Family exits the ride
                    this.EarlyExitPoint - where the Family exits the ride prematurely
    */
    private void setGraphics(int x, int y, Color color) {
        while (x % 10 != 0) {
            x--;
        }
        while (y % 10 != 0) {
            y--;
        }
        this.x = x;
        this.y = y;

        this.color = color;
        boolean reddark = color.getRed() < 80;
        boolean greendark = color.getGreen() < 80;
        boolean bluedark = color.getBlue() < 80;
        if ((reddark && greendark) || (reddark && bluedark) || (bluedark && greendark)) {
            textColor = Color.white;
        } else {
            textColor = Color.black;
        }
        
        // Initialize the exit point
        setExitPoint(x + 50, y + 50);

        //Initialize the Early exit point
        setEarlyExitPoint(x + 30, y + 30);
        
        //Initialize the Flags
        flag = new Flag(x, y - 45, 20, 18, color);
   
    }
    
    /*
    Adds the att to the panel in the form of a white box base
    And Includes: - A label w/ color = this.color
                  - Text on the label = this.name
                  - A white box for the base
                  - A blue box for entry point (configured via new Point(this.x, this.y)
                  - A yellow box for early exit point
                  - A green box for exit point
    Parameters:   - Graphics g - java graphics
    */
    public void draw(Graphics g) {
        // Draw a flag for each ride
        g.setColor(color);
        flag.draw(g);
        
        // Draw the Label
        g.setColor(this.color);
        int length = (int) (this.name.length() * 8.5);
        g.fillRect(this.x + 5, this.y - 15, length, 15);
        g.setColor(this.textColor);
        g.setFont(new java.awt.Font("Courier New", 1, 14));
        g.drawString(name, x + 5 , y - 2 );
        
        //Draw the Base
        g.setColor(new Color(240, 240, 240));
        g.fillRect(x, y, 60, 60);
        g.setColor(color);
        g.drawRect(x, y, 60, 60);

        // Draw Entry Point
        g.setColor(Color.blue);
        g.fillRect(x, y, 10, 10); 
        // Draw Early Exit Point
        g.setColor(Color.yellow);
        g.fillRect(earlyExitPoint.x, earlyExitPoint.y, 10, 10);
        // Draw Exit Point
        g.setColor(Color.green);
        g.fillRect(exitPoint.x, exitPoint.y, 10, 10);
        
        // Draw Entry/Exit Point borders
        g.setColor(Color.black);
        g.drawRect(x, y, 10, 10);
        g.drawRect(earlyExitPoint.x, earlyExitPoint.y, 10, 10);
        g.drawRect(exitPoint.x, exitPoint.y, 10, 10);
        
        // Draw the wait time
        float f=20.0f; // font size.
        g.setColor(Color.black);
        g.setFont(g.getFont().deriveFont(f));
        g.drawString("" + this.getWaitTime(), x + 20, y + 15);
    }
    
    /*
    Returns the main entry point for the att
    Found at this.x , this.y
    */
    public Point getAccessPoint() {
        return new Point(x, y);
    }
    
    /*
    Returns the main exit point for the att
    Found at this.x + 50 , this.y + 50
    */
    public Point getExitPoint() {
        return exitPoint;
    }
    
    /*Returns the early exit point for the att
    Found at this.x + 30 , this.y + 30
    */
    public Point getEarlyExitPoint() {
        return earlyExitPoint;
    }
    
    private void setExitPoint(int a, int b) {
        exitPoint = new Point(a, b);
    }
    
    private void setEarlyExitPoint(int a, int b) {
        earlyExitPoint = new Point(a, b);
    }
    
    public  void setAccessPoint(int a, int b) {
        setGraphics(a, b, color);
    }
    
    public boolean addToQueue(Family g) {
        if (line.size() < lineCapacity) {
            line.add(g);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean addToFPQueue(Family g) {
        if (fline.size() < lineCapacity) {
            fline.add(g);
            return true;
        } else {
            return false;
        }
    }
    
    public void setWaitTime() {
        // The amount of time for an individual if they were to get into the end of the line 
        // queue would be equal to the inFlowTime (0.5 * outflowTime) * # of people in line before them
        // *In minutes PLUS the actual outFlowTime (0.5 * outflowTime) * # of people "on the ride" ahead of them
        totalWaitTime = ((outFlowTime / 2) * line.size()) / 60000;
        totalWaitTime += ((outFlowTime / 2) * fline.size()) / 60000;
        totalWaitTime += ((outFlowTime / 2) * ride.size()) / 60000;
        while (totalWaitTime % 5 != 0) {
            totalWaitTime++;
        }
    }
    
    public int getWaitTime() {
        return totalWaitTime;
    }
    
    public String toString() {
        //return name + " " + totalWaitTime + " " + line.size() + " " + ride.size() + " " + fline.size();
        String retstr = "";
        retstr += name;
        while (retstr.length() < 40) {
            retstr += " ";
        }
        retstr += " L:" + line.size();
        while (retstr.length() < 26) {
            retstr += " ";
        }
        retstr += " FL:" + fline.size();
        while (retstr.length() < 33) {
            retstr += " ";
        }
        retstr += " R:" + ride.size();
        while (retstr.length() < 39) {
            retstr += " ";
        }
        retstr += " OFT:" + (outFlowTime / 13);
        while (retstr.length() < 48) {
            retstr += " ";
        }
        retstr += " ET:" + expTime;
        while (retstr.length() < 55) {
            retstr += " ";
        }
        return retstr;
        
    }
}
