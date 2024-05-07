package mains;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import essentials.Park;
import essentials.Family;
import essentials.Attraction;
import graphics.Bag;
import graphics.Banner;
import graphics.Flag;
import other.myImage;
import timers.Task;
import timers.parkTimer;
import timers.parkTimerTask;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author blake
 */
public class Display extends javax.swing.JFrame {
    
    public Park curr = null;
    public ArrayList<Park> parks = new ArrayList<>();
    public int currnum;
    public boolean hover;
    public boolean hubgraphics;
    
    public boolean[][] grid;
    private ArrayList<Family> hubFamilys = new ArrayList<>();
    public parkTimer tmr;
    
    public Point spawn ;
    public Point mkentrance = new Point();
    public Point akentrance = new Point();
    public Point hsentrance = new Point();
    public Point epentrance = new Point();
    public myImage image = new myImage("map images\\map.jpg");
    
    public Flag mkflag;
    public Flag akflag;
    public Flag hsflag;
    public Flag epflag;
    public Flag mkflagback;
    public Flag akflagback;
    public Flag hsflagback;
    public Flag epflagback;
    public boolean flags = true;
    
    public int loc;
    
    public ArrayList<Family> hubFamilysToAdd = new ArrayList<>();
    
    
    
    /**
     * Creates new form Display
     */
    public Display(ArrayList<Park> parks) {
        initComponents();
        this.parks = parks;
        for (Park p: parks) {
            p.setD(this);
        }
        this.curr = null;
        this.currnum = 4;
        this.add(panel);
        panel.setVisible(true);
        this.setVisible(true);
        addKeyListener(listener);
        setFocusable(true);
        hubgraphics = true;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mkflag = new Flag(dim.width / 2 - 100, (dim.height / 6) - 130, 120, 80, Color.blue, "MAGIC  ",  "KINGDOM", new java.awt.Font("Courier New", 1, 25), 1);
        akflag = new Flag(dim.width / 6, dim.height / 2 - 100 - 130, 120, 80, Color.green, "ANIMAL ", "KINGDOM", new java.awt.Font("Courier New", 1, 25), 1);
        hsflag = new Flag(dim.width/2 - 100, (dim.height - 200) - dim.height / 6 - 130, 120, 80, Color.red, "HOLLYWOOD", "STUDIOS", new java.awt.Font("Courier New", 1, 25), 1);
        epflag = new Flag((dim.width)-200 - (dim.width/6), (dim.height/2 - 100) - 130, 120 ,80, Color.yellow, "EPCOT",new java.awt.Font("Courier New", 1, 25), 1);
        
        
//        g.setColor(Color.red);
//            g.fillOval(size.width / 2 - 100, (size.height - 200) - size.height / 6, 200, 200);
//            g.setColor(Color.green);
//            g.fillOval(size.width / 6, size.height / 2 - 100, 200, 200);
//            g.setColor(Color.yellow);
//            g.fillOval(((size.width) - 200) - size.width / 6, size.height / 2 - 100, 200, 200);
        spawn = new Point(dim.width / 2 - 5, dim.height / 2 - 5);
        mkentrance = new Point((dim.width / 2) - 5, (dim.height / 2) - 120);
        epentrance = new Point((dim.width /2)  + 360, (dim.height / 2));
        hsentrance = new Point((dim.width/2) - 5, (dim.height/2) + 110);
        akentrance = new Point((dim.width/2) - 370, (dim.height/2) - 5);
        
        while (mkentrance.x % 10 != 0) {
            mkentrance.x--;
        }
        while (epentrance.x % 10 != 0) {
            epentrance.x--;
        }
        while (hsentrance.x % 10 != 0) {
            hsentrance.x--;
        }
        while (akentrance.x % 10 != 0) {
            akentrance.x--;
        }
        
        while (mkentrance.y % 10 != 0) {
            mkentrance.y--;
        }
        while (epentrance.y % 10 != 0) {
            epentrance.y--;
        }
        while (hsentrance.y % 10 != 0) {
            hsentrance.y--;
        }
        while (akentrance.y % 10 != 0) {
            akentrance.y--;
        }
        
        
        
        ArrayList<parkTimerTask> tasks = new ArrayList<>();
        
        Runnable Familymove = new Runnable() {
            @Override
            public void run() {
                Familymove();
            }
        };
        Task t = new Task(Familymove);
        parkTimerTask task = new parkTimerTask(t, 0, 50, Familymove);
        tasks.add(task);
        
        Runnable painter = new Runnable() {
            @Override
            public void run() {
                panel.repaint();
            }
        };
        Task painterTsk = new Task(painter);
        parkTimerTask parkPainterTsk = new parkTimerTask(painterTsk, 3000, 20, painter);
        tasks.add(parkPainterTsk);
        
        tmr = new parkTimer(1, tasks);
        tmr.start();
        
        createButtonsAndPanels();
        
        image.minimalize(5);
        image.wash(0.82);
        
        
        
    }
    
    public void createButtonsAndPanels() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
           
        subpanel1.setVisible(false);
        subpanel2.setVisible(false);
        subpanel3.setVisible(false);
        subpanel4.setVisible(false);
        
        int y = 70;
        int x = 130;
        
