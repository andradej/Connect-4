/* Title:  Player.java
 * Author: Jackie Andrade, Giuliano Conte
 * Date: 12/8/14
 * Purpose: Create a Computer Player (named Jerry) that will play against the opposing player. 
 */

import java.util.*; 

public class Player {
    
    private final static int Red = 10;  
    private final static int Blue = 1; 
    private final static int Blank = 0;
    
    private final static int Inf = 1000000;
    
    private static final int MAX_DEPTH = 7;
    
    private int maxDepth;
    private static int searchCount = 0;
    
    public Player(int maxD) {
        maxDepth = maxD;
    }
    
    public Player() {
        maxDepth = MAX_DEPTH;
    }
    
    
    //returns the best column move of the player
    public int move(int[][] B) {
        long startTime = System.nanoTime();
        int[][] BB = swapPositions(B);
        
        int min = Inf+1;
        int bestMove = -1;
        
        for(int col = 0; col < 8; col++) {
            for (int row = 7; row >= 0; --row) {
                if (BB[row][col] == Blank) {
//                    searchCount++;
                    BB[row][col] = Blue;
                    
                    int val = minMax(BB, 1, -Inf, Inf); //minMax with AB pruning
                    
                    if (val < min) {
                        bestMove = col;
                        min = val;
                    }
                    BB[row][col] = Blank;
                    break;
                }
            }
        }
//        print("Searched " + searchCount + " boards!");
//        searchCount = 0;
//        
//        long endTime = System.nanoTime();
//        long duration = endTime - startTime;
//        long integer = duration / 1000000000;
//        long decimal = duration % 1000000000;
//        print("Move generated in " + integer + "." + decimal + " seconds.");
        
        return bestMove;
    }
    
    public static int eval(int[][] B) {
        int val = 0;
        int count;
        
        for (int r = 0; r < 8; ++r) {
            for (int c = 0; c < 8; ++c) {
                //count rows
                if (c < 5) {
                    count = countPieces(B[r][c], B[r][c+1], B[r][c+2], B[r][c+3]);
                    if (count ==  4) return  Inf;
                    if (count == -4) return -Inf;
                    val += score(count);
                }
                //count columns
                if (r < 5) {
                    count = countPieces(B[r][c], B[r+1][c], B[r+2][c], B[r+3][c]);
                    if (count ==  4) return  Inf;
                    if (count == -4) return -Inf;
                    val += score(count);
                }
                //count top-left to bottom-right diagonals
                if (c < 5 && r < 5) {
                    count = countPieces(B[r][c], B[r+1][c+1], B[r+2][c+2], B[r+3][c+3]);
                    if (count ==  4) return  Inf;
                    if (count == -4) return -Inf;
                    val += score(count);
                }
                //count bottom-left to top-right diagonals
                if ((c >= 3 && c < 8) && r < 5) {
                    count = countPieces(B[r][c], B[r+1][c-1], B[r+2][c-2], B[r+3][c-3]);
                    if (count ==  4) return  Inf;
                    if (count == -4) return -Inf;
                    val += score(count);
                }
            }
        }
        return val;
    }
    
    //uses alpha beta pruning to find greatest move val
    private int minMax(int [][] B, int depth, int alpha, int beta) {
        if( isLeaf(B) || depth >= this.maxDepth) {
            if (isLeaf(B)) searchCount++;
            return eval(B);
        }
        else if( depth % 2 == 1 ) {       // even levels are max, Red player  
            int max = -Inf-1;
            for (int col = 0; col < 8; ++col) {
                for (int row = 7; row >=0; --row) {
                    if (B[row][col] == Blank) {
                        searchCount++;
                        B[row][col] = Red;
                        
                        alpha = Math.max(alpha, max);  
                        
                        max = Math.max(max, minMax(B, depth+1, alpha, beta));
                        
                        B[row][col] = Blank;
                        break;
                    }
                }
                if (beta < alpha) {
                    break;
                }
            }
            return max;
        } else {
            int min = Inf+1;
            for (int col = 0; col < 8; ++col) {
                for (int row = 7; row >=0; --row) {
                    if (B[row][col] == Blank) {
                        searchCount++;
                        B[row][col] = Blue;
                        
                        beta = Math.min(beta, min);
                        
                        min = Math.min(min, minMax(B, depth+1, alpha, beta));
                        
                        B[row][col] = Blank;
                        break;
                    }
                }
                if (beta < alpha) {
                    break;
                }
            }
            return min;
        }
    }
    
