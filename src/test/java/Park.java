



import essentials.Guest;
import essentials.Attraction;
import essentials.Fastpass;
import other.myImage;
import timers.Task;
import mains.Display;
import timers.parkTimer;
import timers.parkTimerTask;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import timers.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blake
 */
public class Park extends JFrame{
    

    
    public int dailySecondsSurpassed = 0;
    public Integer[] currentTime = {0,0,0};
    public JLabel timeLabel = new JLabel();
    public JPanel dp = new JPanel();
    
    public Display d; 
    
    public boolean parkHopping = false;
    
    public Point parkExitPoint = new Point();
    
    
    public int blockSize;
    public double xSize;
    public double ySize;
    public boolean shiftPressed;
    int shifti = -1;
    int shiftj = -1;
    //public Attraction leaving;
    
    public boolean labelsShowing = true;
    public String saveFileLocation = "SaveFills.txt";
    
    public ArrayList<Attraction> rides = new ArrayList<>();
            
    public ArrayList<Guest> guests = new ArrayList<>();
    public ArrayList<Guest> guestsmaster = new ArrayList<>();
    public ArrayList<Fastpass> fastpasses = new ArrayList<>();
    
    public JList list = new JList();

    public DefaultListModel trackingModel = new DefaultListModel();
    
    public boolean running = true;
    
    public parkTimer tmr;
    
    public boolean nicegraphics = true;
    
    public boolean statsShowing = false;
    public boolean trackingShowing = false;
    
    public JPanel panel;
    public Guest guest = new Guest("");
    public String boolsfile; public String name;
    public Color bgcolor;
    public ArrayList<Point> colorstarters;
    public myImage image;
    
    public Park(String name, ArrayList<Attraction> rides, String boolsfile, Color bgcolor, myImage image, ArrayList<Point> colorstarters)  {
        this.boolsfile = boolsfile;
        this.rides = rides;
        this.name = name;
        this.bgcolor = bgcolor;
        this.colorstarters = colorstarters;
        this.image = image;
        createBoard();
        colorBoardLoad();
        configureRides();
        configureGuests();
        configureFastpasses();
        configureFrameAndPanel();
        b1x.setVisible(false);
        b5x.setVisible(false);
        b15x.setVisible(false);
        b30x.setVisible(false);

        ArrayList<parkTimerTask> tasks = new ArrayList<>();
        
        // Creates and schedules a timer task for 
        // the addition of each group of people
        int delay = 0;
        for (int i = 0; i < 2; i ++) {
            delay += 25000;
            Runnable x = new Runnable() {
                @Override
                public void run() {
                     for (int x = 130; x <= 1280; x += 10) {
                        for (int y = 830; y <= 870; y += 10) {
                            Random random = new Random();
                            int rand = random.nextInt(3);
                            Guest guest = new Guest("Adult");
                            switch (rand) {
                                case 0:
                                    guest = new Guest("Child");
                                    break;
                                case 1:
                                    guest = new Guest("Adult");
                                    break;
                                case 2:
                                    guest = new Guest("Young Adult");
                                    break;
                                default:
                                    break;
                            }
                            guest.setLocation(x, y);
                            guest.setDestination(determineDestination(guest));
                            guest.tracking = false;
                            guests.add(guest);
                            guestsmaster.add(guest); 
                        }
                    }
                }
            };
            Task addMoreGuests = new Task(x);
            parkTimerTask task1 = new parkTimerTask(addMoreGuests, delay, x);
            tasks.add(task1);
        }
        //SPED UP SPED UP SPED UP SPED UP SPED UP SPED UP
        // Calls the method to assign fastpasses and then displays the tracking panel.
        Runnable x = new Runnable() {
            @Override
            public void run() {
                assignFastpasses();
                ageLabel.setVisible(true);
                fastpassTA.setVisible(true);
                shownTrackerList.setVisible(true);
                b1x.setVisible(true);
                b5x.setVisible(true);
                b15x.setVisible(true);
                b30x.setVisible(true);
            }
        };
        Task assignfastpasses = new Task(x);
        parkTimerTask task2 = new parkTimerTask(assignfastpasses, 75000, x);
        tasks.add(task2);
        
       
        
        // Creates and Loads TimerTasks to transfer people on and off rides at
        // the specified time interval (outFlowTime) for each ride
        
        for (Attraction a : rides) {
            Runnable temp = new Runnable() {
                @Override
                public void run() {
                    if (running) {
                        // By exiting the ride first, we ensure that a person doesnt get on the ride 
                        // and then exit it immediately
                        rideExit(a);
                        rideLoad(a);    
                    }
                    
                }
            };
            Task newTask = new Task(temp);
            // The task is called at half the outflowtime so that
            // the time for one individual to get on and off the ride
            // is equal to the outFlowTime (oft / 2) * 2 = oft;
            parkTimerTask task3 = new parkTimerTask(newTask,0, (a.outFlowTime / 2), temp);
            tasks.add(task3);
        }
        
        // Calls method guestMove every 50 milliseconds
        Runnable temp = new Runnable() {
            @Override
            public void run() {
                if (running) {
                    guestMove();   
                }
            }
        };
        
        Task guestMove = new Task(temp); 
        parkTimerTask task4 = new parkTimerTask(guestMove, 0, 50, temp);
        tasks.add(task4);
        
        // Calls method configureWaits every 1000 milliseconds
        Runnable wrun = new Runnable() {
            @Override
            public void run() {
                if (running) {
                    for (Attraction a : rides) {
                        a.setWaitTime();  
                    }
                }   
            }
        };
        Task configureWaits = new Task(wrun);
        parkTimerTask task5 = new parkTimerTask(configureWaits,0, 1000, wrun);
        tasks.add(task5);
        
        // Calls the method updateLists() and then adjusts the 
        // clock. If the clock reaches an hour mark, an entry is recorded (method: createEntry())
        // Called every second
        Runnable secrun = new Runnable(){
            @Override
            public void run() {
                
                updateLists(); 
                
                // Updates the time label 
                if (running) {
                    dailySecondsSurpassed += 1;    
                }
                // Gives hours
                currentTime[0] = dailySecondsSurpassed / 3600;
                // Gives minutes
                currentTime[1] = (dailySecondsSurpassed % 3600) / 60;
                // Gives seconds
                currentTime[2] = (dailySecondsSurpassed - (currentTime[0] * 3600) - (currentTime[1] * 60));
                
                shownTimeLabel.setText(currentTime[0] + ":" + currentTime[1] + ":" + currentTime[2]);
                // Create an Entry of Wait Times every hour
                if (currentTime[1] == 0 && currentTime[2] == 0) {
                    createEntry(currentTime[0]);
                }
                //panel.repaint();
            }
        };
        Task aSecondPasses = new Task(secrun);
        //dynamictasks.put(1000, aSecondPasses);
        parkTimerTask task6 = new parkTimerTask(aSecondPasses, 0, 1000, secrun);
        tasks.add(task6);
        
        // For each attraction, if the ride is closed, call the method rideEvac();
        Runnable riderun = new Runnable() {
            @Override
            public void run() {
                for (Attraction a : rides) {
                    if (a.closed) {
                        rideEvac(a);     
                    }      
                } 
            }   
        };
        Task rideEvac = new Task(riderun);
        //dynamictasks.put(50, rideEvac);
        parkTimerTask task7 = new parkTimerTask(rideEvac, 0, 50, riderun);
        tasks.add(task7);

        
       tmr = new parkTimer(1, tasks);
       tmr.start();
       
       this.setVisible(false);
       Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
       int px = size.width / 2;
       int py = size.height - 200;
       while (px % 10 != 0) {
           px--;
       }
       while (py % 10 != 0) {
           py--;
       }
       Point p = new Point(px, py);
       setParkExitPoint(p);
   
    }
    
    
    
