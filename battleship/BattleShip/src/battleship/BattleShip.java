/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import javax.swing.JFrame;

/**
 *
 * @author ftonye
 */
public class BattleShip {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        BattleSea bs = new BattleSea();
        bs.setSize(500,400);
        bs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bs.setVisible(true);
        
        // TODO code application logic here
    }
    
}
