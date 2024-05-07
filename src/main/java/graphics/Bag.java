/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author blake
 */
public class Bag {
    
    public boolean isOpen;
    public Color color;
    public int size, x, y;
    

    public Bag(int x, int y, Color color, int size) {
        this.isOpen = false;
        this.color = color;
        this.size = size;
        this.x = x;
        this.y=y + size/5;
    }
    
    public void open() {
        this.isOpen = true;
    }
    
    public void close() {
        this.isOpen = false;
    }
    
    public void draw(Graphics2D g) {
        
        if (!isOpen) {
            int temp = x;
            int temp2 =y;
            
            // Draw the Handle
            g.setColor(color);
            drawHandle(g);
            
            // Alter the color
            g.setColor(darkenColor(color, 1));
            
            // Draw the body
            g.setColor(color);
            drawBody(g);

            // Darken the color
            g.setColor(darkenColor(color, 3));
            
            // draw the ears
            drawEars(g);
            
            // draw the pouch
            x = temp;
            y = temp2;
            if (size >= 100) {
                g.fillRoundRect(x + size * 3/10, y + size * 2/5, size  * 6/10, size * 3/5, 30, 50);    
            } else {
                g.fillRoundRect(x + size * 3/10, y + size * 2/5, size  * 6/10, size * 3/5, 10, 30);   
            }
            
        
        } else {
            int temp = x;
            int temp2 =y;
            
            // Draw the Handle
            g.setColor(color);
            drawHandle(g);
            
            // Alter the color
            g.setColor(darkenColor(color, 1));
            
            // Draw the body
            g.setColor(color);
            drawBody(g);

            // Darken the color
            g.setColor(darkenColor(color, 3));
            
            // draw the ears
            drawEars(g);
            
            // draw the pouch
            x = temp;
            y = temp2;
            if (size >= 100) {
                g.fillRoundRect(x + size * 3/10, y + size * 2/5, size  * 6/10, size * 3/5, 30, 50);    
            } else {
                g.fillRoundRect(x + size * 3/10, y + size * 2/5, size  * 6/10, size * 3/5, 10, 30);   
            }
            
            // Draw the zipped hole
            y += size * 1/10;
            g.setColor(Color.black);
            g.fillOval(x + size * 3/10, y + size * 2/5, size * 6/10, size * 1/10);
            
            // Draw the button cloth
            x = (x + size * 3/10) + (size * 6/10);
            y = (y + size * 2/5) - 10;
            g.setColor(Color.white);
            g.fillRect(x, y, 400, size * 3/5);
            
            x=temp;
            y=temp2;
            
        }
    }
    
    public void drawHandle(Graphics2D g) {  
        g.fillOval(x, y, size, size);
        g.setColor(Color.black);
        g.fillOval(x + size/10, y + size/10, 8*size/10, 8*size/10);
    }
    
    public void drawBody(Graphics2D g) {
        x += size/5;
        if (size >= 100) {
            g.fillRoundRect(x, y, size * 4/5, size, 50,50);    
        } else {
            g.fillRoundRect(x, y, size * 4/5, size, 30, 30);   
        }
            
        y -= size/5;
        g.fillOval(x, y, size *4/5, size *4/5);
        
    }
    
    public void drawEars(Graphics2D g) {
        y -= size * 1/5;
        x -= size * 1/5;
        g.fillOval(x, y, size * 5/10, size * 5/10);
        x += size * 7/10;
        g.fillOval(x, y, size * 5/10, size * 5/10);
        
    }
    
    public Color darkenColor(Color color , int scale) {
        int r = color.getRed() - (10 * scale);
        int b = color.getBlue() - (10 * scale);
        int green = color.getGreen() - (10 * scale);
        if (r < 0) {
            r = 0;
        }
        if (b < 0) {
            b = 0;
        }
        if (green < 0) {
            green = 0;
        }
        return new Color(r,green,b);
        
    }
}