    // The number of squares that fit vertically and horizontally 
    // within the screen
    public int horizontalSpaces;
    public int verticalSpaces;
    // The arraylist of squares that make up the display
    public Rectangle[][] spaces;
    // The squares that are walls are true
    public Boolean[][] bools;
    // The squares that are within the walls are true
    public Boolean[][] innerfills;
    //The squares outside of the walls are true
    public Boolean[][] fills;
    // The squares that are grass spaces are true
    public Boolean[][] grassfills;
    
    /**/
    public void setD(Display d) {
        this.d = d;
    }
    
    /*
    Creates the initial board, setting each space at false,
    these spaces will be loaded in from the file and 
    colored appropriately later
    */
    private void createBoard() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        blockSize = 10;
        xSize = size.getWidth();
        ySize = size.getHeight();
        horizontalSpaces = (int) xSize / blockSize;
        verticalSpaces = (int) ySize / blockSize;
        spaces = new Rectangle[horizontalSpaces][verticalSpaces];
        bools = new Boolean[horizontalSpaces][verticalSpaces];
        fills = new Boolean[horizontalSpaces][verticalSpaces];
        innerfills = new Boolean[horizontalSpaces][verticalSpaces];
        grassfills = new Boolean[horizontalSpaces][verticalSpaces];
        
        
        
        int currx = 0;
        int curry = 0;
        for (int i = 0; i < horizontalSpaces; i++) {
            for (int j = 0; j < verticalSpaces; j++) {
                Point p = new Point(currx, curry);
                
                curry += blockSize;
                Dimension d = new Dimension(blockSize, blockSize);
                spaces[i][j] = new Rectangle(p, d);
                bools[i][j] = false;
                fills[i][j] = false;
                innerfills[i][j] = false;
                grassfills[i][j] = false;
         
            }
            currx += blockSize;  
            curry = 0;
        }
        
