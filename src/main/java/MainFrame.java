


import essentials.Park;
import essentials.Attraction;
import other.myImage;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
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

    
    import org.json.JSONArray;
    import org.json.JSONObject;
    import java.nio.file.*;
    import java.awt.Color;
    import java.util.ArrayList;

public class MainFrame extends javax.swing.JFrame {
    public ArrayList<Park> parks = new ArrayList<>();
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        //initComponents();
        run();
    }


    public class AttractionLoader {

        public static ArrayList<Attraction> loadAttractions(String filePath) {
            ArrayList<Attraction> rides = new ArrayList<>();

            try {
                String content = Files.readString(Paths.get(filePath));
                JSONArray array = new JSONArray(content);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    String name = obj.getString("name");
                    int cost = obj.getInt("cost");
                    String audience = obj.getString("audience");
                    double duration = obj.getDouble("duration");
                    int popularity = obj.getInt("popularity");
                    int x = obj.getInt("x");
                    int y = obj.getInt("y");

                    JSONArray colorArray = obj.getJSONArray("color");
                    Color color = new Color(
                        colorArray.getInt(0),
                        colorArray.getInt(1),
                        colorArray.getInt(2)
                    );

                    Attraction a = new Attraction(name, cost, audience, duration, popularity, x, y, color);
                    rides.add(a);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return rides;
        }
    }
    
    public void loadMK() {
        ArrayList<Attraction> rides = AttractionLoader.loadAttractions("C:\\Users\\blake\\Dev\\Github\\WDW-Wait-Time-Simulator\\AttractionJsons\\mk.json");
        
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
    }
    
    public void loadAK() {
        ArrayList<Attraction> rides = AttractionLoader.loadAttractions("C:\\Users\\blake\\Dev\\Github\\WDW-Wait-Time-Simulator\\AttractionJsons\\ak.json");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
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
        ArrayList<Attraction> rides = AttractionLoader.loadAttractions("C:\\Users\\blake\\Dev\\Github\\WDW-Wait-Time-Simulator\\AttractionJsons\\hs.json");

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
    
    public void loadEP() {
        ArrayList<Attraction> rides = AttractionLoader.loadAttractions("C:\\Users\\blake\\Dev\\Github\\WDW-Wait-Time-Simulator\\AttractionJsons\\ec.json");

        
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