    //returns whether red wins
    private static boolean winForRed(int [][] B) {
        return winHelper(B, Red);
    }
    
    //returns whether blue wins
    private static boolean winForBlue(int [][] B) {
        return winHelper(B, Blue);
    }
    
    //helper method to the winMethods
    private static boolean winHelper(int [][] B, int color) {
        
        int val;
        if (color == 10) 
            val = 40;
        else 
            val = 4;
            
        //check rows
        for (int r = 0; r < 8; ++r) {
            for (int c = 0; c < 5; ++c) {
                if ((B[r][c] + B[r][c+1] + B[r][c+2] + B[r][c+3]) == val) {
                    return true;
                }
            }
        }
        
        //check columns
        for (int c = 0; c < 8; ++c) {
            for (int r = 0; r < 5; ++r) {
                if ((B[r][c] + B[r+1][c] + B[r+2][c] + B[r+3][c]) == val) {
                    return true;
                }
            }
        }  
        
        //check right diagonals
        for (int r = 0; r < 5; ++r) {
            for (int c = 0; c < 5; ++c) {
                if ((B[r][c] + B[r+1][c+1] + B[r+2][c+2] + B[r+3][c+3]) == val) {
                    return true;
                }
            }
        }
        
        //check for left diagonals
        for (int r = 0; r < 5; ++r) {
            for (int c = 3; c < 8; ++c) {
                if ((B[r][c] + B[r+1][c-1] + B[r+2][c-2] + B[r+3][c-3]) == val) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    
    //helper method that counts the sum values of the pieces
    private static int countPieces(int a, int b, int c, int d) {
        int n = a + b + c + d;
        int numRed = n / 10;
        int numBlue = n % 10;
        if(numRed > 0 && numBlue > 0)
            return 0;        // no move for either in this row, return 0
        else if(numBlue == 0)  // only Red's in this sequence
            return numRed;
        else if(numRed == 0)  // only Blue's in this sequence
            return -numBlue; 
        return 0;           // needed for compilation
    }
    
    //helper method that returns value
    private static int score(int n) {
        switch(n) {
            case  0:  return    0;
            case  1:  return    5;
            case -1:  return   -5;
            case  2:  return   25;
            case -2:  return  -25;
            case  3:  return  125;
            case -3:  return -125;
            case  4:  return  Inf;
            case -4:  return -Inf;
            
            default: return 0;
        }
    }
    
    //checks if leaf node
    private static  boolean isLeaf(int[][] B) {
        if(winForRed(B) || winForBlue(B))
            return true; 
        for (int c = 0; c < 8; c++) {
            for (int r = 7; r >= 0; r--) {
                if (B[r][c] == Blank)
                    return false;
            }
        }
        return true;
    }
    
    private static int[][] swapPositions(int[][] B) {
        // Returns best move after swapping the positions of the board. To be used by Connect4.java
        // -- This is because the red and blue values are opposite in Player.java from Connect4.
        int[][] BB = new int[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if      (B[r][c] == Red)  BB[r][c] = Blue;
                else if (B[r][c] == Blue) BB[r][c] = Red;
            }
        }
        return BB;
    }
    
    
    
    
    
/*-----------------------------------------------------------------------------------------------------*/
/*  |TEXT BOARD METHODS|                                                                               */
/*-----------------------------------------------------------------------------------------------------*/
    
    public static int[][] copyBoard(int[][] B) {
        int[][] C = new int[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                C[r][c] = B[r][c];
            }
        }
        return C;
    }
    
    public static String textBoard(int[][] B) {
        int x = B[0].length;
        int y = B.length;
        
        String s = "";
        for (int r = 0; r < y; r++) {
            s += ("\n\t\t---------------------------------\n\t\t|");
            
            for (int c = 0; c < x; c++) {
                s += (" ");
                if      (B[r][c] == Red)   s += ("X");
                else if (B[r][c] == Blue)  s += ("O");
                else if (B[r][c] == Blank) s += (" ");
                
                s += (" |");
            }
        }
        s += ("\n\t\t---------------------------------\n");
        return s;
    }
    
    private static int[][] generateRandomBoard(int moves) {
        int[][] B = new int[8][8];
        
        Random R = new Random();
        if (moves == -1) moves = R.nextInt(64);
        for (int i = 0; i < moves; i++) {
            
            for (int j = 0; j < 8; j++) {
                int nextMove = R.nextInt(8);
                if (i % 2 == 0) r(nextMove, B);
                else            b(nextMove, B);
                
                if (!isLeaf(B)) break;
                else            delete(nextMove, B);
            }
            
        }
        return B;
    }
    
    
    
    
    
    private static void r(int c, int[][] B) {
        int r = 7;
        while (true) {
            if (r < 0)
                return;
            if (B[r][c] == Blank) {
                B[r][c] = Red;
                return;
            }
            r--;
        }
    }
    
    private static void b(int c, int[][] B) {
        int r = 7;
        while (true) {
            if (r < 0)
                return;
            if (B[r][c] == Blank) {
                B[r][c] = Blue;
                return;
            }
            r--;
        }
    }
    
    private static void delete(int c, int[][] B) {
        int r = 0;
        while (true) {
            if (r > 7)
                return;
            if (B[r][c] != Blank) {
                B[r][c] = Blank;
                return;
            }
            r++;
        }
    }
    
    
    
    
    
/*-----------------------------------------------------------------------------------------------------*/
/*  |TEST AND DEBUG METHODS|                                                                           */
/*-----------------------------------------------------------------------------------------------------*/
    
    public static void print(Object s) {
        System.out.println(s);
    }
    
    private static void printCon(Object s) {
        System.out.print(s);
    }
    
    private static void print() {
        print("");
    }
    
}

/*
 * METHODS 
 * 
 */



class PQueue {
    