        //Initialize the spots that spread colors throughout the image
        //innerfills[128][87] = true;
        //innerfills[100][30] = true;
        for (Point p: colorstarters) {
            fills[(int)p.getX() / 10][(int)p.getY() / 10] = true;
        }

    }
    
    /*Fills in the squares created in "createBoard()"
        With two colors: border, background, and, ofc, if not
        -> the default color
    */
    private void colorBoardLoad() {
        // Save.txt contains all the booleans that determine if 
        // each space should be colored blue or not
        try(Scanner fin = new Scanner(new File(this.boolsfile))){
            if(fin.hasNextLine()) {
                while (fin.hasNextLine()) {
                    String curr = fin.nextLine();
                    String[] arr = curr.split(" ");
                    int i = Integer.parseInt(arr[0]);
                    int j = Integer.parseInt(arr[1]);
                    if (j < 96 && i > 0 && j > 0) {
                    if (arr[2].equalsIgnoreCase("T")) {
                        bools[i][j] = true;   
                    } else {
                        bools[i][j] = false;   
                    }   
                    }
                }    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // SaveFills.txt contains all the booleans that determine if 
        // each space should be filled with the background color or not
        /*try(Scanner fin2 = new Scanner(new File(this.fillsfile))){
            if(fin2.hasNextLine()) {
                while (fin2.hasNextLine()) {
                    String curr = fin2.nextLine();
                    String[] arr = curr.split(" ");
                    int i = Integer.parseInt(arr[0]);
                    int j = Integer.parseInt(arr[1]);
                    if (arr[2].equalsIgnoreCase("T")) {
                        fills[i][j] = true;

                    } else {
                        fills[i][j] = false;

                    }   
                }
            }   
        } catch (Exception e) {
                e.printStackTrace();
        }*/
    }
    
    /*
    Adds the rides to the "rides" arrayList
    This arraylist is iterated over a lot
    The rides are added one time each
    */
    public void configureRides() {
        
        
        for (Attraction r: rides) {
            r.fpswitch = false;
        }
    }
    
    /*
    Adds the first round of guests including 1 additional guest
    This additional guest is the first tracked guest and has a 
    preset initial destination
    */
    public void configureGuests() {
        // ADD THE Initial GUESTS
        for (int x = 130; x <= 1280; x += 10) {
            for (int y = 830; y <= 870; y += 10) {
                Random random = new Random();
                int rand = random.nextInt(3);
                Guest guest = new Guest("Adult");
                switch (rand) {
                    case 0:
                        guest = new Guest("Child");
                        break;
                    case 1:
                        guest = new Guest("Adult");
                        break;
                    case 2:
                        guest = new Guest("Young Adult");
                        break;
                    default:
                        break;
                }
                guest.setLocation(x, y);
                guest.setDestination(determineDestination(guest));
                guest.tracking = false;
                guests.add(guest);
                guestsmaster.add(guest); 
            }
        }
        
        /*
        The first guest that is tracked is personally set
        */
        guest = new Guest("Adult");
        guest.setLocation(100, 430);
        guest.tracking = false;
        guest.setDestination(determineDestination(guest));
        guests.add(guest);
        
        guestsmaster.add(guest);
    }
    
    /*
    * Sorts the attractions and then creates FP equal to 3 times the number of 
    * subjects in the park. The algorithm then creates vatious fastpasses for 
    * each attraction during every hour intercval during the park day
    */
    public void configureFastpasses() {
        sortAttractions();
        int totalNumFP = guests.size() * 9;
        int numFPperAttraction = (totalNumFP / rides.size()) + 1;
        for (int i = 0; i < rides.size(); i++) {
            for (int j = 0; j < numFPperAttraction; j++) {
                Fastpass fp = new Fastpass((j % 14) + 7, rides.get(i));
                fastpasses.add(fp);
            }
        }
        
        


    }
    
    /*Sorts the rides arraylist of attractions by priority of said attractions*/
    public void sortAttractions() {
        boolean sorted = false;
        while (!sorted) {
            for (int i = 0; i < rides.size() - 1; i++) {
                if(rides.get(i).priority < rides.get(i + 1).priority) {
                    Attraction temp = rides.get(i);
                    rides.set(i,rides.get(i + 1));
                    rides.set(i + 1, temp);
                    sortAttractions();
                }
            } 
            sorted = true;

        }    
    }
    
    /*The method iterates through the guests three times and the fastpasses arraylist once
    * Assigning the fastpasses to the guests. Prior to this, the fastpasses are shuffled at random.
    */
    public void assignFastpasses() {
        Collections.shuffle(fastpasses);
        int index = 0;
        for (Guest g: guestsmaster) {

                g.fpasses.add(0,fastpasses.get(index));
                System.out.println(g.fpasses.get(0));
                index++;
            
        }
        for (Guest g: guestsmaster) {

                g.fpasses.add(1,fastpasses.get(index));
                System.out.println(g.fpasses.get(1));
                index++;
            
        }
        for (Guest g: guestsmaster) {

                g.fpasses.add(2,fastpasses.get(index));
                System.out.println(g.fpasses.get(2));
                index++;
            
        }
        System.out.println("MainFram.assignFastpasses()");
    }
    
    public DefaultListModel model = new DefaultListModel();
    
    /**/
    public void configureFrameAndPanel() {
        shownTrackerList = new JList<>();
        shownTimeLabel = new JLabel();
        shownList = new JList<>();
        // Sets the starting time tp 7 o'clock
        dailySecondsSurpassed += (7 * 3600);  
        
        
        shownList = list;
        panel = new myPanel();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setVisible(true);
        this.setSize(size);
        this.show(); 
        
         model = new DefaultListModel();
        for (Attraction a : rides) {
            model.addElement(a.toString());
        }
        list = new JList(model);
        list.setLocation(900, 0);
        panel.add(list);
        list.setVisible(true);
 
        shownTrackerList.setModel(trackingModel);
        shownTrackerList.setLocation(900, 900);
        
        initComponents();
        simEndedArea.setVisible(false);
        newDayButton.setVisible(false);
        shownList.setModel(model);
        ageLabel.setVisible(false);
        fastpassTA.setVisible(false);
        shownTrackerList.setVisible(false);
        
        
        
        
        shownTrackerList.setModel(trackingModel);
        
        
        pauseButton.setBounds(500, 500, 50, 50);
        pauseButton.addActionListener((e) -> {
            running = !running;
        });
        
        //Fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        
        /*try {
                Clip clip = AudioSystem.getClip();
                File file = new File("C:\\Users\\blake\\Desktop\\Piano Sounds\\Main Street USA Area Music - Loop.wav");
                clip.open(AudioSystem.getAudioInputStream(file));
                clip.start();


            }catch(Exception e) {
                e.printStackTrace();
        }*/     
        
        reportLoc.setVisible(false);
        printReportButton.setVisible(false);
        
        
    }
    
    /*
    Determines if a guest should move or not. It also determines
    if they go up, left, right, down when they move. It also determines
    If they enter a line queue*/
    // "occupied" currently has no function
    public int counter;
    public void guestMove() {
        
        for (Guest g: guests) {
            if (g.transferred) {
                determineDestination(g);
                g.transferred = false;
            }
        }
        
        counter++;
        // Called every three minutes, checks to see if anyone has completed all the rides
        // If so, their ride history gets wiped
        if (counter % 3600 == 0) {
            for (Guest g: guests) {
                if (g.ridesRidden.size() == rides.size()) {
                    for (Attraction a: rides) {
                        g.ridesRidden.remove(a);
                    }
                }  
            }
            for (Attraction b : rides) {
                for (Guest g: b.line) {
                    if (g.ridesRidden.size() == rides.size()) {
                        for (Attraction a: rides) {
                            g.ridesRidden.remove(a);
                        }
                    }
                } 
                for (Guest g: b.fline) {
                    if (g.ridesRidden.size() == rides.size()) {
                        for (Attraction a: rides) {
                            g.ridesRidden.remove(a);
                        }
                    }
                }
                for (Guest g: b.ride) {
                    if (g.ridesRidden.size() == rides.size()) {
                        for (Attraction a: rides) {
                            g.ridesRidden.remove(a);
                        }
                    }
                }
            }
        }
        
        // This indicates if an individual is already exiting this passTime()
        boolean oneExiting = false;
        
        // If the guest is on a ride, increase the time they've been on that ride
        for (Attraction a: rides) {
            for (Guest g: a.ride) {
                g.expTimer += 0.00083333333333333333333333333333333333333333333333333333;
                
            }
            // If the guest is in line and no other guests have exited this movement,
            // If the exaust is 15 less at some other attraction, the guest early exits
            // the line and goes to that new location. The guest will not early exit from a fastpass line
            for (Guest g: a.line) {
                if (!oneExiting) {
                    Attraction curr = g.attractionDestination;
                    for (Attraction a2 : rides) {
                        if (getExhaust(g, curr) - getExhaust(g, a2) > 15 && !a2.closed) {

                            curr.line.remove(g);
                            g = earlyExit(g);
                            oneExiting = true;
                            if (g.tracking) {
                                trackingModel.addElement("The subject changed lines (" + currentTime[0] + ":" + currentTime [1] + ")");
                            }
                            break;

                        }
                    }
                }
            }
        }
        
        ArrayList<Guest> gueststoremove = new ArrayList<>();
        
        // When the guest reaches their detsination, add them to the appropriate line queue (Fastpass)
        for (Guest g : guests) {
            if (g.attractionDestination != null) {
            if (g.location.getX() == g.destination.getX() && g.location.getY() == g.destination.getY()) {
                for (Fastpass f: g.fpasses) {
                    if ((f.target == g.attractionDestination && f.slot == currentTime[0]) || (f.target == null && f.slot == currentTime[0])) {
                    if (g.attractionDestination.addToFPQueue(g)) {    
                        if (g.tracking) {
                            trackingModel.addElement("The subject used a fastpass for " + g.attractionDestination.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");    
                        }
                        g.fpasses.remove(f);
                        String str = "Fastpasses:\n";
                        for (Fastpass h: g.fpasses) {
                            if (h != null) {
                                str += h.toString() + " \n";
                            }
                        }
                        fastpassTA.setText(str);
                        gueststoremove.add(g);
                        break;
                    }
                    }
                }
            }
            }
        }
        
        for (Guest g: gueststoremove) {
            guests.remove(g);
        }
        gueststoremove.clear();
          
        // When the guest reaches their detsination, add them to the appropriate line queue (Regular Line)   
        for (Guest g : guests) {   
            if (g.attractionDestination != null) {
            if (g.location.getX() == g.destination.getX() && g.location.getY() == g.destination.getY()) {
                if (g.attractionDestination.addToQueue(g)) {
                    if (g.tracking) {
                        trackingModel.addElement("The subject began waiting in line for " + g.attractionDestination.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");    
                    }
                    gueststoremove.add(g);   
                }    
            }
            }
        }
        
        for (Guest g: gueststoremove) {
            guests.remove(g);
        }
        gueststoremove.clear();
        
        for (Guest g: guests) {
            if(g.location.x == parkExitPoint.x && g.location.y == parkExitPoint.y && g.destination.x == parkExitPoint.x && g.destination.y == parkExitPoint.y) {
                gueststoremove.add(g);
                d.hubguests.add(g);  
                g.transferred = true;
            }
        }
        
        for (Guest g: gueststoremove) {
            guests.remove(g);
        }
        gueststoremove.clear();

        for (Guest g : guests) {
                // This indicates if g has moved or not this passTime()
                boolean moved = false;

                // Up
                if (g.location.getY() > g.destination.getY()) {
                    int x = (int)g.location.getX();
                    int y = (int)g.location.getY() - 10;
                    if (x > 0 && y > 0) {
                            g.setLocation(x, y);
                            moved = true;    
                    }
                }

                // Down
                if (g.location.getY() < g.destination.getY() && !moved) {
                    int x = (int)g.location.getX();
                    int y = (int)g.location.getY() + 10;
                    if (x > 0 && y > 0) {
                            g.setLocation(x, y);
                            moved = true;  
                    }
                }

                // Left
                if (g.location.getX() > g.destination.getX() && !moved) {
                    int x = (int)g.location.getX() - 10;
                    int y = (int)g.location.getY();
                    if (x > 0 && y > 0) {
                            g.setLocation(x, y);
                            moved = true;
                    }
                } 

                // Right
                if (g.location.getX() < g.destination.getX() && !moved) {
                    int x = (int)g.location.getX() + 10;
                    int y = (int)g.location.getY();
                    if (x > 0 && y > 0) {
                            g.setLocation(x, y);
                            moved = true;
                    }
                }

                if (currentTime[0] >= 20) {
                    endSim();
                }
                
            
            
        }
        if (nicegraphics) {
            dp.repaint();    
        }
        
        for (Attraction a: rides) {
            for (Guest g: a.line) {
                if (g.tracking) {
                String str = "Fastpasses:\n";
                for (Fastpass f: g.fpasses) {
                    str += f.toString() + " \n";
                }
                fastpassTA.setText(str);
                }
            } 
            for (Guest g: a.ride) {
                if (g.tracking) {
                String str = "Fastpasses:\n";
                for (Fastpass f: g.fpasses) {
                    str += f.toString() + " \n";
                }
                fastpassTA.setText(str);
                }
            }
            for (Guest g: a.fline) {
                if (g.tracking) {
                String str = "Fastpasses:\n";
                for (Fastpass f: g.fpasses) {
                    str += f.toString() + " \n";
                }
                fastpassTA.setText(str);
                }
            }
        }
        
    }  
    
    /*
    If the simulation is actively running, 
    wait times are updated on the display  
    */
    public void configureWaits() {
        if (running) {
            for (Attraction a : rides) {
                a.setWaitTime();  
            }
        }    
    }
    
    /* 
    Updates the list of attractions and wait times
    Update the tracking list
    */
    public void updateLists() {
        // Update the list of attractions and wait times
        // by creating a new list and filling it
        DefaultListModel model = new DefaultListModel();
        for (Attraction a : rides) {
            model.addElement(a.toString());
        }
        shownList.setModel(model);
        
        // Update the tracking list
        shownTrackerList.setModel(trackingModel);
        
   
    }
    
    /*Adds an arraylist of integers formed from the ride wait times
    to the arraylist entries, which is an arraylist of arraylists*/
    // The array list is 14 in length to hold 14 hours of the park day
    public ArrayList<ArrayList<Integer>> entries = new ArrayList<ArrayList<Integer>>(14);
    public void createEntry(int x) {
        ArrayList<Integer> curr = new ArrayList<>();
        curr.add(x);
        for (Attraction a: rides) {
            curr.add(a.getWaitTime());
        }
        entries.add(curr);
        
    }
    
    /* takes individuals from a recently closed ride and the rides queue, 
    removing them to go somewhere else
    */
    public void rideEvac(Attraction a) {
        // Evacuate a guest from the ride
        if (a.ride.size() > 0) {

                Guest g = (Guest) a.ride.remove();
                g.expTimer = 0;
                g.setLocation((int)a.getExitPoint().getX(), (int)a.getExitPoint().getY());

                if (g.tracking) {
                    trackingModel.addElement("Subject evacuated " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
                }
                guests.add(g);
                g.setDestination(determineDestination(g));   
        }

        // Remove a guest from the line
        if (a.line.size() > 0) {
            Guest g = (Guest)a.line.remove();
            g.setLocation((int)a.getEarlyExitPoint().getX(), (int)a.getEarlyExitPoint().getY());

            if (g.tracking) {
                trackingModel.addElement("Subject exited the line for " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }
            guests.add(g);
            g.setDestination(determineDestination(g));
        }
        
        if (a.fline.size() > 0) {
            Guest g = (Guest)a.fline.remove();
            g.setLocation((int)a.getEarlyExitPoint().getX(), (int)a.getEarlyExitPoint().getY());

            if (g.tracking) {
                trackingModel.addElement("Subject exited the line for " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }
            guests.add(g);
            g.setDestination(determineDestination(g));
            g.fpasses.add(new Fastpass(currentTime[0] + 1, null));
        }
     }
    
    /* 
    A method to take an individual from the ride (queue) and place them
    back on the map
    */
    public void rideExit(Attraction a) {
        if (a.ride.size() > 0) {

            Guest g = a.ride.peek();
            if (g.expTimer > a.expTime) {
                g = a.ride.remove();
                g.ridesRidden.add(a);
                g.expTimer = 0;
                g.setLocation((int)a.getExitPoint().getX(), (int)a.getExitPoint().getY());
                if (g.tracking) {
                    trackingModel.addElement("Subject experienced " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
                }
                guests.add(g);
                g.setDestination(determineDestination(g));
            }


        }
    }

    /*
    A method to take an individual from the line (queue) and put them in to the ride (queue)
    */
    public void rideLoad(Attraction a ) {
        // If the fastpass line is closed and the regular line has someone in it,
        // add that someone to the ride and open the fastpass line
        if (!a.fpswitch && a.line.size() > 0) {

            Guest g = a.line.remove();
            if (g.tracking) {
                trackingModel.addElement("Subject was loaded onto " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }

            a.ride.add(g);
            // Starts the guests expierience timer
            g.expTimer = 0;
            a.fpswitch = true;
        // If the fastpass line is open and the fastpass line has someone in it,
        // add that someone to the ride and close the fastpass line
        } else if (a.fpswitch && a.fline.size() > 0) {
            Guest g = (Guest)a.fline.remove();
            
            if (g.tracking) {
                trackingModel.addElement("Subject was loaded onto " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }

            a.ride.add(g);
            // Starts the guests expierience timer
            g.expTimer = 0;
            a.fpswitch = false;
        // If neither of the above conditions is met, and the line has someone in it,
        // add that someone to the ride
        } else if (a.line.size()>0) {
            Guest g = (Guest)a.line.remove();

            if (g.tracking) {
                trackingModel.addElement("Subject was loaded onto " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }

            a.ride.add(g);
            // Starts the guests expierience timer
            g.expTimer = 0;
        // If none of the above conditions are met, and the fastpass line has someone in it,
        // add that someone to the ride and close the fastpass line
        } else if (a.fline.size()>0) {
            Guest g = (Guest)a.fline.remove();
            
            if (g.tracking) {
                trackingModel.addElement("Subject was loaded onto " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }

            a.ride.add(g);
            // Starts the guests expierience timer
            g.expTimer = 0;
            a.fpswitch = false;    
        }
    }
    
    /**/
    public void save() {
        try {
            FileWriter writer = new FileWriter(saveFileLocation);
            for (int i = 0; i < horizontalSpaces; i++) {
            for (int j = 0; j < verticalSpaces; j++) {
                if (fills[i][j]) {
                    writer.write(i + " " + j + " T\n");
                } else {
                    writer.write(i + " " + j + " F\n");
                }
            }
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**/
    public void fill() {
        for (int i = 0; i < horizontalSpaces; i++) {
            for (int j = 0; j < verticalSpaces; j++) {
                if (bools[i][j] == true) {
                    break;
                } else {
                    fills[i][j] = true;
                    continue;
                }

            }
        }
        //panel.repaint();
        for (int i = horizontalSpaces - 1; i > 0; i--) {
            for (int j = verticalSpaces - 1; j > 0; j--) {
                if (bools[i][j] == true) {
                    break;
                } else {
                    fills[i][j] = true;
                    continue;
                }

            }
        }
        //panel.repaint();



        for (int i = 0; i < verticalSpaces; i++) {
            for (int j = 0; j < horizontalSpaces; j++) {
                if (bools[j][i] == true) {
                    break;
                } else {
                    fills[j][i] = true;
                    continue;
                }

            }
        }
        //panel.repaint();
        for (int i = verticalSpaces - 1; i > 0; i--) {
            for (int j = horizontalSpaces - 1; j > 0; j--) {
                if (bools[j][i] == true) {
                    break;
                } else {
                    fills[j][i] = true;
                    continue;
                }

            }
        }
        //panel.repaint();


    }

    /**/
    public Attraction determineDestination(Guest g) {

        Attraction destination = rides.get(0);
        int minExhaust = Integer.MAX_VALUE;
        for (Attraction a : rides) {
            if (a.closed) {
                continue;
            }
            int Exhaust = getExhaust(g, a);

            if (Exhaust < minExhaust) {
                minExhaust = Exhaust;
                destination = a;
            } 
            if (Exhaust == minExhaust) {
                Random random = new Random();
                boolean rand = random.nextBoolean();
                if (rand) {
                    minExhaust = Exhaust;
                    destination = a;
                }
            }


        }
        if (minExhaust > 20 && parkHopping) {
            leavePark(g);
            return null;
        }

        return destination;
    }
    
    /**/
    public Attraction determineDestination (Guest g, Attraction excluded) {
        Attraction destination = rides.get(0);

        int minExhaust = Integer.MAX_VALUE;
        for (Attraction a : rides) {
            if (a == excluded || a.closed) {
                continue;
            }
            int Exhaust = getExhaust(g, a);

            if (Exhaust < minExhaust) {
                minExhaust = Exhaust;
                destination = a;
            } 
            if (Exhaust == minExhaust) {
                Random random = new Random();
                boolean rand = random.nextBoolean();
                if (rand) {
                    minExhaust = Exhaust;
                    destination = a;
                }
            }
        }
        if (minExhaust > 0 && parkHopping) {
            leavePark(g);
            return null;
        }

        return destination;

    }
    
    /**/
    public int getExhaust(Guest g, Attraction a) {
        int Exhaust = 0;
            // Exhaust from waiting
            Exhaust += a.totalWaitTime;

            // Exhaust from traveling
            int distance = (int)Point2D.distance(g.location.getX(), g.location.getY(), a.getAccessPoint().getX(), a.getAccessPoint().getY());
            Exhaust += (distance / 50);

            // excuse some Exhaust if the ride is age appropriate
            if (a.targetAudience.equalsIgnoreCase(g.age)) {
                Exhaust -= 15;
            }
            
            // Exhaust from already riding the ride
            if (g.ridesRidden.contains(a)) {
                Exhaust += 100;
            }
            
            // Excuse exhaust if guest has a fast pass for the attraction at the current time
            for (Fastpass f: g.fpasses) {
                if (f.target == a && currentTime[0] == f.slot) {
                    Exhaust -= 100;
                }
            }
            
            // Excuse exhaust relative to the priority the ride has
            Exhaust -= (a.priority * 1.5);

        return Exhaust;
    }
    
    /**/
    public Guest earlyExit(Guest g) {

        g.setLocation(g.attractionDestination.getEarlyExitPoint());
        Attraction destination = determineDestination(g, g.attractionDestination);
        g.setDestination(destination);
        guests.add(g);
        return g;
    }
    
    /*
        This method redirects the guest to have the park exit as their current destination
    */
    public void leavePark(Guest g) {
        g.destination.x = getParkExitPoint().x;
        g.destination.y = getParkExitPoint().y;
        g.attractionDestination = null;
        
        
    }
    
    
    
    public void setParkExitPoint(Point p) {
        parkExitPoint.x = p.x;
        parkExitPoint.y = p.y;
    }
    
    public Point getParkExitPoint() {
        return parkExitPoint;
    }
    
    /**/
    public void endSim() {
        simEnded = true;
        reportLoc.setVisible(true);
        printReportButton.setVisible(true);
    }

    /**/
    public boolean simEnded = false;
    
    public boolean equals(Park p) {
        if (p.name == this.name) {
            return true;
        }
        return false;
    }
    
    
    /**/
    class myPanel extends JPanel {

        
        public myPanel() {
            setVisible(true);
            addMouseListener(new MouseAdapter() { 
                public void mousePressed(MouseEvent me) { 
                    int xPress = me.getX();
                    int yPress = me.getY();
                    while (xPress % blockSize != 0) {
                        xPress--;
                    }
                    while (yPress % blockSize != 0 ) {
                        yPress--;
                    }
                    int i = xPress/blockSize;
                    int j = yPress/blockSize;
                    
                    //bools[i][j] = true;
                    System.out.println(xPress + " " + yPress);
                    for (Guest g : guests) {
                        if (g.location.getX() == xPress && g.location.getY() == yPress) {
                            
                            for (Guest x : guests) {
                                x.tracking = false;
                            }
                            for (Attraction a: rides) {
                                for (Guest x: a.line) {
                                    x.tracking = false;
                                }
                                for (Guest x: a.ride) {
                                    x.tracking = false;
                                }
                                for (Guest x: a.fline) {
                                    x.tracking = false;
                                }
                            }
                            g.tracking = true;
                            ageLabel.setText("The age group of the subject is " + g.age);
                            trackingModel = new DefaultListModel();
                        }
                    }
                        
                    
                } 
            });
            
            this.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    int xPress = e.getX();
                    int yPress = e.getY();
                    while (xPress % blockSize != 0) {
                        xPress--;
                    }
                    while (yPress % blockSize != 0 ) {
                        yPress--;
                    }
                    int i = xPress/blockSize;
                    int j = yPress/blockSize;
                    
                    //fills[i][j] = true;
                    
                     
                    
                    
                    repaint();
                }
            });
    }
            
            
        
        public ArrayList<Point> expanders = new ArrayList<Point>();
        
        
        @Override
        public void paintComponent(Graphics g) {
        super.paintComponent(g);
            g.setColor(Color.black);
            g.fillRect(0, 0, 2000, 1000);   
           /*if (counter < 6500 ) {
                
                if (counter % 1 == 0) {
                    for (int i = 0; i < innerfills.length; i++) {
                        for (int j = 0; j <innerfills[i].length; j++) {
                            if (innerfills[i][j] == true) {
                                if (!bools[i][j + 1]) {
                                    innerfills[i][j+1] = true;
                                }
                                if (!bools[i + 1][j]) {
                                    innerfills[i + 1][j] = true; 
                                }
                                if (!bools[i - 1][j]) {
                                    innerfills[i - 1][j] = true;  
                                }
                                if (!bools[i][j - 1]) {
                                   innerfills[i][j-1] = true;  
                                } 
                            }
                        }
                    }  
                }   
            }*/
           
           if (counter < 6500)/*4 seconds*/ {
                for (int i = 1; i < fills.length - 1; i++) {
                    for (int j = 1; j <fills[i].length - 1; j++) {
                        if (fills[i][j] == true) {
                            if (!bools[i][j + 1] && !innerfills[i][j + 1]) {
                                fills[i][j+1] = true;
                            }
                            if (!bools[i + 1][j] && !innerfills[i + 1][j]) {
                                fills[i + 1][j] = true; 
                            }
                            if (!bools[i - 1][j] && !innerfills[i - 1][j]) {
                                fills[i - 1][j] = true;  
                            }
                            if (!bools[i][j - 1] && !innerfills[i][j - 1]) {
                               fills[i][j-1] = true;  
                            } 
                        }
                    }
                }  
            }
           
            if (counter == 10) {
               for (int i = 0; i < innerfills.length; i++) {
                    for (int j = 0; j <innerfills[i].length; j++) {
                        if (bools[i][j]) {
                            if (!bools[i][j + 1] && !innerfills[i][j+1]) {
                                grassfills[i][j+1] = true;
                            }
                            if (!bools[i + 1][j] && !innerfills[i + 1][j]) {
                                grassfills[i + 1][j] = true;
                            }
                            if (i-1 >= 0) {
                            if (!bools[i - 1][j] && !innerfills[i-1][j]) {
                                    grassfills[i - 1][j] = true;     
                                  
                            }}
                            if (j -1 >= 0) {
                            if (!bools[i][j - 1] && !innerfills[i][j-1]) {
                                
                                    grassfills[i][j - 1] = true;    
                                
                                
                            }} 
                        }
                    }
                }
            }
           
           
           
           
           
           
           

           if (!simEnded) {
           if (nicegraphics) {
           g.setColor(Color.BLACK);
           for (int i = 0; i < horizontalSpaces; i++) {
                for (int j = 0; j < verticalSpaces; j++) {
                    g.setColor(Color.BLACK);
                    //g.drawRect((int)spaces[i][j].getX(), (int)spaces[i][j].getY(), blockSize, blockSize);
                    if (i > 1 && j > 1) {
                    if (bools[i][j] && !bools[i-1][j] && !bools[i+1][j] ) {
                        g.setColor(new Color(125, 112, 186));
                        g.fillRect((int)spaces[i][j].getX() + 2, (int)spaces[i][j].getY(), blockSize - 4, blockSize); 
                        g.setColor(Color.white);
                        g.fillRect((i * 10) + 3, (j * 10) + 3, 3, 3);
                    } else if(bools[i][j] && !bools[i][j - 1] && !bools[i+1][j + 1] ) {
                        g.setColor(new Color(125, 112, 186));
                        g.fillRect((int)spaces[i][j].getX(), (int)spaces[i][j].getY() + 2, blockSize, blockSize - 4); 
                        g.setColor(Color.white);
                        g.fillRect((i * 10) + 3, (j * 10) + 3, 3, 3);
                    } else if (bools[i][j] && bools[i][j - 1] && bools[i+1][j + 1] && bools[i-1][j] && bools[i+1][j]  ) {
                        g.setColor(new Color(125, 112, 186));
                        g.fillRect((int)spaces[i][j].getX(), (int)spaces[i][j].getY(), blockSize, blockSize); 
                    } else if (bools[i][j]) {
                        // Color of the walls
                        g.setColor(new Color(125, 112, 186));
                        g.fillRect((int)spaces[i][j].getX(), (int)spaces[i][j].getY(), blockSize, blockSize); 
                        g.setColor(Color.black);
                        g.drawLine((i * 10), (j * 10) + 3, (i*10) + 10, (j * 10) + 3);
                        g.drawLine((i * 10), (j * 10) + 6, (i*10) + 10, (j * 10) + 6);
                        g.drawLine((i * 10) + 3, (j * 10), (i * 10) + 3, (j*10) + 10);
                        g.drawLine((i * 10) + 6, (j * 10), (i * 10) + 6, (j*10) + 10);
                        g.setColor(Color.white);
                        g.fillRect((i * 10) + 3, (j * 10) + 3, 3, 3);
                        g.setColor(Color.black);
                        g.drawRect((int)spaces[i][j].getX(), (int)spaces[i][j].getY(), blockSize, blockSize);
                    }
                    }
                    if (fills[i][j]) {
                        
                        if (loc < 0) {
                            loc = 0;
                        }
                        if (backColors.size() > 0) {
                            g.setColor(backColors.get(loc));
                            g.fillRect((int)spaces[i][j].getX(), (int)spaces[i][j].getY(), blockSize, blockSize);     
                        }
                         
                    }
                    if (grassfills[i][j]) {
                        g.setColor(new Color(95, 173, 65));
                        g.fillOval((int)spaces[i][j].getX(), (int)spaces[i][j].getY(), blockSize, blockSize); 
                    }
                    if (innerfills[i][j] == true) { 
                       g.setColor(new Color(162, 214, 249));
                       g.fillRect((int)spaces[i][j].getX(), (int)spaces[i][j].getY(), blockSize, blockSize); 
                   }
                }
            }
        
           //Paint grass in center
           g.setColor(new Color(95, 173, 65));
           g.fillOval(700, 350, 80, 80);
           g.setColor(Color.black);
           g.drawOval(700, 350, 80, 80);
           g.setColor(Color.white);
           g.fillOval(710, 360, 60, 60);
           g.setColor(Color.black);
           g.drawOval(710, 360, 60, 60);
           g.fillOval(730,380,20,20);
           g.drawLine(740, 350, 740, 430);
           g.drawLine(700,390,780,390);
           // Paint Castle
           g.setColor(Color.pink);
           g.fillRect(670, 250, 140, 60);
           g.fillRect(698, 205, 84, 45);
           g.fillRect(740, 175, 42, 30);
           
           g.setColor(Color.black);
           g.fillRect(720, 280, 40, 30);
           g.fillOval(720, 260, 40, 40);
           }
           g.setColor(Color.blue);
           //g.fillPolygon(xPoints, yPoints, WIDTH);
            for (Attraction a : rides) {
                
                
                Point p = a.getAccessPoint();
                int xPress = (int)p.getX();
                int yPress = (int)p.getY();
                    while (xPress % blockSize != 0) {
                        xPress--;
                    }
                    while (yPress % blockSize != 0 ) {
                        yPress--;
                    }
                
                
                
                Point p2 = a.getExitPoint();
                int xPress2 = (int)p2.getX();
                int yPress2 = (int)p2.getY();
                while (xPress2 % blockSize != 0) {
                    xPress2--;
                }
                while (yPress2 % blockSize != 0 ) {
                    yPress2--;
                }
                
                Point p3 = a.earlyExitPoint;
                int xPress3 = (int)p3.getX();
                int yPress3 = (int)p3.getY();
                while (xPress3 % blockSize != 0) {
                    xPress3--;
                }
                while (yPress3 % blockSize != 0 ) {
                    yPress3--;
                }
                
                g.setColor(Color.white);
                g.fillRect(xPress, yPress, 60, 60);
                if (!labelsShowing) {
                    g.setColor(Color.black);
                    g.drawRect(xPress, yPress, 60, 60);
                }
                
                
                if (labelsShowing) {
                    a.draw(g);    
                }
                
                
                g.setColor(Color.red);
                g.fillRect(xPress, yPress, blockSize, blockSize);   
                g.setColor(Color.yellow);
                g.fillRect(xPress2, yPress2, blockSize, blockSize);
                g.setColor(Color.lightGray);
                g.fillRect(xPress3, yPress3, blockSize, blockSize);
                
            }
            
            
            for (Guest t : guests) {
                    t.draw(g);   
            }
            
            if (nicegraphics) {
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
            distance = ((double)hoursWorthDistance * ((double)dailySecondsSurpassed / 3600)) - ((double)7 * hoursWorthDistance);
          
            
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
            for (Attraction a: rides) {
                if (a.closed) {
                    g.setColor(Color.red);
                    float f=40.0f; // font size.
                    g.setFont(g.getFont().deriveFont(f));
                    g.drawString(("X"), x , y + 10);
                }
            }
            
            }
            
           } else {
               simEndedArea.setVisible(true);
               this.setBackground(Color.black);
               this.setOpaque(true);
               newDayButton.setVisible(true);
           }
            
            

                       
           

        }
        public int[][] colors;
        public ArrayList<Color> backColors = new ArrayList<>();
        
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

}
    
    public int getJoy(Guest g) {
        return 0;
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        shownList = new javax.swing.JList<>();
        ageLabel = new javax.swing.JLabel();
        shownPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        simEndedArea = new javax.swing.JTextArea();
        newDayButton = new javax.swing.JButton();
        printReportButton = new javax.swing.JButton();
        reportLoc = new javax.swing.JTextField();
        graphics = new javax.swing.JButton();
        logPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        shownTrackerList = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        shownTimeLabel = new javax.swing.JLabel();
        pauseButton = new javax.swing.JButton();
        labelButton = new javax.swing.JButton();
        b30x = new javax.swing.JRadioButton();
        b15x = new javax.swing.JRadioButton();
        b5x = new javax.swing.JRadioButton();
        b1x = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        fastpassTA = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        shownList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(shownList);

        ageLabel.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        ageLabel.setText("ageLabel");

        javax.swing.GroupLayout statsPanelLayout = new javax.swing.GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsPanelLayout.createSequentialGroup()
                .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(ageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 22, Short.MAX_VALUE))
        );
        statsPanelLayout.setVerticalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        shownPanel = panel;

        simEndedArea.setColumns(20);
        simEndedArea.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        simEndedArea.setLineWrap(true);
        simEndedArea.setRows(5);
        simEndedArea.setText("The simulation has ended. The stats can be viewed on the right. If you would like to start a new day, click the button below.");
        simEndedArea.setWrapStyleWord(true);
        jScrollPane3.setViewportView(simEndedArea);

        newDayButton.setText("Start a new day");
        newDayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newDayButtonActionPerformed(evt);
            }
        });

        printReportButton.setText("Print Report");
        printReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printReportButtonActionPerformed(evt);
            }
        });

        reportLoc.setText("Type Destination for Printed Report here before pressing");

        graphics.setText("Graphics for Performance");
        graphics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphicsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout shownPanelLayout = new javax.swing.GroupLayout(shownPanel);
        shownPanel.setLayout(shownPanelLayout);
        shownPanelLayout.setHorizontalGroup(
            shownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shownPanelLayout.createSequentialGroup()
                .addComponent(graphics, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(shownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, shownPanelLayout.createSequentialGroup()
                        .addComponent(reportLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(newDayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(377, Short.MAX_VALUE))
        );
        shownPanelLayout.setVerticalGroup(
            shownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shownPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(shownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(graphics))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(shownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newDayButton)
                    .addComponent(reportLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printReportButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        shownTrackerList.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        shownTrackerList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(shownTrackerList);

        javax.swing.GroupLayout logPanelLayout = new javax.swing.GroupLayout(logPanel);
        logPanel.setLayout(logPanelLayout);
        logPanelLayout.setHorizontalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addContainerGap())
        );
        logPanelLayout.setVerticalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
        );

        shownTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        shownTimeLabel.setText("timeLabel");

        pauseButton.setText("||");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        labelButton.setText("Hide Labels");
        labelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labelButtonActionPerformed(evt);
            }
        });

        b30x.setText("30x");
        b30x.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b30xActionPerformed(evt);
            }
        });

        b15x.setText("15x");
        b15x.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b15xActionPerformed(evt);
            }
        });

        b5x.setText("5x");
        b5x.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b5xActionPerformed(evt);
            }
        });

        b1x.setText("1x");
        b1x.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1xActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(shownTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(pauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(labelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(b1x)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b5x)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b15x)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b30x)
                        .addGap(14, 14, 14))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shownTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pauseButton)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b30x)
                    .addComponent(b15x)
                    .addComponent(b5x)
                    .addComponent(b1x))
                .addGap(45, 45, 45)
                .addComponent(labelButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fastpassTA.setColumns(20);
        fastpassTA.setRows(5);
        jScrollPane4.setViewportView(fastpassTA);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shownPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(logPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(shownPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public int loc = 0;
    private void newDayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newDayButtonActionPerformed

        
    }//GEN-LAST:event_newDayButtonActionPerformed

    private void labelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelButtonActionPerformed
        labelsShowing = !labelsShowing;
    }//GEN-LAST:event_labelButtonActionPerformed
 
    private void printReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReportButtonActionPerformed
        try {
            //
            FileWriter myWriter = new FileWriter(reportLoc.getText());
            //
            myWriter.write("Time     ");
            for (Attraction a: rides) {
                String printString = a.name;
                printString = printString.substring(0, 5) + ".";
                while (printString.length() < 9) {
                    printString += " ";
                }
                myWriter.write(printString);
            }
            myWriter.write("\n");
            for (ArrayList<Integer> list : entries) {
                
                for (Integer i: list) {
                    String str = "" + i;
                    while (str.length() < 9) {
                        str += " ";
                    }
                    myWriter.write(str);
                }
                myWriter.write("\n");
            }
            
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            JOptionPane.showMessageDialog(null, "Saved","Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot Find Valid File Location","ERROR", JOptionPane.ERROR_MESSAGE);
        } 
        
        try {
            FileWriter myWriter = new FileWriter("tracker.txt");
            myWriter.write("Tracker Guest for " + reportLoc.getText() + "\n");
            for (int i = 0; i < trackingModel.size(); i++) {
                int num = i + 1;
                myWriter.write(num + ". ");
                myWriter.write((String)trackingModel.get(i));
                myWriter.write("\n");
                
            }
            myWriter.close();
                    
        } catch(Exception E) {
            E.printStackTrace();
        }
    }//GEN-LAST:event_printReportButtonActionPerformed

    private void graphicsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphicsActionPerformed
        nicegraphics = !nicegraphics;
    }//GEN-LAST:event_graphicsActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void b1xActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1xActionPerformed
        tmr = tmr.changeTimer(1);
    }//GEN-LAST:event_b1xActionPerformed

    private void b5xActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b5xActionPerformed
       tmr = tmr.changeTimer(5);
    }//GEN-LAST:event_b5xActionPerformed

    private void b15xActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b15xActionPerformed
        tmr = tmr.changeTimer(15);
    }//GEN-LAST:event_b15xActionPerformed

    private void b30xActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b30xActionPerformed
        tmr = tmr.changeTimer(30);
    }//GEN-LAST:event_b30xActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel ageLabel;
    private javax.swing.JRadioButton b15x;
    private javax.swing.JRadioButton b1x;
    private javax.swing.JRadioButton b30x;
    private javax.swing.JRadioButton b5x;
    public javax.swing.JTextArea fastpassTA;
    private javax.swing.JButton graphics;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton labelButton;
    private javax.swing.JPanel logPanel;
    public javax.swing.JButton newDayButton;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton printReportButton;
    private javax.swing.JTextField reportLoc;
    private javax.swing.JList<String> shownList;
    private javax.swing.JPanel shownPanel;
    public javax.swing.JLabel shownTimeLabel;
    private javax.swing.JList<String> shownTrackerList;
    public javax.swing.JTextArea simEndedArea;
    private javax.swing.JPanel statsPanel;
    // End of variables declaration//GEN-END:variables

}