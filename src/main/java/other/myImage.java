package other;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.Random;

/**
 *
 * @author blake
 */
public class myImage {
    
    public BufferedImage imageVar;
    public String FileName;
    
    public myImage(String FileName) {
        // Load in file based on String FileName
        File file = null;
        this.FileName = FileName;
        try {
            file = new File(FileName);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("File couldn't load in.");
        }
        System.out.println("File " + file.getAbsolutePath() + " was loaded in.");
        try {
            imageVar  = ImageIO.read(file);     
        } catch (Exception e) { 
            e.printStackTrace();
            System.out.println("Buffered Image was not created");
        }
        System.out.println("Buffered Image was created");
        
        /*
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int i = 0; 
        int j = 0;
        for (i = 0; i < imageVar.getWidth(); i++) {
            for (j = 0; j < imageVar.getHeight(); j++) {
                temp.setRGB(i, j, imageVar.getRGB(i, j));
            }
        }
        while (i < width) {
            while (j < height) {
                temp.setRGB(i, j, 0);
                j++;
            }
            i++;
        }
        
        imageVar = temp;
        */
        imageVar = cropImage(imageVar, 1900, 900);
    }
    
    
    private BufferedImage cropImage(BufferedImage src, int width, int height) {
      BufferedImage dest = src.getSubimage(0, 0, width, height);
      return dest; 
   }
    
