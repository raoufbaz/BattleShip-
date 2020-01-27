/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import com.sun.prism.paint.Color;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JTextField;

import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;

/**
 *
 * @author ftonye
 */
public class BattleSea extends JFrame {

    private static JButton[] listbtn;
    private static ArrayList<Ship> listShip;
    private static ArrayList<JButton> listJButtonInShip;
    private ArrayList<Integer> Usedbuttons; //contient la liste des boutons deja utilisee
    private ArrayList<Integer> Contour; //contient la liste des boutons autour du bateau

    private InetAddress adrs;

    private JButton btnStartServer;
    private JButton btnStartClient;
    private JTextField txtPortNumber;
    private JTextField txtPortclient;
    private JTextField txtIPclient;
    private BackGroundCom com;
    private Thread t;
    private JRadioButton serverbtn;
    private JRadioButton clientbtn;

    private int status = 0;
    private int retour = 0;

    public BattleSea() {
        this.setSize(500, 425);
        try {
            adrs = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(BattleSea.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setTitle("BattleSea Server address is: " + adrs.getHostAddress());
        this.setLayout(new GridLayout(11, 11));//gridlayout

        createGrid();//******************************//
        linkListenerToSeaSector();
        //client
        this.add(clientbtn);
        this.add(txtPortclient);
        this.add(txtIPclient);
        this.add(btnStartClient);

        //server
        this.add(serverbtn);
        this.add(txtPortNumber);
        this.add(btnStartServer);

        CreateShips();

    }

    //**********************************//
    private void createGrid() {
        listbtn = new JButton[100];

        for (int i = 0; i < listbtn.length; i++) {
            listbtn[i] = new JButton(String.valueOf(i));
            listbtn[i].setSize(10, 10);
            listbtn[i].setBackground(java.awt.Color.blue);
            this.add(listbtn[i]);

        }
        //radio button client 
        clientbtn = new JRadioButton("client");
        clientbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                serverbtn.setSelected(false);
                clientbtn.setSelected(true);
                txtPortNumber.setVisible(false);
                btnStartServer.setVisible(false);
                txtPortclient.setVisible(true);
                txtIPclient.setVisible(true);
                btnStartClient.setVisible(true);
            }
        });
        //radio button server
        serverbtn = new JRadioButton("server");
        serverbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                serverbtn.setSelected(true);
                clientbtn.setSelected(false);

