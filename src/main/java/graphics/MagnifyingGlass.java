package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author blake
 */
public class MagnifyingGlass {
    private int size;
    private int xPos, yPos;
    private Color color;
    private boolean zoomIn;

    public MagnifyingGlass(boolean zoomIn) {
        this(0, 0, zoomIn);
    }

    public MagnifyingGlass(int xPos, int yPos, boolean zoomIn) {
        this(xPos, yPos, 20, Color.BLACK, zoomIn);
    }

    public MagnifyingGlass(int xPos, int yPos, int size, Color color, boolean zoomIn) {
        this.xPos  = xPos;
        this.yPos  = yPos;
        this.size  = size;
        this.color = color;
        this.zoomIn = zoomIn;
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.white);
        g.fillOval(xPos, yPos, size, size);
        g.setColor(color);
        g.setStroke(new BasicStroke(4));
        // Draw the glass
        g.drawOval(xPos, yPos, size, size);
        g.setStroke(new BasicStroke(6));
        // Draw the handle
        int handleLength = size / 2;
        g.drawLine(xPos + (( 3 * size) / 4), yPos + size, xPos + ((3/2) * size), yPos + (3 * size / 2));
        int innerGap = size / 4;
        g.drawLine(xPos + innerGap, yPos + (size / 2), xPos + size - innerGap, yPos + (size / 2));
        if (this.zoomIn) {
            // Draw the vertical line from the plus sign
            g.drawLine(xPos + (size / 2), yPos + innerGap, xPos + (size / 2), yPos + size - innerGap);
        }
    }

}