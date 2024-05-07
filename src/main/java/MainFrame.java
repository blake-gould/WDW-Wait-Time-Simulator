


import essentials.Park;
import essentials.Attraction;
import other.myImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import mains.Boardmaker;
import mains.Display;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blake
 */
public class MainFrame extends javax.swing.JFrame {
    public ArrayList<Park> parks = new ArrayList<>();
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        //initComponents();
        run();
    }
    
    public void loadMK() {
        ArrayList<Attraction> rides = new ArrayList<>();
        
        // ADD THE rides at (index)
        
        
        //(0)
        Attraction hauntedMansion = new Attraction("Haunted Mansion", 1125, "Adult", 7.5, 15, 250, 160, Color.magenta);
        rides.add(hauntedMansion);
        /*
        //(1)
        Attraction spaceMountain = new Attraction("Space Mountain", 5000, "Young Adult", 2.5, 25);
        spaceMountain.setGraphics(1290, 370, Color.white);
        rides.add(spaceMountain);
        
        //(2)
        Attraction splashMountain = new Attraction("Splash Mountain", 3000, "Young Adult", 11, 20);
        splashMountain.setGraphics(30, 300, Color.BLUE);
        rides.add(splashMountain);
        
        //(3)
        Attraction dumbo = new Attraction("Dumbo", 7500, "Child", 1.5, 5);
        dumbo.setGraphics(1200, 150,  Color.gray);
        rides.add(dumbo);
        
        //(4)
        Attraction mermaid = new Attraction("The Little Mermaid", 1636, "Child", 6, 5);
        mermaid.setGraphics(1010, 130, Color.cyan);
        rides.add(mermaid);
        
        //(5)
        Attraction peopleMover = new Attraction("The People Mover", 782, "Adult", 10, 0);
        peopleMover.setGraphics(1100, 460, Color.gray);
        rides.add(peopleMover);
        
        // (6)
        Attraction pooh = new Attraction("Winnie the Pooh", 4235, "Child", 3, 10);
        pooh.setGraphics(920, 240, Color.yellow);
        rides.add(pooh);
       
        // (7)
        Attraction pirates = new Attraction("Pirates of the Carribean", 1125, "Young Adult", 8.5, 15);
        pirates.setGraphics(270, 500, Color.RED);
        rides.add(pirates);
        
        // (8)
        Attraction bigThunder = new Attraction("Big Thunder Mountain Railroad", 1500, "Young Adult", 3.5, 10);
        bigThunder.setGraphics(50, 180, Color.GRAY);
        rides.add(bigThunder);   
        
        //(9)
        Attraction sevenDwarfs = new Attraction("7 Dwarfs Mine Train", 3000, "Young Adult", 2.5, 25);
        sevenDwarfs.setGraphics(910, 170, Color.green);
        rides.add(sevenDwarfs);
        
        //(10)
        Attraction peterPan = new Attraction("Peter Pan's Flight", 4500, "Adult", 3, 20);
        peterPan.setGraphics(590, 200,Color.green);
        rides.add(peterPan);
        
        //(11)
        Attraction buzzLightYear = new Attraction("Buzz Lightyear's Space Ranger Spin", 4500, "Child", 4, 5);
        buzzLightYear.setGraphics(970, 560, new Color(75,0,130));
        rides.add(buzzLightYear);      
        
        //(12)
        Attraction carrousel = new Attraction("Prince Charming Regal Carrousel", 2553, "Child", 2, 0);
        carrousel.setGraphics(710, 90,Color.white);
        rides.add(carrousel); 
        */
        myImage image = new myImage("map images\\magickingdom.jpg");
        image.minimalize(10);
        image.wash(0.62);
        ArrayList<Point> list = new ArrayList<>();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        list.add(new Point(-827 + (dim.width / 2),264 + (dim.height/2)));
        list.add(new Point(341+ (dim.width / 2), -357+ (dim.height/2)));
        list.add(new Point(241+ (dim.width / 2), -161+ (dim.height/2)));
        list.add(new Point(299+ (dim.width / 2), -61+ (dim.height/2)));
        list.add(new Point(18+ (dim.width / 2), -164+ (dim.height/2)));
        list.add(new Point(-69+ (dim.width / 2),-46+ (dim.height/2)));
        Park mk = new Park("Magic Kingdom", rides, "park drawings saves\\magickingdom.txt", new Color(125, 112, 186), image, list, new Point(935,762), 0);
        parks.add(mk);
        //"Save.txt"
        //"SaveFills.txt"
    }
    
    public void loadAK() {
        ArrayList<Attraction> rides = new ArrayList<>();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // 0
        Attraction flightOfPassage = new Attraction("Avatar - Flight of Passage", 2000, "Young Adult", 5, 0, -631  + (dim.width / 2), 204 + (dim.height/2), new Color(0,128,128));
        rides.add(flightOfPassage);
        
        // 1
        Attraction dinosaur = new Attraction("Dinosaur", 1500, "Young Adult", 3, 0, 367 + (dim.width / 2), 243 + (dim.height/2), new Color(154, 184, 122));
        rides.add(dinosaur);
        
        // 2
        Attraction expeditionEverest = new Attraction("Experdition Everest - Legend of the Forbidden Mountain", 1756.1, "Young Adult", 3, 0, 642 + (dim.width / 2), -233 + (dim.height/2), Color.white);
        rides.add(expeditionEverest);
        
        // 3
        Attraction toughToBeABug = new Attraction("It's Tough to be a Bug", 1395.35, "Child", 8, 0, -239 + (dim.width / 2), -142 + (dim.height/2), new Color(135, 137, 192));
        rides.add(toughToBeABug);
        
        // 4
        Attraction kaliRiverRapids = new Attraction("Kali River Rapids", 1894.74, "Child", 3.5, 0, 237 + (dim.width / 2), -241 + (dim.height/2), new Color(236, 115, 87));
        rides.add(kaliRiverRapids);
        
        // 5
        Attraction kilimanjaroSafari = new Attraction("Kilimanjaro Safaris", 1200, "Adult", 18.5, 0, -661 + (dim.width / 2), -317 + (dim.height/2), new Color(219, 182, 143));
        rides.add(kilimanjaroSafari);
        
        // 6
        Attraction naviRiverJourney = new Attraction("Na'vi River Journey", 1875, "Adult", 5, 0, -491 + (dim.width / 2) , 253 + (dim.height/2), new Color(229, 68, 109));
        rides.add(naviRiverJourney);
        
        // 7
        Attraction triceratopSpin = new Attraction("Triceratop Spin", 3750, "Child", 1.5, 0, 426 + (dim.width / 2), 88 + (dim.height/2), new Color(159, 226, 191));
        rides.add(triceratopSpin);
        
        myImage image = new myImage("map images\\animalkingdom.jpg");
        image.minimalize(10);
        image.wash(0.62);
        ArrayList<Point> list = new ArrayList<>();
        list.add(new Point(-522+ (dim.width / 2),-7+ (dim.height/2)));
        list.add(new Point(-223+ (dim.width / 2),-141+ (dim.height/2)));
        list.add(new Point(425+ (dim.width / 2),-77+ (dim.height/2)));
        list.add(new Point(144+ (dim.width / 2),169+ (dim.height/2)));
        list.add(new Point(-186+ (dim.width / 2), -344+ (dim.height/2)));
        Park ak = new Park("Animal Kingdom", rides, "park drawings saves\\animalkingdom.txt", Color.green, image, list, new Point(745,763), 3);
        parks.add(ak);
    }
    
    public void laodHS() {
    ArrayList<Attraction> rides = csvFile("attraction saves\\Hollywood Studios.csv");
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
    myImage image = new myImage("map images\\hollywoodstudios.jpg");
    image.minimalize(10);
    image.wash(0.62);
    
    ArrayList<Point> list = new ArrayList<>();
    list.add(new Point(-266+ (dim.width / 2), 108+ (dim.height/2)));
    list.add(new Point(-783+ (dim.width / 2), -391+ (dim.height/2)));
    Park hs = new Park("Hollywood Studios", rides, "park drawings saves\\hollywoodstudios.txt", Color.red, image, list, new Point(1509,487), 2);
    parks.add(hs);
    }
    
    public ArrayList<Attraction> csvFile(String file) {
        ArrayList<Attraction> rides = new ArrayList<>();
        try {
            
            Scanner sc = new Scanner(new File(file));  
            sc.useDelimiter(",");   //sets the delimiter pattern  
            sc.nextLine();
            while (sc.hasNext())  //returns a boolean value  
            {  
                String curr = sc.nextLine();
                String[] currs = curr.split(",");
                System.out.println(curr);
                System.out.println(Integer.valueOf(currs[8]) + " " + Integer.valueOf(currs[9]) + " " +  Integer.valueOf(currs[10]));
                Color c = new Color(Integer.valueOf(currs[8]), Integer.valueOf(currs[9]), Integer.valueOf(currs[10]));
                
                Attraction a = new Attraction(currs[1], Double.parseDouble(currs[2]), currs[3], Double.parseDouble(currs[4]), Integer.valueOf(currs[5]), Integer.valueOf(currs[6]), Integer.valueOf(currs[7]), c);
                rides.add(a);
            }   
            sc.close();  //closes the scanner  
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("File Success" + file + rides.size());
        return rides;
    }
    
    public void loadEP() {
        ArrayList<Attraction> rides = csvFile("attraction saves\\Epcot.csv");
        
        myImage image = new myImage("map images\\epcot.jpg");
        image.minimalize(10);
        image.wash(0.62);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        ArrayList<Point> list = new ArrayList<>();
        list.add(new Point(-750+ (dim.width / 2), 217+ (dim.height/2)));
        list.add(new Point(-750+ (dim.width / 2), -341+ (dim.height/2)));
        list.add(new Point(-326+ (dim.width / 2), -35+ (dim.height/2)));
        list.add(new Point(370+ (dim.width / 2), -51+ (dim.height/2)));
        parks.add(new Park("Epcot", rides, "park drawings saves\\epcot.txt", Color.white, image, list, new Point(148,411), 1));
    }
    
    public void run() {
        loadMK();
        loadEP();
        laodHS();
        loadAK();
        Display dp = new Display(parks);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newBoardButton = new javax.swing.JButton();
        runButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        newBoardButton.setText("New Board");
        newBoardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBoardButtonActionPerformed(evt);
            }
        });

        runButton.setText("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(newBoardButton, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                    .addComponent(runButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newBoardButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runButton)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newBoardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBoardButtonActionPerformed
        Boardmaker bm = new Boardmaker();
        bm.setVisible(true);
    }//GEN-LAST:event_newBoardButtonActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        run();
    }//GEN-LAST:event_runButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        MainFrame main = new MainFrame();
        main.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton newBoardButton;
    private javax.swing.JButton runButton;
    // End of variables declaration//GEN-END:variables
}