        button1 = new JButton(); //Tracking
        panel.add(button1);
        button1.setBounds(x, y, 20, 20);
        button1.setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 subpanel1.setVisible(!subpanel1.isVisible());
                 
            }
        });
        
        button2 = new JButton();
        panel.add(button2);
        button2.setBounds(x + 50, y, 20, 20);
        button2.setVisible(true);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 subpanel2.setVisible(!subpanel2.isVisible());
                 
            }
        });
        
        button3 = new JButton();
        panel.add(button3);
        button3.setBounds(x + 100, y, 20, 20);
        button3.setVisible(true);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 subpanel3.setVisible(!subpanel3.isVisible());
                 
            }
        });
        
        button4 = new JButton();
        panel.add(button4);
        button4.setBounds(x + 150, y, 20, 20);
        button4.setVisible(true);
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 subpanel4.setVisible(!subpanel4.isVisible());
                 
            }
        });
        
        button1.setBackground(Color.black);
        button2.setBackground(Color.black);
        button3.setBackground(Color.black);
        button4.setBackground(Color.black);
        
        button1.setOpaque(true);
        button2.setOpaque(true);
        button3.setOpaque(true);
        button4.setOpaque(true);
        
        JButton button5 = new JButton("1");
        button5.setText("1x");
        JButton button6 = new JButton("5");
        button6.setText("5x");
        JButton button7 = new JButton("15");
        button7.setText("15x");
        JButton button8 = new JButton("30");
        button8.setText("30x");
        buttonT = new JButton();
        
        subpanel5.add(button5);
        subpanel5.add(button6);
        subpanel5.add(button7);
        subpanel5.add(button8);
        panel.add(buttonT);
        
        button5.setVisible(true);
        button6.setVisible(true);
        button7.setVisible(true);
        button8.setVisible(true);
        buttonT.setVisible(true);
        
        buttonT.setBounds(x + 200, y, 20, 20);
        buttonT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subpanel5.setVisible(!subpanel5.isVisible());
            }
        });
        
        button5.setBounds(20, 44, 60, 20);
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Park p: parks) {
                    p.tmr = p.tmr.changeTimer(1);    
                }
                tmr = tmr.changeTimer(1);
                setClocks();
                
            }
        });
        
        button6.setBounds(20, 108, 60, 20);
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Park p: parks) {
                    p.tmr = p.tmr.changeTimer(5);    
                }
                tmr = tmr.changeTimer(5);
                setClocks();
            }
        });
        
        button7.setBounds(20, 172, 60, 20);
        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Park p: parks) {
                    p.tmr = p.tmr.changeTimer(15);    
                }
                tmr = tmr.changeTimer(15);
                setClocks();
            }
        });
        
        button8.setBounds(20, 236, 60, 20);
        button8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Park p: parks) {
                    p.tmr = p.tmr.changeTimer(30);    
                }
                tmr = tmr.changeTimer(30);
                setClocks();
            }
        });
        
        subpanel5.setVisible(false);
        JToggleButton button9 = new JToggleButton();
        button9.addActionListener((e) -> {
            if (button9.isSelected()) {
                for (Park p : parks) {
                    p.tmr.pause();
                }
                tmr.pause();
                button5.setVisible(false);
                button6.setVisible(false);
                button7.setVisible(false);
                button8.setVisible(false);
            } else {
                for (Park p : parks) {
                    p.tmr.resume();
                }
                tmr.resume();
                button5.setVisible(true);
                button6.setVisible(true);
                button7.setVisible(true);
                button8.setVisible(true);
            }
        });
        button9.setVisible(true);
        subpanel5.add(button9);
        button9.setBounds(30, 300, 40, 20 );
        
    }
    
    public void setClocks() {
        Park model = parks.get(0);
        for (int i = 0; i < parks.size(); i++) {
            parks.get(i).dailySecondsSurpassed = model.dailySecondsSurpassed;
        }
    }
    
    
    public JButton button1;
    public JButton button2;
    public JButton button3;
    public JButton button4;
    public JButton buttonT;
    
    
    public void Familymove() {
        
        int z = attractionsList.getSelectedIndex();
        if (z != -1) {
            Attraction a = curr.rides.get(z);
            info1.setText(a.name);
            info2.setText(a.line.size() + "");
            info3.setText(a.fline.size() + "");
            info4.setText(a.ride.size() + "");
            info5.setText(a.outFlowTime / 13 + "");
            info6.setText(a.expTime + "");
            info7.setText(a.targetAudience);
            info8.setText(a.priority + "");
            info9.setBackground(curr.rides.get(attractionsList.getSelectedIndex()).color);    
        }
        for (Family f: hubFamilysToAdd) {
            hubFamilys.add(f);
        }
        
        hubFamilysToAdd.clear();
        
        ArrayList<Family> Familystoremove = new ArrayList<>();

        if (hubFamilys.size() > 0) {
            flags = false;
        } else {
            flags = true;
        }
        
        for (Family g: hubFamilys) {
            if (g.transferred) {
                if (g.oldPark == 0) {
                    g.setLocation(mkentrance);
                } else
                if (g.oldPark == 1) {
                    g.setLocation(epentrance);
                } else
                if (g.oldPark == 2) {
                    g.setLocation(hsentrance);
                } else
                if (g.oldPark == 3) {
                    g.setLocation(akentrance);
                }
                
                determinePark(g);
                g.transferred = false;
            }
        }
        
        for (Family g: hubFamilys) {
            if (g.location.x == g.destination.x && g.location.y == g.destination.y) {
                Familystoremove.add(g);
                Park p = g.parkDestination;
                p.familysToAdd.add(g);
                g.setLocation(p.parkExitPoint);
                g.transferred = true;
                g.attractionDestination = p.determineDestination(g);
                g.destination.x = g.attractionDestination.x;
                g.destination.y = g.attractionDestination.y;
                
            }
        }
        
        for (Family g: Familystoremove) {
            hubFamilys.remove(g);
        }
        
        Familystoremove.clear();
        
        //System.out.println(hubFamilys.size());
        
        for (Family g : hubFamilys) {
                // This indicates if g has moved or not this passTime()
                boolean moved = false;

                // Up
                if (g.location.getY() > g.destination.getY()) {
                    int x = (int)g.location.getX();
                    int y = (int)g.location.getY() - 1;
                    if (x > 0 && y > 0) {
                            g.setLocation(x, y);
                            moved = true;    
                    }
                }

                // Down
                if (g.location.getY() < g.destination.getY() && !moved) {
                    int x = (int)g.location.getX();
                    int y = (int)g.location.getY() + 1;
                    if (x > 0 && y > 0) {
                            g.setLocation(x, y);
                            moved = true;  
                    }
                }

                // Left
                if (g.location.getX() > g.destination.getX() && !moved) {
                    int x = (int)g.location.getX() - 1;
                    int y = (int)g.location.getY();
                    if (x > 0 && y > 0) {
                            g.setLocation(x, y);
                            moved = true;
                    }
                } 

                // Right
                if (g.location.getX() < g.destination.getX() && !moved) {
                    int x = (int)g.location.getX() + 1;
                    int y = (int)g.location.getY();
                    if (x > 0 && y > 0) {
                            g.setLocation(x, y);
                    }
                }   
        }
    }
    
    public void determinePark(Family g) {
        int minexaust = Integer.MAX_VALUE;
        Park choice = null;
        for (Park p: parks) {
            int pexaust = p.getParkExaust(g);
            if (pexaust < minexaust && g.oldP != p) {
                choice = p;
                minexaust= pexaust;
            }
        }
        if (choice.equals(parks.get(0))) {
            g.destination.x = mkentrance.x;
            g.destination.y = mkentrance.y;
            g.parkDestination = parks.get(0);
        }
        if (choice.equals(parks.get(1))) {
            g.destination.x = epentrance.x;
            g.destination.y = epentrance.y;
            g.parkDestination = parks.get(1);
        }
        if (choice.equals(parks.get(2))) {
            g.destination.x = hsentrance.x;
            g.destination.y = hsentrance.y;
            g.parkDestination = parks.get(2);
        }
        if (choice.equals(parks.get(3))) {
            g.destination.x = akentrance.x;
            g.destination.y = akentrance.y;
            g.parkDestination = parks.get(3);
        }
        
    }
    
    
    
    public void initializeHub() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = ((2 * size.width) / 3) - 400;
        int height = ((2 * size.height) / 3) - 400;
        int xlimit = width / 10;
        int ylimit = height / 10;
        grid = new boolean[xlimit][ylimit];
        for (int i = 0; i < xlimit; i++) {
            for (int j = 0; j < ylimit; j++) {
                grid[i][j] = false;
            }
        }
    }
    
    
    
    
    public KeyListener listener = new KeyListener() {

        @Override
        public void keyReleased(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_F3) {
                // What you do when F3 key pressed
            } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
                // What you do when F2 key pressed
            } 
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // Do nothing
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println(".keyPressed()");
            int keyCode = e.getKeyCode();
        //System.out.println("keyPressed: "+e);
        switch( keyCode ) { 
            case KeyEvent.VK_UP:
                // handle up 
                break;
            case KeyEvent.VK_DOWN:
                // handle down 
                break;
            case KeyEvent.VK_LEFT:
                parkhopleft();
                
                break;
            case KeyEvent.VK_RIGHT :
                parkhopright();
                
                break;
         }
        }
    };
    

    
    public void parkhopright() {
        currnum++;
        if (currnum >= parks.size()) {
            currnum = 0;
        }
        curr = parks.get(currnum);
        curr.dp = panel;
        panel.repaint();
        
    }
    
    
    public void parkhopleft() {
        currnum--;
        if (currnum < 0) {
            currnum = parks.size() - 1;
        }
        curr = parks.get(currnum);
        curr.dp = panel;
        panel.repaint();
        
    }
    
