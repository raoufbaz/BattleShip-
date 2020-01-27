/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintStream;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ftonye
 */
public class BackGroundCom implements Runnable
{
     private int port;
     private String IP;
     private ServerSocket ss ;
     private Socket cs;
     private Scanner reader;
     private PrintStream writer ;
   
     public Boolean dataToSend;
  
     
     public String missileEntrant;
     public String missileSortant;
     
     public int cible;
      public int cible_retour;
       public int status;
     
//constructor pour server
     public BackGroundCom(int port)
     {
         this.port = port;
         dataToSend = false;
         startServer();
         cible_retour=0;
         status=0;
       
     }
     //constructor pour client
      public BackGroundCom(int port, String adresseIP)
     {
         this.port = port;
         this.IP = adresseIP;
         dataToSend = false;
       
        startClient(); 
     }
     
     private void startServer()
     {
        try 
        {   
            ss = new ServerSocket(this.port); //creer le socket serveur
            cs = ss.accept(); //quand serverSocket trouve un client, il stock la reference dans CS
            JOptionPane.showMessageDialog(null,"Server accept connection from" + cs.getInetAddress().getHostAddress());
        }
        catch (IOException ex)
        {
          JOptionPane.showMessageDialog(null,ex.getMessage());
        }
					 
	System.out.println("this is server");
     }
    
      private void startClient()
     {
        try 
        {
              System.out.println("this is client ");
            cs = new Socket(IP,port); //server ip adress et port 
        } catch (IOException ex) {
            Logger.getLogger(BackGroundCom.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
      
     @Override
    public void run()
    {
        try
        {
           reader = new Scanner(cs.getInputStream()); // cree objet qui recupere ce que le client envoi     //client recoit du serveur
        } 
          catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
          
        try
        {
           writer = new PrintStream(cs.getOutputStream()); // cree objt qui envoi information au client    //envoi au server
        } 
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
        while(true)
        {
            synchronized(dataToSend)
           {
                if(dataToSend)
                {
                    missileSortant = String.valueOf(cible) + "," + String.valueOf(cible_retour) + "," + String.valueOf(status) ;
                    
                     writer.println(missileSortant ); // envoi message contenu dans missileOutgoing
                     dataToSend = false;                //ferme le
                }
                else 
                {
                    writer.println(100); //apres quil envoi le premier missile, il envoi juste 100
                }
           }
            
           missileEntrant = reader.nextLine();//quand il recoit un message, stock dans missile incoming
           String[] msg = missileEntrant.split(",");
           
           if(Integer.valueOf(msg[0]) >= 0 && Integer.valueOf(msg[0]) < 100 )
           {
               String msg2;
               msg2 = BattleSea.UpdateGrig(missileEntrant);
                  String[] msg1 = msg2.split(",");
                  cible_retour = Integer.valueOf(msg1[0]);
                  status = Integer.valueOf(msg1[1]);
           }
           
           
          
             
             
        }
      
       
    }

    
}
