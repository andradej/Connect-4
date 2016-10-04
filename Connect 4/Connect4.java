/* Title:  Connect4.java
 * Author: Wayne Snyder (waysnyder@gmail.com)
 * Date: 4/19/12
 * Purpose: This is a driver program for a Connect4 program for CS 112
 * Uses: method move() from class Player
 */    


import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.EventObject;


public class Connect4 {
  
  static final int delay = 100; 
  static final int delayReset = 2000; 
  static int movesLeft = 64; 
  
  private static Player machine; 
  
  private static final int redPlayer = 1;                 // human player
  private static final int bluePlayer = 10;               // machine
  
  private static JFrame frame=new JFrame("Connect Four");
  private static int [][] slot = new int[8][8];                 // 0 = no piece; 1 = player;  10 = machine
  private static JButton [][] slotButton = new JButton[8][8];   // Background color = no piece; red = human player; blue = machine
  private static JLabel l1=new JLabel("Connect Four"); 
  private static JButton resetButton = new JButton("Reset");
  private static JButton quitButton = new JButton("Quit");
  private static JLabel results = new JLabel("Playing....");  

  
  private static Color bg = new Color(255,240,180); 
  private static Color [] normal = { new Color(255,240,180), new Color(255,100,100), null, null, null, null, null, null, null, null, new Color(100,100,255) };  
  private static Color [] win  = { new Color(255,240,180), new Color(200,0,0), null, null, null, null, null, null, null, null, new Color(0,0,200) };   
  
   
  // check for win by player, 4 in a sequence
  private static boolean checkWin(int player) {    // 1 = player, 10 = machine

    // check all horizontal rows
    for(int i = 0; i < 8; ++i)
      for(int j = 0; j < 5; ++j) {
         if(slot[i][j] == player && slot[i][j+1] == player && slot[i][j+2] == player && slot[i][j+3] == player) {
             // code when found win
           int j2 = j;
           for(int k = 0; k < 4; ++k) {
             slotButton[i][j2].setBackground(win[player]); 
             slotButton[i][j2].setOpaque(true); 
             j2 += 1; 
           }
           return true;
         }
      }
 
    // check all vertical columns
    for(int i = 0; i < 5; ++i)
      for(int j = 0; j < 8; ++j) {
         if(slot[i][j] == player && slot[i+1][j] == player && slot[i+2][j] == player && slot[i+3][j] == player) {
             // code when found win
           int i2 = i;
           for(int k = 0; k < 4; ++k) {
             slotButton[i2][j].setBackground(win[player]); 
             slotButton[i2][j].setOpaque(true); 
             i2 += 1; 
           }
           return true;
         }
    } 

 
    // check all lower-left to upper-right diagonals
    for(int i = 3; i < 8; ++i)
      for(int j = 0; j < 5; ++j) {
      
         if(slot[i][j] == player && slot[i-1][j+1] == player && slot[i-2][j+2] == player && slot[i-3][j+3] == player) {
             // code when found win
           int i2 = i; int j2 = j; 
           for(int k = 0; k < 4; ++k) {
             slotButton[i2][j2].setBackground(win[player]); 
             slotButton[i2][j2].setOpaque(true); 
             i2 += -1; j2 += 1; 
           }
           return true;
         }  
    }   
    // check all upper-left to lower-right diagonals
    for(int i = 0; i < 5; ++i)
      for(int j = 0; j < 5; ++j) {
         if(slot[i][j] == player && slot[i+1][j+1] == player && slot[i+2][j+2] == player && slot[i+3][j+3] == player) {
           int i2 = i; int j2 = j; 
           for(int k = 0; k < 4; ++k) {
             slotButton[i2][j2].setBackground(win[player]); 
             slotButton[i2][j2].setOpaque(true); 
             i2 += 1; j2 += 1; 
           }
           return true;
         }
    }

     return false;  
  }
  

  
  private static void reset() {
     for(int i = 0; i < 8; ++i)
       for(int j = 0; j < 8; ++j)
          slot[i][j] = 0; 
     for(int i = 0; i < 8; ++i)
       for(int j = 0; j < 8; ++j) {
          slotButton[i][j].setBackground(bg); 
          slotButton[i][j].setOpaque(true); 
       }
     results.setText("Playing....");
     movesLeft = 64; 
 //    System.out.println("Total nodes generated: " + machine.totalNodes); 
 //    machine.totalNodes = 0; 
//     System.out.println("Resetting.........\n\n"); 
  }
  