                txtPortNumber.setVisible(true);
                btnStartServer.setVisible(true);
                txtPortclient.setVisible(false);
                txtIPclient.setVisible(false);
                btnStartClient.setVisible(false);
            }

        });
        //button Start Server
        btnStartServer = new JButton("Start Server");
        btnStartServer.setVisible(false);
        btnStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                com = new BackGroundCom(Integer.parseInt(txtPortNumber.getText()));
                t = new Thread(com);
                t.start();
            }
        });
        //button Start Client
        btnStartClient = new JButton("Start Client");
        btnStartClient.setVisible(false);
        btnStartClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) { //add runnable or condition pour client
                com = new BackGroundCom(Integer.parseInt(txtPortclient.getText()), txtIPclient.getText());
                t = new Thread(com);
                t.start();
            }

        });

        txtPortNumber = new JTextField();
        txtPortNumber.setVisible(false);
        txtPortNumber.setText("23000");

        txtPortclient = new JTextField();
        txtPortclient.setVisible(false);
        txtPortclient.setText("23000");

        txtIPclient = new JTextField();
        txtIPclient.setVisible(false);
        txtIPclient.setText("127.0.0.1");
    }

    private void linkListenerToSeaSector() {
        for (int i = 0; i < listbtn.length; i++) {
            listbtn[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {

                    com.cible = Integer.parseInt(ae.getActionCommand());
                    com.dataToSend = true;

                }
            });
        }

    }

    private void CreateShips() {
        int head = 0, nb = 0, orientation = 0;
        Random r = new Random();
        listShip = new ArrayList<Ship>(); //contient tout les ship
        listJButtonInShip = new ArrayList<JButton>(); //contient tout les boutons de tt les ship
        Usedbuttons = new ArrayList<Integer>();
        Contour = new ArrayList<Integer>();
        boolean contourCollision = false;
        boolean ready = false;
        boolean Debordementsafe = false;
        boolean ContourSafe = false;

        for (int i = 0; i < 5; i++) //cree 5 bateaux
        {
            ArrayList<JButton> boatsection = new ArrayList<JButton>(); //contient tt les boutons dun ship
            while (ready == false) {//pret a creer un ship

                while (Debordementsafe == false) {
                    do {
                        nb = (int) (Math.random() * ((5 - 2) + 1)) + 2;
                        orientation = r.nextInt(2) + 1; //if ==1 horizental,2== verticale

                        if (orientation == 1) {
                            do {
                                head = r.nextInt(93) + 1; //premiere case du bateau [1,98]
                            } while ((head > 5 && head < 10 || head > 15 && head < 20 || head > 25 && head < 30 || head > 35 && head < 40 || head > 45 && head < 50 || head > 55 && head < 60 || head > 65 && head < 70 || head > 75 && head < 80 || head > 85 && head < 90 || head > 94 && head < 100));

                        }

                        if (orientation == 2) {
                            do {
                                head = r.nextInt(78) + 1;
                            } while (head > 60);
                        }

                    } while (willCollide(head, nb, orientation));
                    Debordementsafe = true;
                }

                while (ContourSafe == false) {
                    contourCollision = this.createContour(head, nb, orientation);
                    if (contourCollision == true) {
                        ContourSafe = true;
                    } else {
                        Contour.clear();
                        do {
                            nb = (int) (Math.random() * ((5 - 2) + 1)) + 2;
                            orientation = r.nextInt(2) + 1; //if ==1 horizental,2== verticale

                            if (orientation == 1) {
                                do {
                                    head = r.nextInt(93) + 1; //premiere case du bateau [1,98]
                                } while ((head > 5 && head < 10 || head > 15 && head < 20 || head > 25 && head < 30 || head > 35 && head < 40 || head > 45 && head < 50 || head > 55 && head < 60 || head > 65 && head < 70 || head > 75 && head < 80 || head > 85 && head < 90 || head > 94 && head < 100));

                            }

                            if (orientation == 2) {
                                do {
                                    head = r.nextInt(78) + 1; //premiere case du bateau [1,98]
                                } while (head > 60);
                            }

                        } while (willCollide(head, nb, orientation));
                        Debordementsafe = true;
                    }
                }
                if (ContourSafe == true && Debordementsafe == true) {
                    ready = true;
                    Debordementsafe = false;
                    ContourSafe = false;
                }
            }

            if (orientation == 1) { //horizontal

                for (int j = 0; j < nb; j++) {
                    boatsection.add(listbtn[head + j]); // cree le content dun ship
                    listJButtonInShip.add(listbtn[head + j]); //ajoute les boutons du ship dans le container
                    Usedbuttons.add(head + j);
                }
            }

            /**
             * ****************************VERTICAL***************************************
             */
            if (orientation == 2) { //vertical

                for (int j = 0; j < nb; j++) {
                    if (j == 0) { //if premier elem
                        boatsection.add(listbtn[head]); // cree le content dun ship
                        listJButtonInShip.add(listbtn[head]); //ajoute les boutons du ship dans le container
                        Usedbuttons.add(head);
                    } else {
                        boatsection.add(listbtn[head + (10 * j)]); // cree le content dun ship
                        listJButtonInShip.add(listbtn[head + (10 * j)]); //ajoute les boutons du ship dans le container
                        Usedbuttons.add(head + (10 * j));

                    }
                }
            }

            Ship s = new Ship(boatsection);
            listShip.add(s);
            ready = false;
        }
    }

    private boolean createContour(int head, int length, int orientation) {
        if (orientation == 1) {          //horizontal
            for (int i = 0; i < length; i++) {
                if (head + i >= 0 && head + i <= 9) {  //****************top
                    if (head + i == 0) { //top left
                        Contour.add((head + i) + 10);
                    } else if (head + i == 9) { //top right
                        Contour.add((head + i) + 10);
                    } else {
                        if (i == 0) { //premier elem
                            Contour.add((head + i) + 10);
                            Contour.add((head + i) - 1);
                        }
                        if (i == length - 1) { //last elem
                            Contour.add((head + i) + 10);
                            Contour.add((head + i) + 1);
                        }
                    }
                }
                if (head + i >= 90 && head + i <= 99) {  //****************Bot
                    if (head + i == 90) { //bot left
                        Contour.add((head + i) - 10);
                    } else if (head + i == 9) { //bot right
                        Contour.add((head + i) - 10);
                    } else {
                        if (i == 0) { //premier elem
                            Contour.add((head + i) - 10);
                            Contour.add((head + i) - 1);
                        } else if (i == length - 1) { //last elem
                            Contour.add((head + i) - 10);
                            Contour.add((head + i) + 1);
                        } else {
                            Contour.add((head + i) - 10);
                        }
                    }
                }
                if ((head + i) % 10 == 0 && (head + i) != 90) {  //****************left
                    Contour.add((head + i) + 10);
                    Contour.add((head + i) - 10);
                }                               //****************right
                if (head + i == 19 || head + i == 29 || head + i == 39 || head + i == 49 || head + i == 59 || head + i == 69 || head + i == 79 || head + i == 89) {
                    Contour.add((head + i) + 10);
                    Contour.add((head + i) - 10);
                }                               //autre
                if (head + i >= 11 && head + i <= 18 || head + i >= 21 && head + i <= 28 || head + i >= 31 && head + i <= 38 || head + i >= 41 && head + i <= 48 || head + i >= 51 && head + i <= 58 || head + i >= 61 && head + i <= 68 || head + i >= 71 && head + i <= 78 || head + i >= 81 && head + i <= 88) { //touche pas les border
                    if (i == 0) { //first elem
                        Contour.add((head + i - 1));
                        Contour.add((head + i) + 10);
                        Contour.add((head + i) - 10);
                    } else if (i == length - 1) { //last elem
                        Contour.add((head + i) + 1);
                        Contour.add((head + i) + 10);
                        Contour.add((head + i) - 10);
                    } else { //milieu
                        Contour.add((head + i) + 10);
                        Contour.add((head + i) - 10);
                    }
                }
            }
        }
        if (orientation == 2) {
            /**
             * ********************************A FAIRE Vertical*******************
             */
            for (int i = 0; i < length; i++) {
                if (head + 10 * i >= 0 && head + 10 * i <= 9) {  //****************top
                    if (head + 10 * i == 0) { //top left
                        Contour.add((head + 10 * i) + 1);
                    } else if (head + 10 * i == 9) { //top right
                        Contour.add((head + 10 * i) - 1);
                    } else {
                        if (i == 0) { //premier elem
                            Contour.add((head + 10 * i) + 1);
                            Contour.add((head + 10 * i) - 1);
                        }

                    }
                }
                if (head + 10 * i >= 90 && head + 10 * i <= 99) {  //****************Bot

                    if (i == length - 1) { //last elem
                        Contour.add((head + 10 * i) - 1);
                        Contour.add((head + 10 * i) + 1);
                    }
                }
                if ((head + 10 * i) % 10 == 0 && (head + 10 * i) != 90) {  //****************left
                    if (i == 0) { //premier elem
                        Contour.add((head + 10 * i) - 10);
                        Contour.add((head + 10 * i) + 1);
                    } else if (i == length - 1) { //last elem
                        Contour.add((head + 10 * i) + 1);
                        Contour.add((head + 10 * i) + 10);
                    } else {
                        Contour.add((head + 10 * i) + 1);

                    }
                }                               //****************right
                if (head + 10 * i == 19 || head + 10 * i == 29 || head + 10 * i == 39 || head + 10 * i == 49 || head + 10 * i == 59 || head + 10 * i == 69 || head + 10 * i == 79 || head + 10 * i == 89) {
                    if (i == 0) { //premier elem
                        Contour.add((head + 10 * i) - 10);
                        Contour.add((head + 10 * i) - 1);
                    } else if (i == length - 1) { //last elem
                        Contour.add((head + 10 * i) - 1);
                        Contour.add((head + 10 * i) + 10);
                    } else {
                        Contour.add((head + 10 * i) - 1);
                    }
                }

                //autre
                if (head + 10 * i >= 11 && head + 10 * i <= 18 || head + 10 * i >= 21 && head + 10 * i <= 28 || head + 10 * i >= 31 && head + 10 * i <= 38 || head + 10 * i >= 41 && head + 10 * i <= 48 || head + i >= 51 && head + i <= 58) { //touche pas les border
                    if (i == 0) { //first elem
                        Contour.add((head + 10 * i - 1));
                        Contour.add((head + 10 * i + 1));
                        Contour.add((head + 10 * i - 10));
                    } else if (i == length - 1) { //last elem
                        Contour.add((head + 10 * i) + 1);
                        Contour.add((head + 10 * i) - 1);
                        Contour.add((head + 10 * i) + 10);
                    } else { //milieu
                        Contour.add((head + 10 * i) + 1);
                        Contour.add((head + 10 * i) - 1);
                    }
                }
            }
        }

        for (int k = 0; k < Contour.size(); k++) {
            if (Usedbuttons.contains(Contour.get(k))) {
                return false;
            }
        }

        for (int j = 0; j < Contour.size(); j++) {
            Usedbuttons.add(Contour.get(j));
        }
        Contour.clear();
        return true;
    }

    private boolean willCollide(int head, int length, int orientation) {
        if (orientation == 1) {         //horizontal
            for (int i = 0; i < length - 1; i++) {
                int res = head + i;
                if (Usedbuttons.contains(res)) {
                    return true;
                }
            }
        }
        if (orientation == 2) {          //vertical
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    if (Usedbuttons.contains(head)) {
                        return true;
                    }
                } else {
                    int res = head + (10 * i);
                    if (Usedbuttons.contains(res)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String UpdateGrig(String incomming) {
        String[] msg = incomming.split(",");
        int cible = Integer.valueOf(msg[0]);
        int retour = Integer.valueOf(msg[1]);
        int status = Integer.valueOf(msg[2]);
        int stat1 = 0;

        for (Ship element : listShip) {
            stat1 = element.checkHit(listbtn[cible]);
            if (stat1 != 0) {
                break;
            }
        }

        if (status == 0) {
            if (retour != 0) {
                listbtn[retour].setBorder(BorderFactory.createLineBorder(java.awt.Color.magenta, 5)); // Line Border + Thickness of the Border
            }
        }
        if (status == 1) {
            listbtn[retour].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 5));
        }
        if (status == 3) {
            listbtn[retour].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 5)); 
        }

        String msge = String.valueOf(cible) + "," + String.valueOf(stat1);
        return msge;
    }

}
