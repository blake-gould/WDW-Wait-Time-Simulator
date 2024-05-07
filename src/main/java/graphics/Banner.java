/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author blake
 */
public class Banner {
    
    public String text;
    public Color color;
    private int width, x, y;
    
    public Banner(String text, Color color, int width) {
        this.text = text;
        this.color = color;
        this.width = width;
        setLocation(0, 0);
        fontMatchWidth();
    }
    
    public Banner(String text, Color color, int width, int x, int y) {
        this(text, color, width);
        setLocation(x, y);
    }
    
    
    
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
    private Font font = (new java.awt.Font("Courier New", 1, 100));;
    public void draw(Graphics g) {
        int height = text.length() * width;
        height += width;
        g.setColor(color);
        g.fillRect(x, y, width, height);
        
        g.setFont(font);
        int tempy = y + width;
        boolean reddark = color.getRed() < 80;
        boolean greendark = color.getGreen() < 80;
        boolean bluedark = color.getBlue() < 80;
        if ((reddark && greendark) || (reddark && bluedark) || (bluedark && greendark)) {
            g.setColor(Color.white);
        } else {
            g.setColor(Color.black);
        }
        for (int i = 0; i < text.length(); i++) {
            g.drawString(text.charAt(i) + "", (int) (x + (0.15 * width)), tempy);
            tempy += width;
        }
        
        
    }
    
    private void fontMatchWidth() {
        while (font.getSize() > width + 2) {
            font = new java.awt.Font(font.getName(), font.getStyle(), font.getSize() - 1);
        }
    }
    
    
}