    public final int SIZE = 8; 
    private Board[] A = new Board[SIZE]; 
    private int size = 0;
    private int front = 0; 
    private int next = 0; 
    
    
    /*
     * Inserts an integer and puts it in its appropriate
     * place in the priority queue.
     */
    public void insert(Board B, int mult) { 
        A[next] = B;
        
        int back;                              //back is the same as next but
        if (next <= front && size() > 0) {     //sometimes "extended" past the
            back = next + A.length;            //length of the array, to help the
        } else {                               //for-loop to read through the 
            back = next;                       //array if next is "behind" front
        }
        
        for (int i = back; i > front; i--) {
            int ii = i % A.length;             //used to denote actual location in array
            
            if (A[ii].compareTo(A[nextSlotCounterClock(ii)], mult) > 0) {
                swap(ii, nextSlotCounterClock(ii));
            }
        }
        
        next = nextSlotClock(next);
        ++size;
    }
    
    /*
     * Returns highest priority item in the queue.
     */
    public Board getMax() {
        Board temp = A[front];
        front = nextSlotClock(front);
        --size;
        return temp; 
    }
    
    /*
     * Swaps two positions in the array
     */
    private void swap(int b, int a) {
        Board temp = A[a];
        A[a] = A[b];
        A[b] = temp;
    }
    
    /*
     * Returns next index in a circular array clockwise
     */
    private int nextSlotClock(int k) { 
        return ((k + 1) % A.length); 
    }
    
    /*
     * Returns next index in circular array counter-clockwise
     */
    private int nextSlotCounterClock(int k) {
        return ((k + (A.length - 1)) % A.length);               
    }
    
    /*
     * returns the number of elements stored in the array
     */
    public int size() { 
        return size; 
    }  
    
    /*
     * Returns whether the array is empty or not
     */
    public boolean isEmpty() { 
        return (size == 0); 
    }
    
    /*
     * Graphically prints the array, showing where
     * the front and next indices are.
     */
    public void list() {
            Player.print("---PRINTING PQUEUE... ==-------");
        for (int i = 0; i < A.length; i++) {
            if (next == i) {
                System.out.print("/ ");
            }
            for (int j = 0; j < size; j++) {
                Player.print(Player.textBoard(this.getMax().getArray()));
            }
            if (nextSlotCounterClock(front) == i) {
                System.out.print("\\ ");
            }
        }
            Player.print("---DONE PRINTING PQUEUE... ==-------");
        System.out.println();
    }
    
}

class Board {
    private int[][] A;
    private int val;
    
    public Board(int[][] B) {
        A = B;
        val = Player.eval(B);
    }
    
    public Board(int[][] B, int v) {
        A = B;
        val = v;
    }
    
    public int compareTo(Board B, int mult) {
        if (val < B.val)  return  1*mult;
        if (val > B.val)  return -1*mult;
        else              return  0;
    }
    
    public int[][] getArray() {
        return A;
    }
    
    public int getVal() {
        return val;
    }
}