    public myImage() {
        imageVar = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < imageVar.getWidth(); x++) {
            for (int y = 0; y < imageVar.getHeight(); y++) {
                Random rand = new Random();
                int red = rand.nextInt(256);
                int green = rand.nextInt(256);
                int blue = rand.nextInt(256);
                int rgb = 65536 * red + 256 * green + blue;
                
                imageVar.setRGB(x, y, rgb);
            }
        }
    }
    
    public void minimalize(int cubic) {
        // This section makes minecraft image
        int currHeight = 0;
        int currWidth = 0; 
        while (currHeight + cubic <= imageVar.getHeight()) {
            while (currWidth + cubic <= imageVar.getWidth()) {
                fillSection(currWidth, currHeight, cubic, cubic);
                currWidth += cubic;
            }
            currWidth = 0;
            currHeight += cubic;
        }
        //end section
        
        // This section makes the colors that are similar the same
        /*ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Integer> blues = new ArrayList<>();
        ArrayList<Integer> greens = new ArrayList<>();
        ArrayList<Integer> reds = new ArrayList<>();
        
        int counter = 0;
        currHeight = 0;
        currWidth = 0;
        while (currHeight + cubic <= imageVar.getHeight()) {
            while (currWidth + cubic <= imageVar.getWidth()) {
                int color = imageVar.getRGB(currWidth, currHeight);
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;
                
                boolean found = false;
                for (int i = 0; i < blues.size(); i++) {
                    //System.out.println(Math.abs(blues.get(i) - blue));
                    if (Math.abs(blues.get(i) - blue) <= 60) {
                    if (Math.abs(greens.get(i) - green) <= 60) {
                    if (Math.abs(reds.get(i) - red) <= 60) {
                        
                        imageVar.setRGB(currWidth, currHeight, colors.get(i));
                        fillSection(currWidth, currHeight, cubic, cubic);
                        found = true;
                        counter++;
                        break;
                        
                    }    
                    }  
                    }
                }
                
                if (!found) {
                    colors.add(color);
                    reds.add(red);
                    blues.add(blue);
                    greens.add(green);
                }
                
                
                
                
                currWidth += cubic;
            }
            currWidth = 0;
            currHeight += cubic;
        }
        
        System.out.println(counter);*/
        
    }
    
    private void fillSection(int x, int y, int width, int height) {
        int color = imageVar.getRGB(x,y);
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                try {
                    imageVar.setRGB(i, j, color);
                } catch(Exception e) {
                    e.printStackTrace();
                    
                }
            }
        }
    }
    
    public void toBlackandWhite() {
        for (int i = 0; i < imageVar.getWidth(); i++) {
            for (int j = 0; j < imageVar.getHeight(); j++) {
                int color = imageVar.getRGB(i, j);
                int blue = color & 0xff;
                int rgb = 65536 * blue + 256 * blue + blue;
                //System.out.println(blue);
                imageVar.setRGB(i, j, rgb);
            }
        }
    }
    
    /*
    This Method takes the image and places a white film overtop of it.
    */
    
    public void wash(double alpha) {
        for (int i = 0; i < imageVar.getHeight(); i++) {
            for (int j = 0; j < imageVar.getWidth(); j++) {
                int color = imageVar.getRGB(j, i);
                int blue = (int)(((color & 0xff) * alpha)  +  256*(1-alpha));
                int green = (int)((((color & 0xff00) >> 8) * alpha) + 256*(1-alpha));
                int red =  (int)((((color & 0xff0000) >> 16) * alpha) + 256*(1-alpha));
                color = 65536 * red + 256 * green + blue;
                imageVar.setRGB(j, i, color);
                
            }
        }
    }
    
    public void toBasicColors() {
        // Blue: (0, 0, 255)
        // Red: (255, 0 , 0)
        // Green (0, 255, 0)
        // Purple (127, 0, 255)
        // Yellow (255, 255, 0)
        // Orange (255, 128, 0)
        // Black (0, 0, 0)
        // White (255, 255, 255)
        
        ArrayList<Integer> reds = new ArrayList<>(
      List.of(0, 255, 0, 127, 255, 255, 0, 255));
        ArrayList<Integer> blues = new ArrayList<>(
      List.of(255, 0, 0, 255, 0, 0, 0, 255));
        ArrayList<Integer> greens = new ArrayList<>(
      List.of(0, 0, 255, 0, 255, 128, 0, 255));

        
        for (int i = 0; i < imageVar.getWidth(); i++) {
            for (int j = 0; j < imageVar.getHeight(); j++) {
                int color = imageVar.getRGB(i, j);
                int min = Integer.MAX_VALUE;
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;
                for (int k = 0; k < reds.size(); k++) {
                    int determinate = Math.abs(reds.get(k) - red) + Math.abs(greens.get(k) - green) + Math.abs(blues.get(k) - blue);
                    if (determinate < min) {
                        min = determinate;
                        color = 65536 * reds.get(k) + 256 * greens.get(k) + blues.get(k);
                    }
                }
                imageVar.setRGB(i, j, color);
            }
        }
    }
    
    public void shrink(int cubic) {
        int currHeight = 0;
        int currWidth = 0; 
        while (currHeight + cubic < imageVar.getHeight()) {
            while (currWidth + cubic < imageVar.getWidth()) {
                fillSection(currWidth, currHeight, cubic, cubic);
                currWidth += cubic;
            }
            currWidth = 0;
            currHeight += cubic;
        }
        
        
        
        BufferedImage bimg = new BufferedImage(imageVar.getWidth() / cubic, imageVar.getHeight() / cubic, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < bimg.getWidth(); x++) {
            for (int y = 0; y < bimg.getHeight(); y++) {
                bimg.setRGB(x, y, imageVar.getRGB(x * cubic, y * cubic));
            }
        }
        
        this.imageVar = bimg;
    }
    
    public void reflectVertically() {
        int halfWidth = imageVar.getWidth() / 2;
        
        for (int j = 0; j < imageVar.getHeight(); j++) {
            int counter = 1;
            for (int i = halfWidth; i < imageVar.getWidth(); i++) {
                imageVar.setRGB(i, j, imageVar.getRGB(i - counter, j));
                counter += 2;
            }    
        }
    }
    
    public void reflectHorizontally() {
        int halfHeight = imageVar.getHeight() / 2;
        
        for (int j = 0; j < imageVar.getWidth(); j++) {
            int counter = 1;
            for (int i = halfHeight; i < imageVar.getHeight(); i++) {
                imageVar.setRGB(j, i, imageVar.getRGB(j , i - counter));
                counter += 2;
            }    
        }    
    }
    
    public void negate(){
        for (int i = 0; i < imageVar.getHeight(); i++) {
            for (int j = 0; j < imageVar.getWidth(); j++) {
                int color = imageVar.getRGB(j, i);
                int blue = 255 - (color & 0xff);
                int green = 255 - ((color & 0xff00) >> 8);
                int red =  255 - ((color & 0xff0000) >> 16);
                color = 65536 * red + 256 * green + blue;
                imageVar.setRGB(j, i, color);
                
            }
        }
    }
    
    public void darken() {
        for (int i = 0; i < imageVar.getHeight(); i++) {
            for (int j = 0; j < imageVar.getWidth(); j++) {
                int color = imageVar.getRGB(j, i);
                int blue = (color & 0xff) / 2;
                int green = ((color & 0xff00) >> 8) / 2;
                int red =  ((color & 0xff0000) >> 16) / 2;
                color = 65536 * red + 256 * green + blue;
                imageVar.setRGB(j, i, color);
                
            }
        }
    }
    
    
    
    public String toString() {
        return FileName;
    }
    
    public void combineColors(myImage img2) {
        
        BufferedImage bi2 = img2.imageVar;
        if (bi2.getWidth() > imageVar.getWidth()) {
            bi2 = cropImage(bi2, imageVar.getWidth(), bi2.getHeight());
        } else {
            imageVar = cropImage(imageVar, bi2.getWidth(), imageVar.getHeight());
        }
        
        if (bi2.getHeight() > imageVar.getHeight()) {
            bi2 = cropImage(bi2, bi2.getWidth(), imageVar.getHeight());
        } else {
            imageVar = cropImage(imageVar, imageVar.getWidth(), bi2.getHeight());
        }
        
        
        
        for (int i = 0; i < imageVar.getHeight(); i++) {
            for (int j = 0; j < imageVar.getWidth(); j++) {
                int color = imageVar.getRGB(j, i);
                int color2 = bi2.getRGB(j, i);
                int blue = (color & 0xff);
                int green = ((color & 0xff00) >> 8);
                int red =  ((color & 0xff0000) >> 16);
                int blue2 = (color2 & 0xff);
                int green2 = ((color2 & 0xff00) >> 8);
                int red2 =  ((color2 & 0xff0000) >> 16);
                int blue3 = (blue + blue2) / 2;
                int red3 = (red + red2) / 2;
                int green3 = (green + green2) / 2;
                color = 65536 * red3 + 256 * green3 + blue3;
                imageVar.setRGB(j, i, color);
                
            }
        }
        
    }

    public void smoothColors() {
        for (int i = 0; i < imageVar.getWidth(); i++) {
            for (int j = 0; j < imageVar.getHeight(); j++) {
                int color = imageVar.getRGB(i, j);
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;
                while (blue % 30 != 0) {
                    blue--;
                }
                while (red % 30 != 0) {
                    red--;
                }
                while (green % 30 != 0) {
                    green--;
                }
                
                color = 65536 * red + 256 * green + blue;
                imageVar.setRGB(i, j, color);
            }
        }
    }
    
    public void createShadow() {
        for (int i = 0; i < imageVar.getWidth(); i++) {
            for (int j = 0; j < imageVar.getHeight(); j++) {
                int color = imageVar.getRGB(i, j);
                //System.out.println(color);
                if (color != -1) {
                    color = 0;
                }
                
                imageVar.setRGB(i, j, color);
            }
        }    
    }
}
