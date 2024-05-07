/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package essentials;

import essentials.Attraction;
import essentials.Fastpass;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blake
 */
public class Family {
    //public int id;
    public String age;
    public Point location;
    public Point destination;
    public Color C;
    
    public boolean transferred;
    public int oldPark;
    public Park oldP;
    
    // An integer to pair w the Attraction: expTime:
    // Determines how long the individual has been on their current
    // ride in minutes
    public double expTimer;
    
    
    public Attraction attractionDestination;
    public Park parkDestination;
    public HashSet<Attraction> ridesRidden = new HashSet<>();
    public boolean tracking = false;
    public int hungerStatus;
    
   
    
    public ArrayList<Fastpass> fpasses = new ArrayList<>();
    
    public Family(String age) {
        this.age = age;
        this.location = new Point();
        this.destination = new Point();
        transferred = false;
    }
    
    
    public void setDestination(Attraction att) {
        if (att != null) {
            destination = att.getAccessPoint();
            attractionDestination = att;
        }
        
    }
    
    public void setLocation (int x, int y) {
        this.location = new Point(x, y);
    }
    
    public void setLocation (Point p) {
        this.location = p;
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.darkGray);
        if (tracking) {
            g.setColor(Color.white);
        }
        
        g.fillOval((int)location.getX() + 1, (int)location.getY() + 1, 8, 8);
        if (tracking) {
            g.setColor(Color.red);
            g.drawOval((int)location.getX() + 1, (int)location.getY() + 1, 8, 8);
        }
        
    }

}
