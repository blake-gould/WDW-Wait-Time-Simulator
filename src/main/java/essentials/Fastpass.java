package essentials;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blake
 */
public class Fastpass {
    public int slot;
    public Attraction target;
    
    public Fastpass(int slot, Attraction target) {
        this.slot = slot;
        this.target = target;
    }
    
    @Override
    public String toString() {
        if (target == null) {
            return "(" +  "Magic Pass" + ", " + slot + ")";    
        }
        return "(" +  target.name + ", " + slot + ")";
    }
}
