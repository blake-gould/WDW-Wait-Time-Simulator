/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package essentials;


import other.myImage;
import timers.Task;
import mains.Display;
import timers.parkTimer;
import timers.parkTimerTask;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blake
 */
public class Park {
    
    public JLabel shownTimeLabel = new JLabel();
    public JLabel ageLabel = new JLabel();
    
    public int dailySecondsSurpassed = 0;
    public Integer[] currentTime = {0,0,0};
    public JLabel timeLabel = new JLabel();
    public JPanel dp = new JPanel();
    
    public Display d; 
    
    public boolean parkHopping = true;
    
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
            
    public ArrayList<Family> Familys = new ArrayList<>();
    public ArrayList<Family> Familysmaster = new ArrayList<>();
    public ArrayList<Fastpass> fastpasses = new ArrayList<>();
    
    public JList list = new JList();

    public DefaultListModel trackingModel = new DefaultListModel();
    
    public boolean running = true;
    
    public parkTimer tmr;
    
    public boolean nicegraphics = true;
    
    public boolean statsShowing = false;
    public boolean trackingShowing = false;
    
    public JPanel panel;
    public Family Family = new Family("");
    public String boolsfile; public String name;
    public Color bgcolor;
    public ArrayList<Point> colorstarters;
    public myImage image;
    public int parknum;
    
    public JTextArea fastpassTA = new JTextArea();
    
    public ArrayList<Family> familysToAdd = new ArrayList<>();
    
    public Park(String name, ArrayList<Attraction> rides, String boolsfile, Color bgcolor, myImage image, ArrayList<Point> colorstarters, Point p, int parknum)  {
        this.boolsfile = boolsfile;
        this.rides = rides;
        this.name = name;
        this.bgcolor = bgcolor;
        this.colorstarters = colorstarters;
        this.image = image;
        this.parknum = parknum;
        createBoard();
        colorBoardLoad();
        configureRides();
        configureFamilys();
        configureFastpasses();
        configureFrameAndPanel();
        

        ArrayList<parkTimerTask> tasks = new ArrayList<>();
        
        // Creates and schedules a timer task for 
        // the addition of each group of people
        int delay = 0;
        for (int i = 0; i < 4; i ++) {
            delay += 25000;
            Runnable x = new Runnable() {
                @Override
                public void run() {
                     for (int x = 130; x <= 1280; x += 10) {
                        for (int y = 830; y <= 870; y += 10) {
                            Random random = new Random();
                            int rand = random.nextInt(3);
                            Family Family = new Family("Adult");
                            switch (rand) {
                                case 0:
                                    Family = new Family("Child");
                                    break;
                                case 1:
                                    Family = new Family("Adult");
                                    break;
                                case 2:
                                    Family = new Family("Young Adult");
                                    break;
                                default:
                                    break;
                            }
                            Family.setLocation(x, y);
                            Family.setDestination(determineDestination(Family));
                            Family.tracking = false;
                            Familys.add(Family);
                            Familysmaster.add(Family); 
                        }
                    }
                }
            };
            Task addMoreFamilys = new Task(x);
            parkTimerTask task1 = new parkTimerTask(addMoreFamilys, delay, x);
            tasks.add(task1);
        }
        //SPED UP SPED UP SPED UP SPED UP SPED UP SPED UP
        // Calls the method to assign fastpasses and then displays the tracking panel.
        Runnable x = new Runnable() {
            @Override
            public void run() {
                assignFastpasses();
                //ageLabel.setVisible(true);
                //fastpassTA.setVisible(true);
                //shownTrackerList.setVisible(true);
                
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
                        rideLoad(a);    
                    }
                    
                }
            };
            Task newTask = new Task(temp);
            
