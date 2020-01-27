/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * @author ftonye
 */
public class Ship {

    public ArrayList<JButton> BoatSections;

    private int status;
    private int nbHit;

    public Ship(ArrayList<JButton> BoatSection) {
        this.BoatSections = new ArrayList<JButton>();
        this.BoatSections = BoatSection;
        this.status = 0;
        this.nbHit = 0;
        initShip();
    }

    public int getStatus() {
        return this.status;
    }

    public void initShip() {

        for (JButton section : this.BoatSections) {

            section.setBackground(java.awt.Color.green);

        }

    }

    public int checkHit(JButton cible) {

        for (JButton section : this.BoatSections) {
            if (section.equals(cible)) {
                this.nbHit++;
                if (nbHit == this.BoatSections.size()) {
                    for (JButton section2 : this.BoatSections) {
                        section2.setBackground(java.awt.Color.black);
                    }
                    return 3;
                } else {
                    cible.setBackground(java.awt.Color.red);
                    return 1;
                }
            }

        }
        return 0;

    }

}