  public static void main(String [] args) {
    
     machine = new Player(); 
   
     frame.setSize(800,800);
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setBackground(bg); 

     l1.setFont(new Font("TimesRoman",Font.BOLD,32)); 
     quitButton.setFont(new Font("TimesRoman",Font.BOLD,20)); 
     resetButton.setFont(new Font("TimesRoman",Font.BOLD,20)); 
     results.setFont(new Font("TimesRoman",Font.BOLD,20)); 
     

     
     for(int i = 0; i < 8; ++i)
       for(int j = 0; j < 8; ++j) {
          slotButton[i][j] = new JButton(); 
          JBox.setSize(slotButton[i][j],80,80); 
          slotButton[i][j].setBorder(BorderFactory.createEtchedBorder());
          slotButton[i][j].setBackground(bg); 
       }
     JBox body=JBox.vbox(JBox.CENTER, 
                         JBox.vspace(10),
                         l1,
                         JBox.vspace(10), 
                         JBox.hbox(JBox.CENTER, 
                                   slotButton[0][0], slotButton[0][1], slotButton[0][2], slotButton[0][3], slotButton[0][4], slotButton[0][5], slotButton[0][6],slotButton[0][7]),
                         JBox.hbox(JBox.CENTER,                         
                                   slotButton[1][0], slotButton[1][1], slotButton[1][2], slotButton[1][3], slotButton[1][4], slotButton[1][5], slotButton[1][6], slotButton[1][7]),
                         JBox.hbox(JBox.CENTER,
                                   slotButton[2][0], slotButton[2][1], slotButton[2][2], slotButton[2][3], slotButton[2][4], slotButton[2][5], slotButton[2][6],slotButton[2][7]),
                         JBox.hbox(JBox.CENTER,
                                   slotButton[3][0], slotButton[3][1], slotButton[3][2], slotButton[3][3], slotButton[3][4], slotButton[3][5], slotButton[3][6],slotButton[3][7]),
                         JBox.hbox(JBox.CENTER,
                                   slotButton[4][0], slotButton[4][1], slotButton[4][2], slotButton[4][3], slotButton[4][4], slotButton[4][5], slotButton[4][6],slotButton[4][7]),
                         JBox.hbox(JBox.CENTER,
                                   slotButton[5][0], slotButton[5][1], slotButton[5][2], slotButton[5][3], slotButton[5][4], slotButton[5][5], slotButton[5][6],slotButton[5][7]),                                   
                         JBox.hbox(JBox.CENTER,
                                   slotButton[6][0], slotButton[6][1], slotButton[6][2], slotButton[6][3], slotButton[6][4], slotButton[6][5], slotButton[6][6],slotButton[6][7]),                                   
                        JBox.hbox(JBox.CENTER,
                                   slotButton[7][0], slotButton[7][1], slotButton[7][2], slotButton[7][3], slotButton[7][4], slotButton[7][5], slotButton[7][6],slotButton[7][7]),                                    
                         JBox.vspace(10),
                         JBox.hbox(JBox.CENTER,
                                   quitButton, JBox.hspace(100), 
                                   resetButton, JBox.hspace(100), 
                                   results),
                         JBox.vspace(10)
     ); 
  
                           
     frame.add(body);
     frame.setVisible(true);
     
     JEventQueue events=new JEventQueue();
     events.listenTo(resetButton,"reset");
     events.listenTo(quitButton,"quit");
     for(int i = 0; i < 8; ++i) 
       for(int j = 0; j < 8; ++j) 
         events.listenTo(slotButton[i][j], i + " " + j);
   
    eventloop:   while(true){
        EventObject event=events.waitEvent();
        String name=events.getName(event);
        if(name.equals("quit")){
            System.exit(1); 
        } else if(name.equals("reset")){
            reset(); 
        } else {
          // read move from the GUI; name should be the row and column separated by space
          for(int i = 0; i < 8; ++i) 
            for(int j = 0; j < 8; ++j) {
              if(name.equals(i + " " + j)){ 
                 if(slot[i][j] != 0)              // user pushed button already pushed, so ignore
                     continue eventloop; 
                 // user made a legal move
             //    System.out.println("Red Player: " + j); 
                 --movesLeft; 
                 int r = i;
                 while(r < 8 && slot[r][j] == 0) {
                    slot[r][j] = redPlayer;                  
                    slotButton[r][j].setBackground(normal[redPlayer]); 
                    slotButton[r][j].setOpaque(true); 
                    ++r;
                    if(r < 8 && slot[r][j] == 0) {
                       try {
                          Thread.sleep(delay);
                       } catch(InterruptedException e) {
                       }
                       slot[r-1][j] = 0;                  
                       slotButton[r-1][j].setBackground(bg); 
                       slotButton[r-1][j].setOpaque(false); 
                    }
                 }
                 
              }       
            }
          
          if(checkWin(redPlayer)) {
            results.setText("Red Player Wins!"); 
            try {
             Thread.sleep(delayReset);
            } catch(InterruptedException e) {
            }
            results.setText("Resetting...."); 
            try {
             Thread.sleep(delayReset);
            } catch(InterruptedException e) {
            }
            reset(); 
            continue; 
          }
          try {
             Thread.sleep(1000);
          } catch(InterruptedException e) {
          } 
          
   //       int j = machine.move(slot,bluePlayer); 
          int j = machine.move(slot); 
          
     //     System.out.println("Blue Player: " + j); 
          --movesLeft; 
          if(movesLeft <= 0) {
            results.setText("Tie Game!"); 
            try {
             Thread.sleep(delayReset);
            } catch(InterruptedException e) {
            }
            results.setText("Resetting...."); 
            reset();  
            try {
             Thread.sleep(delayReset);
            } catch(InterruptedException e) {
            }
            continue eventloop; 
          }
          //System.out.println(j); 
          int r = 0;
          
          while(r < 8 && r >= 0 && slot[r][j] == 0) {
             slot[r][j] = bluePlayer;                  
             slotButton[r][j].setBackground(normal[bluePlayer]); 
             slotButton[r][j].setOpaque(true); 
             ++r;
             if(r < 8 && r >= 0 && slot[r][j] == 0) {
                try {
                   Thread.sleep(delay);
                } catch(InterruptedException e) {
                }
                slot[r-1][j] = 0;                  
                slotButton[r-1][j].setBackground(bg); 
                slotButton[r-1][j].setOpaque(false); 
             }
          }

          if(checkWin(bluePlayer)) {
            results.setText("Blue Player Wins!"); 
            try {
             Thread.sleep(delayReset);
            } catch(InterruptedException e) {
            }
            results.setText("Resetting...."); 
            reset();  
            try {
             Thread.sleep(delayReset);
            } catch(InterruptedException e) {
            }
            
          }
        }
     }
  }
  
}

