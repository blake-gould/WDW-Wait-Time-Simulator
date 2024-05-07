/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;



/**
 *
 * @author blake
 */
public class Flag {
    
    private int x, y, width, height, iter;
    private Color color;
    private ArrayList<Rectangle> rectangles = new ArrayList<>();
    private double count = 0;
    private String text = "", text2 = "";
    private Color textColor = new Color(0,0,0);
    private Font font;
    
    public Flag(int x, int y, int width, int height, Color color) {
        double itercalc = (width/5);
        construct(x, y, width, height, color, (int)itercalc);
        initialize(count);    
    }
    
    public Flag(int x, int y,int width, int height, Color color, String text, Font font, int scale) {
        construct(x, y, width, height, color, text.length());
        this.text = text;
        this.font = font;
        this.width = scale * (width / iter);
        initialize(count);
        
        boolean reddark = color.getRed() < 80;
        boolean greendark = color.getGreen() < 80;
        boolean bluedark = color.getBlue() < 80;
        if ((reddark && greendark) || (reddark && bluedark) || (bluedark && greendark)) {
            textColor = Color.white;
        } else {
            textColor = Color.black;
        }
        
    }
    
    public Flag(int x, int y,int width, int height, Color color, String text, String text2, Font font, int scale) {
        this.text = text;
        this.text2 = text2;
        fixStrings();
        System.out.println(text.length() + " FALG " + text2.length());
        construct(x, y, width, height, color, text.length());
        this.font = font;
        this.width = scale * (width / iter);
        initialize(count);
        
        boolean reddark = color.getRed() < 80;
        boolean greendark = color.getGreen() < 80;
        boolean bluedark = color.getBlue() < 80;
        if ((reddark && greendark) || (reddark && bluedark) || (bluedark && greendark)) {
            textColor = Color.white;
        } else {
            textColor = Color.black;
        }
        
    }
    
    private void fixStrings() {
        while (text.length() > text2.length()) {
            text2 = text2 + " ";
        }
        while (text2.length() > text.length()) {
            text = text + " ";    
        }
    }
    
    private void construct(int x, int y, int width, int height, Color color, int iter) {
        this.x = x;
        this.y = y;
        this.width = width / iter;
        this.height = height;
        this.color = color;
        this.iter = iter;
    }
    
    
    private void initialize(double count) {
        rectangles = new ArrayList<>();
        int range = 100/height;
        if (range < 1) {
            range = 1;
        }
        int x2 = 5;
        for (int i = 0; i < iter; i++) {
            rectangles.add(new Rectangle(x + x2, y + 4 + (int)(Math.cos(Math.toDegrees(i + count)) * 10)/range , width, height));
            x2 += width;
        }
    }
    
    private void fontMatchWidth() {
        while (font.getSize() > width + 2) {
            font = new java.awt.Font(font.getName(), font.getStyle(), font.getSize() - 1);
        }
    }

    
    public void draw(Graphics g) {
        this.count += 0.003;
        initialize(this.count);
        g.setColor(Color.black);
        g.fillOval(x, y, 5, 5);
        g.fillRect(x, y + 4, 5, height * 3);
        g.setColor(color);
        for (Rectangle r : rectangles) {
            g.fillRect((int)r.getX(), (int)r.getY(), (int)r.width, (int)r.height);
        }
        
        if (text != "") {
            fontMatchWidth();

            g.setColor(textColor);
            int next = 0;
            for (Rectangle r : rectangles) {
                g.setFont(font);
                g.drawString(text.charAt(next) + "", (int)(r.getX()) + width/4, (int)(r.getY()) + (r.height / 2));
                next++;
            }
        }
        if (text2 != "" ) {
            g.setColor(textColor);
            int next = 0;
            for (Rectangle r : rectangles) {
                g.setFont(font);
                g.drawString(text2.charAt(next) + "", (int)(r.getX()) + width/4, (int)(r.getY()) + (r.height * 3 / 4));
                next++;
            }
            
        }
        
   
    }
    
}