public ArrayList<JToggleButton> buttons = new ArrayList<>();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new dpanel();
        paknam = new javax.swing.JLabel();
        subpanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        attractionsList = new javax.swing.JList<>();
        info2 = new javax.swing.JLabel();
        info1 = new javax.swing.JLabel();
        info8 = new javax.swing.JLabel();
        info5 = new javax.swing.JLabel();
        info6 = new javax.swing.JLabel();
        info7 = new javax.swing.JLabel();
        info9 = new javax.swing.JLabel();
        info3 = new javax.swing.JLabel();
        info4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        subpanel3 = new javax.swing.JPanel();
        subpanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        trackingList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        FamilyInfoList = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        fastpassList = new javax.swing.JTextArea();
        subpanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        subpanel5 = new javax.swing.JPanel();
        timeLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel.setPreferredSize(new java.awt.Dimension(1900, 900));

        paknam.setFont(new java.awt.Font("Blackadder ITC", 1, 48)); // NOI18N
        paknam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        subpanel2.setBackground(new java.awt.Color(0, 0, 0));
        subpanel2.setForeground(new java.awt.Color(255, 255, 255));
        subpanel2.setPreferredSize(new java.awt.Dimension(190, 550));
        subpanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        attractionsList.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        attractionsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        attractionsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                attractionsListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(attractionsList);

        subpanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 11, 244, 590));

        info2.setBackground(new java.awt.Color(153, 0, 153));
        info2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info2.setForeground(new java.awt.Color(255, 255, 255));
        info2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info2.setText("jLabel3");
        info2.setOpaque(true);
        subpanel2.add(info2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 138, 42));

        info1.setBackground(new java.awt.Color(153, 0, 153));
        info1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info1.setForeground(new java.awt.Color(255, 255, 255));
        info1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info1.setOpaque(true);
        subpanel2.add(info1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 138, 80));

        info8.setBackground(new java.awt.Color(153, 0, 153));
        info8.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info8.setForeground(new java.awt.Color(255, 255, 255));
        info8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info8.setText("jLabel3");
        info8.setOpaque(true);
        subpanel2.add(info8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 138, 42));

        info5.setBackground(new java.awt.Color(153, 0, 153));
        info5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info5.setForeground(new java.awt.Color(255, 255, 255));
        info5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info5.setText("jLabel3");
        info5.setOpaque(true);
        subpanel2.add(info5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 138, 42));

        info6.setBackground(new java.awt.Color(153, 0, 153));
        info6.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info6.setForeground(new java.awt.Color(255, 255, 255));
        info6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info6.setText("jLabel3");
        info6.setOpaque(true);
        subpanel2.add(info6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 138, 42));

        info7.setBackground(new java.awt.Color(153, 0, 153));
        info7.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info7.setForeground(new java.awt.Color(255, 255, 255));
        info7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info7.setText("jLabel3");
        info7.setOpaque(true);
        subpanel2.add(info7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 138, 42));

        info9.setBackground(new java.awt.Color(153, 0, 153));
        info9.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info9.setForeground(new java.awt.Color(255, 255, 255));
        info9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info9.setOpaque(true);
        subpanel2.add(info9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 390, 40));

        info3.setBackground(new java.awt.Color(153, 0, 153));
        info3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info3.setForeground(new java.awt.Color(255, 255, 255));
        info3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info3.setText("jLabel3");
        info3.setOpaque(true);
        subpanel2.add(info3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 138, 41));

        info4.setBackground(new java.awt.Color(153, 0, 153));
        info4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        info4.setForeground(new java.awt.Color(255, 255, 255));
        info4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info4.setOpaque(true);
        subpanel2.add(info4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 138, 41));
        subpanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 140, -1));

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Audience Priority");
        jLabel4.setOpaque(true);
        subpanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 140, -1));

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Line Size");
        jLabel5.setOpaque(true);
        subpanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 140, -1));

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("FastPass Line Size");
        jLabel6.setOpaque(true);
        subpanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 140, -1));

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Families on the Ride");
        jLabel7.setOpaque(true);
        subpanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 140, -1));

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("OutFlow (Guests/Second)");
        jLabel8.setOpaque(true);
        subpanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 140, -1));

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Experience Time (min)");
        jLabel9.setOpaque(true);
        subpanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 140, -1));

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Target Audience");
        jLabel10.setOpaque(true);
        subpanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 140, -1));

        subpanel3.setBackground(new java.awt.Color(51, 51, 51));
        subpanel3.setPreferredSize(new java.awt.Dimension(300, 500));

        javax.swing.GroupLayout subpanel3Layout = new javax.swing.GroupLayout(subpanel3);
        subpanel3.setLayout(subpanel3Layout);
        subpanel3Layout.setHorizontalGroup(
            subpanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 409, Short.MAX_VALUE)
        );
        subpanel3Layout.setVerticalGroup(
            subpanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );

        subpanel1.setBackground(new java.awt.Color(51, 51, 51));
        subpanel1.setPreferredSize(new java.awt.Dimension(600, 190));

        trackingList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(trackingList);

        jLabel1.setBackground(new java.awt.Color(153, 0, 153));
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tracking:");
        jLabel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);

        FamilyInfoList.setForeground(new java.awt.Color(255, 255, 255));

        fastpassList.setColumns(20);
        fastpassList.setRows(5);
        jScrollPane3.setViewportView(fastpassList);

        javax.swing.GroupLayout subpanel1Layout = new javax.swing.GroupLayout(subpanel1);
        subpanel1.setLayout(subpanel1Layout);
        subpanel1Layout.setHorizontalGroup(
            subpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(subpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                    .addComponent(FamilyInfoList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        subpanel1Layout.setVerticalGroup(
            subpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(subpanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(FamilyInfoList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                .addContainerGap())
        );

        subpanel4.setBackground(new java.awt.Color(51, 51, 51));
        subpanel4.setPreferredSize(new java.awt.Dimension(850, 190));

        jLabel2.setBackground(new java.awt.Color(153, 0, 153));
        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Toggle Closures");
        jLabel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);

        javax.swing.GroupLayout subpanel4Layout = new javax.swing.GroupLayout(subpanel4);
        subpanel4.setLayout(subpanel4Layout);
        subpanel4Layout.setHorizontalGroup(
            subpanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(564, Short.MAX_VALUE))
        );
        subpanel4Layout.setVerticalGroup(
            subpanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subpanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        subpanel5.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout subpanel5Layout = new javax.swing.GroupLayout(subpanel5);
        subpanel5.setLayout(subpanel5Layout);
        subpanel5Layout.setHorizontalGroup(
            subpanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        subpanel5Layout.setVerticalGroup(
            subpanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        timeLabel.setBackground(new java.awt.Color(102, 0, 102));
        timeLabel.setFont(new java.awt.Font("SansSerif", 0, 36)); // NOI18N
        timeLabel.setForeground(new java.awt.Color(255, 255, 255));
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeLabel.setText("00:00:00");
        timeLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        timeLabel.setOpaque(true);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(641, 641, 641)
                .addComponent(paknam, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(subpanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subpanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subpanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 836, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subpanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 836, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(subpanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(282, 282, 282))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paknam, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(subpanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(subpanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subpanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(subpanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(103, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(subpanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(182, 182, 182))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void attractionsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_attractionsListValueChanged
       int x = attractionsList.getSelectedIndex();
       if (x != -1) {
        Attraction a = curr.rides.get(x);
        info1.setText(a.name);
        info2.setText(a.line.size() + "");
        info3.setText(a.fline.size() + "");
        info4.setText(a.ride.size() + "");
        info5.setText(a.outFlowTime / 13 + "");
        info6.setText(a.expTime + "");
        info7.setText(a.targetAudience);
        info8.setText(a.priority + "");
        info9.setBackground(curr.rides.get(attractionsList.getSelectedIndex()).color);    
       }
       
    }//GEN-LAST:event_attractionsListValueChanged

    /**
     * @param args the command line arguments
     */
    class dpanel extends JPanel {
        
        public ArrayList<JLabel> label1s = new ArrayList<>();
        public ArrayList<JLabel> label2s = new ArrayList<>();
        public int[][] colors;
        public ArrayList<Color> backColors = new ArrayList<>();
        
        public dpanel() {
            addMouseListener(new MouseAdapter() { 
                public void mousePressed(MouseEvent me) { 
                    if (curr != null && me.getX() > 70 && me.getX() < 120 && me.getY() > 40 && me.getY() < 90) {
                        bgg.isOpen = !bgg.isOpen;
                        
                    }
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    if (hover && !hubgraphics) {
                        hubgraphics = true;
                        currnum = 4;
                        
                        curr = null;
                        
                        
                    }
                    else if (hover && hubgraphics) {
                        if (me.getX() < 100) {
                            currnum = 3;

                            

                        } else if (me.getX() > dim.width - 100) {
                            currnum = 1;
                        

                        } else if (me.getY() < 100) {
                            currnum = 0;
               

                        }  else if (me.getY() > dim.height - 100) {
                            currnum = 2;
                        }                         
                        hubgraphics = false;
                        curr = parks.get(currnum);
                        curr.dp = panel;
                        adjustButtons();
                    }
                    
                    if (curr != null) {
                        int xPress = me.getX();
                        int yPress = me.getY();
                        

                        
                        for (Family g : curr.Familys) {
                            if (Math.abs(g.location.getX() - xPress) < 6 && Math.abs(g.location.getY() - yPress) < 6) {

                                for (Family x : curr.Familys) {
                                    x.tracking = false;
                                }
                                for (Attraction a: curr.rides) {
                                    for (Family x: a.line) {
                                        x.tracking = false;
                                    }
                                    for (Family x: a.ride) {
                                        x.tracking = false;
                                    }
                                    for (Family x: a.fline) {
                                        x.tracking = false;
                                    }
                                }
                                g.tracking = true;
                                curr.ageLabel.setText("The age group of the subject is " + g.age);
                                curr.trackingModel = new DefaultListModel();

                                
                            }
                        }    
                    }
                    
                    System.out.println(me.getX() + "," + me.getY());
                    
                    
                    
                }    
            });
            
        }
        
        public void adjustButtons() {
            int x = 4;
            int y = 50;
            for (JToggleButton b: buttons) {
                                subpanel4.remove(b);
                        }
                        buttons = new ArrayList<>();
                        for (Attraction a : curr.rides) {
                            JToggleButton closedButton = new JToggleButton("X");
                            closedButton.setSelected(false);
                            if (a.closed) {
                                closedButton.setSelected(true);
                            }
                            closedButton.addActionListener((e) -> {
                                if (closedButton.isSelected()) {
                                    a.closed = true;
                                } else {
                                    a.closed = false;
                                }
                            });
                            closedButton.setText(a.name);
                            buttons.add(closedButton);
                            subpanel4.add(closedButton);
                            closedButton.setVisible(true);
                            closedButton.setBounds(x, y, 200, 20);
                            x+=204;
                            
                            if (x > 900 - 200) {
                                x = 4;
                                y+=24;
                            }
                        }
            // Initialize Attractions Statistics   
            info1.setText(curr.rides.get(0).name);
            info2.setText((curr.rides.get(0).line.size() + ""));
            info3.setText(curr.rides.get(0).fline.size() + "");
            info4.setText(curr.rides.get(0).ride.size() + "");
            info5.setText(curr.rides.get(0).outFlowTime / 13 + "");
            info6.setText(curr.rides.get(0).expTime + "");
            info7.setText(curr.rides.get(0).targetAudience);
            info8.setText(curr.rides.get(0).priority + "");
            info9.setBackground(curr.rides.get(0).color);
            
            // Create Wait Time Labels 
            // 20 - 50 - 20, 20 - 50 - 20
            //           20
            // 30 - 35 - 30, 30 - 35 - 30
            //           20
            // 20 - 50 - 20, 20 - 50 - 20
            //           20
            
            x = 20;
            y = 10;
            int x2 = 30;
            int y2= 40;
            
            for (Attraction a : curr.rides) {
                JLabel label = new JLabel(a.name);
                JLabel label2 = new JLabel(a.getWaitTime() + "");
                subpanel3.add(label);
                subpanel3.add(label2);
                label.setBounds(x, y, 80, 20);
                label2.setBounds(x2, y2, 50, 50);
                x += 120;
                //y += 70;
                x2 += 120;
                if (x + label.getWidth()> 350) {
                    x = 20;
                    y+= 110;
                }
                if (x2 + label2.getWidth()> 350) {
                    x2 = 30;
                    y2 += 110;
                }
                label.setVisible(true);
                label2.setVisible(true);
                label.setBackground(a.color);
                label2.setBackground(Color.white);
                label.setOpaque(true);
                label2.setOpaque(true);
                Font font = new java.awt.Font("SansSerif", 0, 10);
                label.setFont(font);
                label1s.add(label);
                label2s.add(label2);
                label.setForeground(a.textColor);
                label2.setFont(new java.awt.Font("SansSerif", 0, 20));
                label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                
                
            }
        }
        
        public void initializeBG(Graphics g) {
            g.setColor(Color.white);
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            g.fillRect(0, 0, size.width, size.height); 
            
            if (curr == null) {
                g.drawImage(image.imageVar, 0, 0, null);    
            }
        }
        
        public void setLabelTexts() {           
            timeLabel.setText(parks.get(0).shownTimeLabel.getText());
            
            if (curr != null) {
                attractionsList.setModel(curr.model);
                paknam.setText(curr.name);
                trackingList.setModel(curr.trackingModel);
                fastpassList.setText(curr.fastpassTA.getText());
                FamilyInfoList.setText(curr.ageLabel.getText());
                timeLabel.setText(curr.shownTimeLabel.getText());
                if (attractionsList.getSelectedIndex() != -1) {
                    attractionsList.setSelectedIndex(attractionsList.getSelectedIndex());
                }
                for (int i = 0; i < curr.rides.size(); i++) {
                    label1s.get(i).setText(curr.rides.get(i).name);
                    label2s.get(i).setText(curr.rides.get(i).getWaitTime() + "");
                }
            } else {
                paknam.setText("");     
            }
             
        }
        
        @Override
        public void paintComponent(Graphics g) throws ConcurrentModificationException{
            super.paintComponent(g);
            initializeBG(g);
            setLabelTexts();
            
            if (!hubgraphics) {
                drawParkGraphics(g);
            }
             else {
                drawHubGraphics(g);
            }
            
            drawHover(g);
            
            
           
          /* g.setColor(Color.white);
           switch (currnum) {
                case 0: g.fillOval(0, size.height - 50, size.width, 100);  break; 
                case 1: g.fillOval(-50, 0, 100, size.height); break;
                case 2: g.fillOval(0, -50, size.width, 100); break;
                case 3: g.fillOval(size.width - 50, 0, 100, size.height);break;
                
            }*/
            
        
                       
         

        }
        
        public void setSunColors() {
            // sunrise
            colors[0][0] = 181;
            colors[0][1] = 214;
            colors[0][2] = 224;
            
            // Morning sun
            colors[1][0] = 255;
            colors[1][1] = 239;
            colors[1][2] = 122; 
            
            // noon sun
            colors[2][0] = 247;
            colors[2][1] = 193;
            colors[2][2] = 106;
            
            // Evening sun
            colors[3][0] = 255;
            colors[3][1] = 107;
            colors[3][2] = 62;
            
            // sunset
            colors[4][0] = 39;
            colors[4][1] = 33;
            colors[4][2] = 78;
        }
        
        public void drawHubGraphics(Graphics g) throws ConcurrentModificationException {
            g.setColor(Color.white);
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            //g.fillRect(0, 0, size.width, size.height);
            g.setColor(Color.blue);
            g.fillOval(size.width / 2 - 100, size.height / 6, 200, 200);
            g.setColor(Color.red);
            g.fillOval(size.width / 2 - 100, (size.height - 200) - size.height / 6, 200, 200);
            g.setColor(Color.green);
            g.fillOval(size.width / 6, size.height / 2 - 100, 200, 200);
            g.setColor(Color.yellow);
            g.fillOval(((size.width) - 200) - size.width / 6, size.height / 2 - 100, 200, 200);
            g.setColor(Color.black);
            g.drawRect(size.width / 6 + 200, size.height / 6 + 200, (2 * size.width) / 3 - 400, (2* size.height) / 3 - 400);
            g.setColor(Color.black);
            g.fillRect(spawn.x, spawn.y, 10, 10);
            g.setColor(Color.red);
            g.fillRect(mkentrance.x, mkentrance.y, 10, 10);
            g.fillRect(epentrance.x, epentrance.y, 10, 10);
            g.fillRect(hsentrance.x, hsentrance.y, 10, 10);
            g.fillRect(akentrance.x, akentrance.y, 10, 10);
            
            for (Family x: hubFamilys) {
                    x.draw(g);    
            }
            
            
            
//            mkflagback.draw(g);
//            akflagback.draw(g);
//            hsflagback.draw(g);
//            epflagback.draw(g);
            if (flags) {
                mkflag.draw(g);
                akflag.draw(g);
                hsflag.draw(g);
                epflag.draw(g);    
            }
            
            
            
        
        }
        
        public void drawParkGraphics(Graphics g) throws ConcurrentModificationException {
            
            
            g.drawImage(curr.image.imageVar, 0, 0, null);  
            
           /*if (curr.counter < 3000 ) {
                
                if (curr.counter % 1 == 0) {
                    for (int i = 1; i < curr.innerfills.length - 1; i++) {
                        for (int j = 1; j < curr.innerfills[i].length - 1; j++) {
                            if (curr.innerfills[i][j] == true) {
                                if (!curr.bools[i][j + 1]) {
                                    curr.innerfills[i][j+1] = true;
                                }
                                if (!curr.bools[i + 1][j]) {
                                    curr.innerfills[i + 1][j] = true; 
                                }
                                if (!curr.bools[i - 1][j]) {
                                    curr.innerfills[i - 1][j] = true;  
                                }
                                if (!curr.bools[i][j - 1]) {
                                   curr.innerfills[i][j-1] = true;  
                                } 
                            }
                        }
                    }  
                }   
            }*/
           // Fills the background with the sky color
           if (curr.counter < 3000)/*4 seconds*/ {
                for (int i = 1; i < curr.fills.length - 1; i++) {
                    for (int j = 1; j <curr.fills[i].length - 1; j++) {
                        if (curr.fills[i][j] == true) {
                            if (!curr.bools[i][j + 1] && !curr.innerfills[i][j + 1]) {
                                curr.fills[i][j+1] = true;
                            }
                            if (!curr.bools[i + 1][j] && !curr.innerfills[i + 1][j]) {
                                curr.fills[i + 1][j] = true; 
                            }
                            if (!curr.bools[i - 1][j] && !curr.innerfills[i - 1][j]) {
                                curr.fills[i - 1][j] = true;  
                            }
                            if (!curr.bools[i][j - 1] && !curr.innerfills[i][j - 1]) {
                               curr.fills[i][j-1] = true;  
                            } 
                        }
                    }
                }  
            }
           
           // Fills the grass sections 
               for (int i = 0; i < curr.innerfills.length; i++) {
                    for (int j = 0; j <curr.innerfills[i].length; j++) {
                        if (curr.bools[i][j]) {
                            if (!curr.bools[i][j + 1] && !curr.innerfills[i][j+1]) {
                                curr.grassfills[i][j+1] = true;
                            }
                            if (!curr.bools[i + 1][j] && !curr.innerfills[i + 1][j]) {
                                curr.grassfills[i + 1][j] = true;
                            }
                            if (i-1 >= 0) {
                            if (!curr.bools[i - 1][j] && !curr.innerfills[i-1][j]) {
                                    curr.grassfills[i - 1][j] = true;     
                                  
                            }}
                            if (j -1 >= 0) {
                            if (!curr.bools[i][j - 1] && !curr.innerfills[i][j-1]) {
                                
                                    curr.grassfills[i][j - 1] = true;    
                                
                                
                            }} 
                        }
                    }
                }
            
           
           
           
           
           
           
           
        
           if (!curr.simEnded) {
           if (curr.nicegraphics) {
           g.setColor(Color.BLACK);
           for (int i = 0; i < curr.horizontalSpaces; i++) {
                for (int j = 0; j < curr.verticalSpaces; j++) {
                    g.setColor(Color.BLACK);
                    //g.drawRect((int)curr.spaces[i][j].getX(), (int)curr.spaces[i][j].getY(), curr.blockSize, curr.blockSize);
                    if (i > 1 && j > 1) {
                        g.setColor(curr.bgcolor);
                    if (curr.bools[i][j] && !curr.bools[i-1][j] && !curr.bools[i+1][j] ) {
                        
                        g.fillRect((int)curr.spaces[i][j].getX() + 2, (int)curr.spaces[i][j].getY(), curr.blockSize - 4, curr.blockSize); 
                        g.setColor(Color.white);
                        g.fillRect((i * 10) + 3, (j * 10) + 3, 3, 3);
                    } else if(curr.bools[i][j] && !curr.bools[i][j - 1] && !curr.bools[i+1][j + 1] ) {

                        g.fillRect((int)curr.spaces[i][j].getX(), (int)curr.spaces[i][j].getY() + 2, curr.blockSize, curr.blockSize - 4); 
                        g.setColor(Color.white);
                        g.fillRect((i * 10) + 3, (j * 10) + 3, 3, 3);
                    } else if (curr.bools[i][j] && curr.bools[i][j - 1] && curr.bools[i+1][j + 1] && curr.bools[i-1][j] && curr.bools[i+1][j]  ) {

                        g.fillRect((int)curr.spaces[i][j].getX(), (int)curr.spaces[i][j].getY(), curr.blockSize, curr.blockSize); 
                    } else if (curr.bools[i][j]) {
                        // Color of the walls

                        g.fillRect((int)curr.spaces[i][j].getX(), (int)curr.spaces[i][j].getY(), curr.blockSize, curr.blockSize); 
                        g.setColor(Color.black);
                        g.drawLine((i * 10), (j * 10) + 3, (i*10) + 10, (j * 10) + 3);
                        g.drawLine((i * 10), (j * 10) + 6, (i*10) + 10, (j * 10) + 6);
                        g.drawLine((i * 10) + 3, (j * 10), (i * 10) + 3, (j*10) + 10);
                        g.drawLine((i * 10) + 6, (j * 10), (i * 10) + 6, (j*10) + 10);
                        g.setColor(Color.white);
                        g.fillRect((i * 10) + 3, (j * 10) + 3, 3, 3);
                        g.setColor(Color.black);
                        g.drawRect((int)curr.spaces[i][j].getX(), (int)curr.spaces[i][j].getY(), curr.blockSize, curr.blockSize);
                    }
                    }
                    if (curr.fills[i][j]) {
                        
                        if (loc < 0) {
                            loc = 0;
                        }
                        if (backColors.size() > 0) {
                            g.setColor(backColors.get(loc));
                            g.fillRect((int)curr.spaces[i][j].getX(), (int)curr.spaces[i][j].getY(), curr.blockSize, curr.blockSize);     
                        }
                         
                    }
                    if (curr.grassfills[i][j]) {
                        g.setColor(new Color(95, 173, 65));
                        g.fillOval((int)curr.spaces[i][j].getX(), (int)curr.spaces[i][j].getY(), curr.blockSize, curr.blockSize); 
                    }
                    /*if (curr.innerfills[i][j] == true) { 
                       g.setColor(new Color(162, 214, 249));
                       g.fillRect((int)curr.spaces[i][j].getX(), (int)curr.spaces[i][j].getY(), curr.blockSize, curr.blockSize); 
                   }*/
                }
            }
           int adjustx = 240;
           int adjusty = 60;
            if (currnum == 0) {
           //Paint grass in center
           g.setColor(new Color(95, 173, 65));
           g.fillOval(700 + adjustx, 350 + adjusty, 80, 80);
           g.setColor(Color.black);
           g.drawOval(700+ adjustx, 350+ adjusty, 80, 80);
           g.setColor(Color.white);
           g.fillOval(710+ adjustx, 360+ adjusty, 60, 60);
           g.setColor(Color.black);
           g.drawOval(710+ adjustx, 360+ adjusty, 60, 60);
           g.fillOval(730+ adjustx,380+ adjusty,20,20);
           g.drawLine(740+ adjustx, 350+ adjusty, 740+ adjustx, 430+ adjusty);
           g.drawLine(700+ adjustx,390+ adjusty,780+ adjustx,390+ adjusty);
           // Paint Castle
           
           g.setColor(Color.pink);
           g.fillRect(670, 250, 140, 60);
           g.fillRect(698, 205, 84, 45);
           g.fillRect(740, 175, 42, 30);
           
           g.setColor(Color.black);
           g.fillRect(720, 280, 40, 30);
           g.fillOval(720, 260, 40, 40);
           }
           
           
           for (Attraction a : curr.rides) {
                if (curr.labelsShowing) {
                    a.draw(g);    
                }
            }
           drawBag((Graphics2D) g);
           
           
           
            
            
           // CLOUDS
           for (int j = 50; j < 900; j = j+200) {

           for (int i = 0; i < 1900; i = i+300) {
                   if (i + curr.currentTime[2] < 1900) {
                        g.setColor(new Color(255, 255, 255, 200));
                        //g.setColor(Color.white);
                        int num = i + curr.currentTime[2] * 4;
                        while (num > 1800) {
                            num = num - 1800;
                        }
                        int num2 = j;
                        if ((i/100) % 9 == 0) {
                            
                        } 
                        else if ((i/100) % 2 == 0) {
                            num2 += 66;
                        }
                        else if ((i/100) % 3 == 0) {
                            num2 += 132;
                        }
                        g.fillOval(num + 12, num2 + 10, 30, 30);
                        g.fillOval(num + 34, num2, 20, 20);
                        g.fillOval(num, num2, 20, 20);
                        
                   }
           }
           }
           }
           
           g.setColor(Color.blue);
           //g.fillPolygon(xPoints, yPoints, WIDTH);
            
               
            //Flag flag = new Flag(80, 80, 75, 60, Color.black, "Blake Gould", new java.awt.Font("SansSerif", 1, 18), 3);
            //flag.draw(g);
//                Point p = a.getAccessPoint();
//                int xPress = (int)p.getX();
//                int yPress = (int)p.getY();
//                    while (xPress % curr.blockSize != 0) {
//                        xPress--;
//                    }
//                    while (yPress % curr.blockSize != 0 ) {
//                        yPress--;
//                    }
//                
//                
//                
//                Point p2 = a.getExitPoint();
//                int xPress2 = (int)p2.getX();
//                int yPress2 = (int)p2.getY();
//                while (xPress2 % curr.blockSize != 0) {
//                    xPress2--;
//                }
//                while (yPress2 % curr.blockSize != 0 ) {
//                    yPress2--;
//                }
//                
//                Point p3 = a.earlyExitPoint;
//                int xPress3 = (int)p3.getX();
//                int yPress3 = (int)p3.getY();
//                while (xPress3 % curr.blockSize != 0) {
//                    xPress3--;
//                }
//                while (yPress3 % curr.blockSize != 0 ) {
//                    yPress3--;
//                }
//                
//                g.setColor(Color.white);
//                g.fillRect(xPress, yPress, 60, 60);
//                if (!curr.labelsShowing) {
//                    g.setColor(Color.black);
//                    g.drawRect(xPress, yPress, 60, 60);
//                }
                
                
                
                
                
//                g.setColor(Color.red);
//                g.fillRect(xPress, yPress, curr.blockSize, curr.blockSize);   
//                g.setColor(Color.yellow);
//                g.fillRect(xPress2, yPress2, curr.blockSize, curr.blockSize);
//                g.setColor(Color.lightGray);
//                g.fillRect(xPress3, yPress3, curr.blockSize, curr.blockSize);
 
            for (Family t : curr.Familys) {
                    t.draw(g);   
            }
           }
            if (curr.nicegraphics) {
            // Draw the time bar for the sun/moon going across the bar to indicate the time
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            g.setColor(Color.BLACK);
            g.fillRect(0, 800, dim.width, 50);
            colors = new int[5][3];
            setSunColors();
            int divisions = dim.width / 5;
            int colorChangeInv = divisions / 5;
            int currInvCounter = 0;
            int x = 0;
            int y = 800;
            int[] currColor = {colors[0][0], colors[0][1], colors[0][2]};
            int hourCoverage = 15;
            int hourChangeInv = divisions / hourCoverage;
            int currHour = 7;

            for (int i = 0; i < divisions; i++) {
                for (int j = 0; j < 3; j++) {
                    if (currColor[j] > 255) {
                        currColor[j] = 255;
                    }
                    if (currColor[j] < 0) {
                        currColor[j] =0;
                    }
                }
                Color color = new Color(currColor[0], currColor[1], currColor[2]);
                
                if (currInvCounter < colorChangeInv ) {
                currColor[0] = ((colors[1][0] - colors[0][0]) / colorChangeInv) + currColor[0];
                currColor[1] =((colors[1][1] - colors[0][1]) / colorChangeInv) + currColor[1];
                currColor[2] =  ((colors[1][2] - colors[0][2]) / colorChangeInv) + currColor[2];   
                } else if (currInvCounter < (2*colorChangeInv)) {
                currColor[0] = ((colors[2][0] - colors[1][0]) / colorChangeInv) + currColor[0];
                currColor[1] =((colors[2][1] - colors[1][1]) / colorChangeInv) + currColor[1];
                currColor[2] =  ((colors[2][2] - colors[1][2]) / colorChangeInv) + currColor[2];   
                } else if (currInvCounter < (3*colorChangeInv)) {
                currColor[0] = ((colors[3][0] - colors[2][0]) / colorChangeInv) + currColor[0];
                currColor[1] =((colors[3][1] - colors[2][1]) / colorChangeInv) + currColor[1];
                currColor[2] =  ((colors[3][2] - colors[2][2]) / colorChangeInv) + currColor[2];       
                } else if (currInvCounter < (4*colorChangeInv)) {
                currColor[0] = ((colors[4][0] - colors[3][0]) / colorChangeInv) + currColor[0];
                currColor[1] =((colors[4][1] - colors[3][1]) / colorChangeInv) + currColor[1];
                currColor[2] =  ((colors[4][2] - colors[3][2]) / colorChangeInv) + currColor[2]; 
                }
                
                
                if (currInvCounter % hourChangeInv == 0) {
                    g.setColor(Color.black);
                    if (currHour == 13) {
                        currHour = 1;
                    }
                    g.drawString("" + currHour, x, y);
                    currHour++;
                }
                currInvCounter++;
                
                backColors.add(color);
                g.setColor(color);
                
                
                
                
                
                
                
                
                g.fillRect(x, y, 5, 50);
                x += 5;
 
            }
            
            
            
            
            
            
            
            int hoursWorthDistance = 112;
            double distance = 0;
            distance = ((double)hoursWorthDistance * ((double)curr.dailySecondsSurpassed / 3600)) - ((double)7 * hoursWorthDistance);
          
            
            // Draw the sun/ mooon
            if (distance < hoursWorthDistance * 11) {
                g.setColor(Color.yellow);
                g.fillOval((int)distance - 25 , 800, 50, 50);    
            } else {
                g.setColor(Color.cyan);
                g.fillOval((int)distance - 25 , 800, 50, 50);  
            }
            loc = (int)distance - 25;
            loc = loc / 5;
            
            // DOES NOT WORK
            for (Attraction a: curr.rides) {
                if (a.closed) {
                    g.setColor(Color.red);
                    float f=40.0f; // font size.
                    g.setFont(g.getFont().deriveFont(f));
                    g.drawString(("X"), x , y + 10);
                }
            }
            
            }
            
//           } else {
//               curr.simEndedArea.setVisible(true);
//               this.setBackground(Color.black);
//               this.setOpaque(true);
//               curr.newDayButton.setVisible(true);
//           }

            g.setColor(Color.white);
            g.fillOval(curr.parkExitPoint.x, curr.parkExitPoint.y, 10, 10);
            g.setColor(Color.red);
            g.drawOval(curr.parkExitPoint.x, curr.parkExitPoint.y, 10, 10);

        
        }
        
        public void drawHover(Graphics g) throws ConcurrentModificationException  {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            PointerInfo a = MouseInfo.getPointerInfo();
            Point b = a.getLocation();
            boolean caseMet = false;
            switch(currnum) {
                case 2: if (b.getY() < 100 ) {
                    hover = true;
                    caseMet = true;
                }
                    break;
                case 3: if (b.getX() > size.getWidth() - 100) {
                    hover = true;
                    caseMet = true;
                }
                    break;
                case 0: if (b.getY() > size.getHeight() - 100) {
                    hover = true;
                    caseMet = true;
                }
                    break;
                case 1: if (b.getX() < 100) {
                    hover = true;
                    caseMet = true;
                }
                
                case 4: if (b.getX() < 100 || b.getY() > size.getHeight() - 100 || b.getX() > size.getWidth() - 100 || b.getY() < 100) {
                    hover = true;
                    caseMet = true;
                }
                    break;    
            }
           
            if (!caseMet) {
                hover = false;
            }
           
           
           if (hover) {
            g.setColor(Color.white);
                switch (currnum) {
                    case 0: g.drawOval(0, size.height - 100, size.width, 200);  break; 
                    case 1: g.drawOval(-100, 0, 200, size.height); break;
                    case 2: g.drawOval(0, -100, size.width, 200); break;
                    case 3: g.drawOval(size.width - 100, 0, 200, size.height);break;
                    case 4: g.drawOval(0, size.height - 100, size.width, 200); g.drawOval(-100, 0, 200, size.height);g.drawOval(0, -100, size.width, 200);g.drawOval(size.width - 100, 0, 200, size.height);;break;
                }
                g.setColor(new Color((float)1.0,(float) 1.0,(float) 1.0, (float)0.5));
                switch (currnum) {
                    case 0: g.fillOval(0, size.height - 100, size.width, 200);break;
                    case 1: g.fillOval(-100, 0, 200, size.height);break;
                    case 2: g.fillOval(0, -100, size.width, 200);break;
                    case 3: g.fillOval(size.width - 100, 0, 200, size.height);break;
                    case 4: g.fillOval(size.width - 100, 0, 200, size.height);g.fillOval(0, -100, size.width, 200);g.fillOval(-100, 0, 200, size.height);g.fillOval(0, size.height - 100, size.width, 200);break;
                }
            }
        }
        
        public void drawBag(Graphics2D g)throws ConcurrentModificationException  { 
            bgg.draw(g); 
            if (!bgg.isOpen) {
                button1.setVisible(false);
                button2.setVisible(false);
                button3.setVisible(false);
                button4.setVisible(false);
                buttonT.setVisible(false);
                
            } else {
                button1.setVisible(true);
                button2.setVisible(true);
                button3.setVisible(true);
                button4.setVisible(true);
                buttonT.setVisible(true);
                
                int x = 130;
                int y = 65;
                g.setColor(Color.black);
                g.fillOval(x - 5, y, 30, 30);
                g.setColor(Color.white);
//                g.drawString("Tracking", x, y);

                g.setColor(Color.black);
                g.fillOval(x + 50 - 5, y, 30, 30);
                g.setColor(Color.white);
//                g.drawString("Attraction Stats", x + 30, y - 60);

                g.setColor(Color.black);
                g.fillOval(x + 100 - 5, y, 30, 30);
                g.setColor(Color.white);
//                g.drawString("Wait Times", x + 60, y);

                g.setColor(Color.black);
                g.fillOval(x + 150 - 5, y, 30, 30);
                g.setColor(Color.white);
//                g.drawString("Closures", x + 90, y - 60);
                
                int bannerwidth = 15;
                Banner b1 = new Banner("Tracking", Color.red, bannerwidth, x - 5, y + 30);
                Banner b2 = new Banner("Attraction Stats", Color.yellow, bannerwidth, x - 5 + 50, y + 30);
                Banner b3 = new Banner("Wait Times", Color.blue, bannerwidth, x - 5 + 100, y + 30);
                Banner b4 = new Banner("Closures", Color.gray, bannerwidth, x - 5 + 150, y + 30);
                b1.draw(g);
                b2.draw(g);
                b3.draw(g);
                b4.draw(g);
            }
            
                
           
        }
       
        public Bag bgg= new Bag(70, 40, Color.pink, 50);;
}
    

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FamilyInfoList;
    private javax.swing.JList<String> attractionsList;
    private javax.swing.JTextArea fastpassList;
    private javax.swing.JLabel info1;
    private javax.swing.JLabel info2;
    private javax.swing.JLabel info3;
    private javax.swing.JLabel info4;
    private javax.swing.JLabel info5;
    private javax.swing.JLabel info6;
    private javax.swing.JLabel info7;
    private javax.swing.JLabel info8;
    private javax.swing.JLabel info9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel paknam;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel subpanel1;
    private javax.swing.JPanel subpanel2;
    private javax.swing.JPanel subpanel3;
    private javax.swing.JPanel subpanel4;
    private javax.swing.JPanel subpanel5;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JList<String> trackingList;
    // End of variables declaration//GEN-END:variables
}