            parkTimerTask task3 = new parkTimerTask(newTask,0, (a.outFlowTime), temp);
            tasks.add(task3);
        }
        
        // Calls method FamilyMove every 50 milliseconds
        Runnable temp = new Runnable() {
            @Override
            public void run() {
                if (running) {
                    FamilyMove();   
                    for (Attraction a : rides) {
                        rideExit(a);
                    }
                }
            }
        };
        
        Task FamilyMove = new Task(temp); 
        parkTimerTask task4 = new parkTimerTask(FamilyMove, 0, 50, temp);
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
       
       // Ensure that the point given for parkexit is a valid point for the grid
       int px =(int) p.getX();
       int py = (int) p.getY();
       while (px % 10 != 0) {
           px--;
       }
       while (py % 10 != 0) {
           py--;
       }
       Point p2 = new Point(px,py);
       setParkExitPoint(p2);
   
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
    This arraylist is iterated over often
    The rides are added one time each
    */
    public void configureRides() {
        
        
        for (Attraction r: rides) {
            r.fpswitch = false;
        }
    }
    
    /*
    Adds the first round of Familys including 1 additional Family
    This additional Family is the first tracked Family and has a 
    preset initial destination
    */
    public void configureFamilys() {
        // ADD THE Initial FamilyS
        for (int x = 130; x <= 1280; x += 10) {
            for (int y = 830; y <= 870; y += 10) {
                Random random = new Random();
                int rand = random.nextInt(3);
                Family Family = new Family("Adult");
                switch (rand) {
                    case 0:
                        Family = new Family("Child");
                        break;
                    case 1:
                        Family = new Family("Adult");
                        break;
                    case 2:
                        Family = new Family("Young Adult");
                        break;
                    default:
                        break;
                }
                Family.setLocation(x, y);
                Family.setDestination(determineDestination(Family));
                Family.tracking = false;
                Familys.add(Family);
                Familysmaster.add(Family); 
            }
        }
        
        /*
        The first Family that is tracked is personally set
        */
        Family = new Family("Adult");
        Family.setLocation(100, 430);
        Family.tracking = false;
        Family.setDestination(determineDestination(Family));
        Familys.add(Family);
        
        Familysmaster.add(Family);
    }
    
    /*
    * Sorts the attractions and then creates FP equal to 3 times the number of 
    * subjects in the park. The algorithm then creates vatious fastpasses for 
    * each attraction during every hour intercval during the park day
    */
    public void configureFastpasses() {
        sortAttractions();
        int totalNumFP = Familys.size() * 9;
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
    
    /*The method iterates through the Familys three times and the fastpasses arraylist once
    * Assigning the fastpasses to the Familys. Prior to this, the fastpasses are shuffled at random.
    */
    public void assignFastpasses() {
        Collections.shuffle(fastpasses);
        int fastPassIndex = 0;
        for (int i=0; i < 3; i++) {
            for (Family fam: Familysmaster) {
                if (fastPassIndex < fastpasses.size()) {
                    fam.fpasses.add(i,fastpasses.get(fastPassIndex));
                    fastPassIndex++;
                }
            }    
        }
        
    }
    
    public DefaultListModel model = new DefaultListModel();
    
    /**/
    public void configureFrameAndPanel() {
        
        shownTimeLabel = new JLabel();
        // Sets the starting time tp 7 o'clock
        dailySecondsSurpassed += (7 * 3600);  

        
        model = new DefaultListModel();
        for (Attraction a : rides) {
            model.addElement(a.toString());
        }



        // IMPORTANT FOR PAUSE BUTTON
        
//        pauseButton.addActionListener((e) -> {
//            running = !running;
//        });
        
        
        // MUSIC
        
        /*try {
                Clip clip = AudioSystem.getClip();
                File file = new File("C:\\Users\\blake\\Desktop\\Piano Sounds\\Main Street USA Area Music - Loop.wav");
                clip.open(AudioSystem.getAudioInputStream(file));
                clip.start();


            }catch(Exception e) {
                e.printStackTrace();
        }*/     
        
        
    }
    
    /*
    Determines if a Family should move or not. It also determines
    if they go up, left, right, down when they move. It also determines
    If they enter a line queue*/
    // "occupied" currently has no function
    public int counter;
    public void FamilyMove() {
        
        for (Family g: familysToAdd) {
            Familys.add(g);
        }
        familysToAdd = new ArrayList<>();
        
        ArrayList<Family> familysToDetermineDestination = new ArrayList<>();
        for (Family g: Familys) {
            if (g.transferred) {
                familysToDetermineDestination.add(g);
                g.transferred = false;
            }
        }
        
        for (Family g: familysToDetermineDestination) {
            determineDestination(g);
        }
        
        familysToDetermineDestination = new ArrayList<>();
        familysToDetermineDestination.clear();
        
        counter++;
        // Called every three minutes, checks to see if anyone has completed all the rides
        // If so, their ride history gets wiped
        if (counter % 3600 == 0) {
            for (Family g: Familys) {
                if (g.ridesRidden.size() == rides.size()) {
                    for (Attraction a: rides) {
                        g.ridesRidden.remove(a);
                    }
                }  
            }
            for (Attraction b : rides) {
                for (Family g: b.line) {
                    if (g.ridesRidden.size() == rides.size()) {
                        for (Attraction a: rides) {
                            g.ridesRidden.remove(a);
                        }
                    }
                } 
                for (Family g: b.fline) {
                    if (g.ridesRidden.size() == rides.size()) {
                        for (Attraction a: rides) {
                            g.ridesRidden.remove(a);
                        }
                    }
                }
                for (Family g: b.ride) {
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
        
        // If the Family is on a ride, increase the time they've been on that ride
        for (Attraction a: rides) {
            for (Family g: a.ride) {
                g.expTimer += 0.00083333333333333333333333333333333333333333333333333333;   
            }
            // If the Family is in line and no other Familys have exited this movement,
            // If the exaust is 15 less at some other attraction, the Family early exits
            // the line and goes to that new location. The Family will not early exit from a fastpass line
            for (Family g: a.line) {
                if (!oneExiting) {
                    Attraction curr = g.attractionDestination;
                    boolean foundARideableRide = false;
                    for (Attraction a2 : rides) {
                        if (curr == null) {
                            determineDestination(g, g.attractionDestination);
                            break;
                        } else if (getExhaust(g, curr) - getExhaust(g, a2) > 15 && !a2.closed) {

                            curr.line.remove(g);
                            g = earlyExit(g);
                            oneExiting = true;
                            if (g.tracking) {
                                trackingModel.addElement("The subject changed lines (" + currentTime[0] + ":" + currentTime [1] + ")");
                            }
                            break;

                        } else if (getExhaust(g,a2) < 100) {
                            foundARideableRide = true;
                        }
                    }
                    if (!foundARideableRide) {
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
        
        ArrayList<Family> Familystoremove = new ArrayList<>();
        
        // When the Family reaches their detsination, add them to the appropriate line queue (Fastpass)
        for (Family g : Familys) {
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
                        Familystoremove.add(g);
                        break;
                    }
                    }
                }
            }
            }
        }
        
        for (Family g: Familystoremove) {
            Familys.remove(g);
        }
        Familystoremove.clear();
          
        // When the Family reaches their detsination, add them to the appropriate line queue (Regular Line)   
        for (Family g : Familys) {   
            if (g.attractionDestination != null) {
            if (g.location.getX() == g.destination.getX() && g.location.getY() == g.destination.getY()) {
                if (g.attractionDestination.addToQueue(g)) {
                    if (g.tracking) {
                        trackingModel.addElement("The subject began waiting in line for " + g.attractionDestination.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");    
                    }
                    Familystoremove.add(g);   
                }    
            }
            }
        }
        
        for (Family g: Familystoremove) {
            Familys.remove(g);
        }
        Familystoremove.clear();
        
        for (Family g: Familys) {
            if(g.location.x == parkExitPoint.x && g.location.y == parkExitPoint.y && g.destination.x == parkExitPoint.x && g.destination.y == parkExitPoint.y) {
                Familystoremove.add(g);
                d.hubFamiliesToAdd.add(g);  
                g.transferred = true;
            }
        }
        
        for (Family g: Familystoremove) {
            Familys.remove(g);
        }
        Familystoremove.clear();

        for (Family g : Familys) {
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
            for (Family g: a.line) {
                if (g.tracking) {
                String str = "Fastpasses:\n";
                for (Fastpass f: g.fpasses) {
                    str += f.toString() + " \n";
                }
                fastpassTA.setText(str);
                }
            } 
            for (Family g: a.ride) {
                if (g.tracking) {
                String str = "Fastpasses:\n";
                for (Fastpass f: g.fpasses) {
                    str += f.toString() + " \n";
                }
                fastpassTA.setText(str);
                }
            }
            for (Family g: a.fline) {
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
        // Evacuate a Family from the ride
        if (a.ride.size() > 0) {

                Family g = (Family) a.ride.remove();
                g.expTimer = 0;
                g.setLocation((int)a.getExitPoint().getX(), (int)a.getExitPoint().getY());

                if (g.tracking) {
                    trackingModel.addElement("Subject evacuated " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
                }
                Familys.add(g);
                g.setDestination(determineDestination(g));   
        }

        // Remove a Family from the line
        if (a.line.size() > 0) {
            Family g = (Family)a.line.remove();
            g.setLocation((int)a.getEarlyExitPoint().getX(), (int)a.getEarlyExitPoint().getY());

            if (g.tracking) {
                trackingModel.addElement("Subject exited the line for " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }
            Familys.add(g);
            g.setDestination(determineDestination(g));
        }
        
        if (a.fline.size() > 0) {
            Family g = (Family)a.fline.remove();
            g.setLocation((int)a.getEarlyExitPoint().getX(), (int)a.getEarlyExitPoint().getY());

            if (g.tracking) {
                trackingModel.addElement("Subject exited the line for " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }
            Familys.add(g);
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

            Family g = a.ride.peek();
            if (g.expTimer > a.expTime) {
                g = a.ride.remove();
                g.ridesRidden.add(a);
                g.expTimer = 0;
                g.setLocation((int)a.getExitPoint().getX(), (int)a.getExitPoint().getY());
                if (g.tracking) {
                    trackingModel.addElement("Subject experienced " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
                }
                Familys.add(g);
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

            Family g = a.line.remove();
            if (g.tracking) {
                trackingModel.addElement("Subject was loaded onto " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }

            a.ride.add(g);
            // Starts the Familys expierience timer
            g.expTimer = 0;
            a.fpswitch = true;
        // If the fastpass line is open and the fastpass line has someone in it,
        // add that someone to the ride and close the fastpass line
        } else if (a.fpswitch && a.fline.size() > 0) {
            Family g = (Family)a.fline.remove();
            
            if (g.tracking) {
                trackingModel.addElement("Subject was loaded onto " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }

            a.ride.add(g);
            // Starts the Familys expierience timer
            g.expTimer = 0;
            a.fpswitch = false;
        // If neither of the above conditions is met, and the line has someone in it,
        // add that someone to the ride
        } else if (a.line.size()>0) {
            Family g = (Family)a.line.remove();

            if (g.tracking) {
                trackingModel.addElement("Subject was loaded onto " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }

            a.ride.add(g);
            // Starts the Familys expierience timer
            g.expTimer = 0;
        // If none of the above conditions are met, and the fastpass line has someone in it,
        // add that someone to the ride and close the fastpass line
        } else if (a.fline.size()>0) {
            Family g = (Family)a.fline.remove();
            
            if (g.tracking) {
                trackingModel.addElement("Subject was loaded onto " + a.name + " (" + currentTime[0] + ":" + currentTime [1] + ")");
            }

            a.ride.add(g);
            // Starts the Familys expierience timer
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
            
        } catch (IOException e) {
            
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
    public Attraction determineDestination(Family g) {

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
        if (minExhaust > 100 && parkHopping) {
            leavePark(g);
            return null;
        }

        return destination;
    }
    
    /**/
    public Attraction determineDestination (Family g, Attraction excluded) {
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
        if (minExhaust > 100 && parkHopping) {
            leavePark(g);
            return null;
        }

        return destination;

    }
    
    /**/
    public int getExhaust(Family g, Attraction a) {
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
            
            // Excuse exhaust if Family has a fast pass for the attraction at the current time
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
    public Family earlyExit(Family g) {
        g.setLocation(g.attractionDestination.getEarlyExitPoint());
        Attraction destination = determineDestination(g, g.attractionDestination);
        g.setDestination(destination);
        Familys.add(g);
        return g;
    }
    
    /*
        This method redirects the Family to have the park exit as their current destination
    */
    public void leavePark(Family g) {
        g.destination.x = getParkExitPoint().x;
        System.out.print(getParkExitPoint().x + " , " + getParkExitPoint().y);
        g.destination.y = getParkExitPoint().y;
        g.attractionDestination = null;
        g.oldPark = parknum;
        g.oldP = this;
        
        
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
    }

    /**/
    public boolean simEnded = false;
    
    public boolean equals(Park p) {
        if (p.name == this.name) {
            return true;
        }
        return false;
    }
    
    public int getParkExaust(Family g) {
        int returnvalue = 0;
        int count = 0;
        for (Attraction a: rides) {
            if (!a.closed) {
                returnvalue += getExhaust(g, a);    
                count++;
            }
        }
        
        return returnvalue / count;
    }
}