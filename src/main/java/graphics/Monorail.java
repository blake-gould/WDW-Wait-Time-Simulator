/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;
import java.awt.Graphics;

/**
 *
 * @author blake
 */
public class Monorail {
    
    private int x = -1, y = -1, radius = -1, size = -1, width = -1, height = -1;
    
    /*
    Constructs a Monorail with a cirular path
    */
    public Monorail(int x, int y, int radius, int size) {
        this(x, y, size);
        this.radius = radius;
    }
    
    /*
    Constructs a Monorail with a square path
    */
    public Monorail(int x, int y, int width, int height, int size) {
        this(x, y, size);
        this.width = width;
        this.height = height;
    }
    
    private Monorail(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }
    
    public void draw(Graphics g) {
        // Circle Path
        if (radius != -1) {
        g.drawArc(x, y, radius, radius, 0, 90);
        
        // Square Path
        } else {
            
        }
    }
}
