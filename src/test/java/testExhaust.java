/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import essentials.Attraction;
import essentials.Guest;
import java.awt.Color;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author blake
 */
public class testExhaust {
    
    public testExhaust() {
        formPaths(a1, a2, a3);
    }
    
    public class Node {
        public Node next;
        public Attraction value;
        
        public Node(Attraction value) {
            this.next = null;
            this.value = value;
        }
        
        public void next(Node next) {
            this.next = next;
        }
    }
    
    public ArrayList<Node> formPaths(Attraction a, Attraction b, Attraction c) {
        Node()
    }
    
    Guest g = new Guest("Adult");
    Attraction a1 = new Attraction("a1", 2000, "Child", 8, 10, 0, 0, Color.yellow);
    Attraction a2 = new Attraction("a2", 4000, "Young Adult", 6, 20, 0, 0, Color.yellow);
    Attraction a3 = new Attraction("a3", 6000, "Adult", 4, 5, 0, 0, Color.yellow);
    
}
