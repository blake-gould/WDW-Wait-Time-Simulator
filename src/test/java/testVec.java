/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Vector;
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
public class testVec {
    
    public testVec() {
           Vector x = new Vector();
          Vector y = new Vector();
          x.add(0);
          x.add(1);
          x.add(2);
          y.add("String");
          y.add("String");
          y.add("Another");
          Vector z = union(x, y);

        assertTrue(z.toString() == "[0, 1, 2, String, String, Another]");
        
    }
    
    public static Vector union(Vector a, Vector b) {
        Vector<Object> v = new Vector<Object>();
	for (Object o : a) {
		v.add(o);
	}
	for (Object o : b) {
		if (!v.contains(o)) {
			v.add(o);
		}
	}
	return v;
}
    


    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
