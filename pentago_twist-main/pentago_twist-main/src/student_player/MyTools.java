package student_player;

import boardgame.Board;
import boardgame.Move;
import pentago_twist.PentagoBoard;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

import java.util.ArrayList;
import java.util.Collections;

public class MyTools {

    int mycolour =999;
    int enemycolour =999;


    public MyTools(int whoami){ //constructor that initializes my player's colour
        if (whoami == 0){
            mycolour =1;
            enemycolour = 0;
        }
        else if (whoami == 1){
            mycolour =0;
            enemycolour = 1;
        }
    }


    public class MoveNode{ //class that holds move and value of move
        public PentagoMove MyMove = null;
        public int value;

        public MoveNode(PentagoMove mymove, int value){ //constructor
            this.value = value;
            this.MyMove = mymove;
        }
    }




    public int consecvalue = 125; //get points for consecutive * W W * * * = 125 and * W W W * * = 250
    public int blankspaceconsec =25; // get points for surrounding blank space  * W * = +50  and BW* = +25
    public int disjointset = -70;//-70 //lose points if disjoint W W B W W
    public int unwinnable = -125;//-100 //W W W W B B lost points if there are 4 blacks and 2 whites or vice versa in the same row,column or diag
    public int jointset = 70; // W W E W W

   //Analyze all row,column or diag based on conditions above and return score of given board state
    public int evaluate(PentagoBoardState pbs) {
        PentagoBoardState.Piece[][] pieces = pbs.getBoard();


        int whitescore =0; //keep track of white's total score
        int blackscore = 0;//keep track of black's total score

        //Check rows
        for (int x = 0; x <6 ; x++) {
            int countWHori = 0;
            int countBHori = 0;

            int blacks = 0;
            int whites = 0;


            for (int y = 0; y <5 ; y++) {
                //Count how many black and white pieces
                if (pieces[x][y].ordinal() == 0 ) {

                    //countBHori = countBHori + countvalue;
                    blacks++;
                }
                if (pieces[x][y].ordinal() == 1) {

                    //countWHori = countWHori +countvalue;
                    whites++;
                }

                //Check for consecutive
                if (pieces[x][y].ordinal() == 0 && pieces[x][y+1].ordinal() == 0 ) {

                    countBHori = countBHori +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 0 && pieces[x][y+1].ordinal() == 2 ) {

                    countBHori = countBHori + blankspaceconsec;
                }
                else if (pieces[x][y].ordinal() == 2 && pieces[x][y+1].ordinal() == 0 ) {

                    countBHori = countBHori + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x][y+1].ordinal() == 1 ) {

                    countWHori = countWHori +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x][y+1].ordinal() == 2 ) {

                    countWHori = countWHori + blankspaceconsec;
                }
                else if (pieces[x][y].ordinal() == 2 && pieces[x][y+1].ordinal() == 1 ) {

                    countWHori = countWHori + blankspaceconsec;
                }

                //Check for disjoint and joint set If * B B W * * then disjoint and * B B * B * Then joint set for B * B
                if (y!=4){
                    if (pieces[x][y].ordinal() == 0 && pieces[x][y+1].ordinal() == 0 && pieces[x][y+2].ordinal() == 1){
                        countBHori = countBHori +disjointset;
                    }
                    else if (pieces[x][y].ordinal() == 1 && pieces[x][y+1].ordinal() == 1 && pieces[x][y+2].ordinal() == 0){
                        countWHori = countWHori +disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x][y+1].ordinal() == 2 && pieces[x][y+2].ordinal() == 0){
                        countBHori = countBHori +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 1 && pieces[x][y+1].ordinal() == 2 && pieces[x][y+2].ordinal() == 1){
                        countWHori = countWHori +jointset;
                    }
                }
            }
            //check if unwinnable
            if (blacks == 4 && whites==2){
                countBHori += unwinnable;
            }
            if (blacks == 2 && whites==4){
                countWHori += unwinnable;
            }
            if (blacks == 3 && whites==3){
                countWHori += unwinnable;
                countBHori += unwinnable;
            }


            //Run value for row in evaluation scheme and add to total score
            int valuew = consecutivevalue(countWHori);
            whitescore = whitescore +valuew;
            int valueb = consecutivevalue(countBHori);
            blackscore = blackscore + valueb;
            //System.out.println("Black consec hori " + valueb + "White consec hori  " + valuew);

        }

        //Check Verticals
        for (int y = 0; y <6 ; y++) {
            int countWvert = 0;
            int countBvert = 0;
            int blacks = 0;
            int whites = 0;


            for (int x = 0; x <5 ; x++) {
                if (pieces[x][y].ordinal() == 0) {

                    //countBvert = countBvert +1;
                    blacks++;
                }
                if (pieces[x][y].ordinal() == 1) {

                    //countWvert = countWvert +1;
                    whites++;
                }
                if (pieces[x][y].ordinal() == 0 && pieces[x+1][y].ordinal() == 0 ) {

                    countBvert = countBvert +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y].ordinal() == 2 ) {

                    countBvert = countBvert + blankspaceconsec;
                }
                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y].ordinal() == 0 ) {

                    countBvert = countBvert + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y].ordinal() == 1 ) {

                    countWvert = countWvert +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y].ordinal() == 2 ) {

                    countWvert = countWvert + blankspaceconsec;
                }
                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y].ordinal() == 1 ) {

                    countWvert = countWvert + blankspaceconsec;
                }

                if (x!=4){
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y].ordinal() == 0 && pieces[x+1][y].ordinal() == 1){
                        countBvert = countBvert +disjointset;
                    }
                    else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y].ordinal() == 1 && pieces[x+1][y].ordinal() == 0){
                        countWvert = countWvert +disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y].ordinal() == 2 && pieces[x+1][y].ordinal() == 0){
                        countBvert = countBvert +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y].ordinal() == 2 && pieces[x+1][y].ordinal() == 1){
                        countWvert = countWvert +jointset;
                    }
                }
            }

            if (blacks == 4 && whites==2){
                countBvert += unwinnable;
            }
            if (blacks == 3 && whites==3){
                countBvert += unwinnable;
                countWvert += unwinnable;
            }
            if (blacks == 2 && whites==4){
                countWvert += unwinnable;
            }
            //System.out.println("Black consec vert " + countBvert + "White consec  " + countWvert);
            int valuew = consecutivevalue(countWvert);
            whitescore = whitescore +valuew;
            int valueb = consecutivevalue(countBvert);
            blackscore = blackscore + valueb;
            //System.out.println("Black consec VERT " + valueb + "White consec hori  " + valuew);
        }

        //S West N EAST  Top
        for (int a = 1; a <6 ; a++) { //loop through all diagonal lines
            int countWdiagSoNe = 0;
            int countBdiagSoNe = 0;
            int x=0;
            int blacks = 0;
            int whites = 0;


            for (int y = a; y !=0 ; y--) { //loop through one diagonal line

                int r = pieces[x][y].ordinal();
                int s =pieces[x+1][y-1].ordinal();
                //System.out.println("x " + x+ " y " +y);
                //System.out.println("first " + r+ " second " +s);
                if (pieces[x][y].ordinal() == 0) {

                    //countBdiagSoNe = countBdiagSoNe +2;
                    blacks++;
                }
                if (pieces[x][y].ordinal() == 1 ) {

                    //countWdiagSoNe = countWdiagSoNe +2;
                    whites++;
                }
                if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 ) {

                    countBdiagSoNe = countBdiagSoNe +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 ) {

                    countBdiagSoNe = countBdiagSoNe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y-1].ordinal() == 0 ) {

                    countBdiagSoNe = countBdiagSoNe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 ) {

                    countWdiagSoNe = countWdiagSoNe +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 ) {

                    countWdiagSoNe = countWdiagSoNe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y-1].ordinal() == 1 ) {

                    countWdiagSoNe = countWdiagSoNe + blankspaceconsec;
                }

                // check for joint and disjoint set at these x y coordinates by looking 2 pieces ahead
                if (x==0 && y==4){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                if (x==1 && y==3){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                if (x==2 && y==2){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                if (x==0 && y==5){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                if (x==1 && y==4){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                if (x==2 && y==3){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                if (x==3 && y==2){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }

                x++;


            }
            if (blacks == 4 && whites==2){
                countBdiagSoNe += unwinnable;
            }
            if (blacks == 3 && whites==3){
                countBdiagSoNe += unwinnable;
                countWdiagSoNe += unwinnable;
            }
            if (blacks == 2 && whites==4){
                countWdiagSoNe += unwinnable;
            }
            if (a==4 && blacks == 4 && whites==1){
                countBdiagSoNe += unwinnable;
            }
            if (a==4 && blacks == 1 && whites==4){
                countWdiagSoNe += unwinnable;
            }

            //System.out.println("Black consec vert " + countBdiagSoNe + "White consec  " + countWdiagSoNe);
            int valuew = consecutivevalue(countWdiagSoNe);
            whitescore = whitescore +valuew;
            int valueb = consecutivevalue(countBdiagSoNe);
            blackscore = blackscore + valueb;
            //System.out.println("Black consec DIAGOMAAL " + valueb + "  " + countBdiagSoNe + "White consec hori  " + valuew + " " + countWdiagSoNe);
        }
        //S West N EAST  Bot
        for (int a = 1; a <5 ; a++) {
            int countWdiagSoNe = 0;
            int countBdiagSoNe = 0;
            int y=5;
            int blacks = 0;
            int whites = 0;

            for (int x = a; x <5 ; x++) {

                int r = pieces[x][y].ordinal();
                int s =pieces[x+1][y-1].ordinal();
                //System.out.println("x " + x+ " y " +y);
                //System.out.println("first " + r+ " second " +s);
                if (pieces[x][y].ordinal() == 0) {

                    //countBdiagSoNe = countBdiagSoNe +
                    blacks++;
                }
                if (pieces[x][y].ordinal() == 1 ) {

                    //countWdiagSoNe = countWdiagSoNe +2;
                    whites++;
                }
                if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 ) {

                    countBdiagSoNe = countBdiagSoNe +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 ) {

                    countBdiagSoNe = countBdiagSoNe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y-1].ordinal() == 0 ) {

                    countBdiagSoNe = countBdiagSoNe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 ) {

                    countWdiagSoNe = countWdiagSoNe +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 ) {

                    countWdiagSoNe = countWdiagSoNe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y-1].ordinal() == 1 ) {

                    countWdiagSoNe = countWdiagSoNe + blankspaceconsec;
                }
                if (x==1 && y==5){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                if (x==2 && y==4){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                if (x==3 && y==3){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 1 && pieces[x+2][y-2].ordinal() == 0) {

                        countWdiagSoNe = countWdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 0 && pieces[x+2][y-2].ordinal() == 1) {

                        countBdiagSoNe = countBdiagSoNe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 1) {

                        countWdiagSoNe = countWdiagSoNe +jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y-1].ordinal() == 2 && pieces[x+2][y-2].ordinal() == 0) {

                        countBdiagSoNe = countBdiagSoNe + jointset;
                    }
                }
                //System.out.println(x + "   y:" + y +" Black consec DIAGOMAAL " + countBdiagSoNe + "White consec hori  "  + countWdiagSoNe);
                y--;


            }
            if (a==1 && blacks == 4 && whites==1){
                countBdiagSoNe += unwinnable;
            }
            /*if (blacks == 3 && whites==3){
                countBdiagSoNe += unwinnable;
                countWdiagSoNe += unwinnable;
            }*/
            if (a==1&& blacks == 1 && whites==4){
                countWdiagSoNe += unwinnable;
            }
            //System.out.println("Black consec vert " + countBdiagSoNe + "White consec  " + countWdiagSoNe);
            int valuew = consecutivevalue(countWdiagSoNe);
            whitescore = whitescore +valuew;
            int valueb = consecutivevalue(countBdiagSoNe);
            blackscore = blackscore + valueb;
            //System.out.println("Black consec DIAGOMAAL " + valueb + "  " + countBdiagSoNe + "White consec hori  " + valuew + " " + countWdiagSoNe);
        }

        //NorthWest S EAST  Left
        for (int a = 0; a <5 ; a++) {
            int countWdiagNoSe = 0;
            int countBdiagNoSe = 0;
            int y=0;
            int blacks = 0;
            int whites = 0;

            for (int x = a; x <5 ; x++) {
                //System.out.println(pbs+"x " + x+ " y " +y);
                int r = pieces[x][y].ordinal();
                int s =pieces[x+1][y+1].ordinal();
                //System.out.println("x " + x+ " y " +y);
                //System.out.println("first " + r+ " second " +s);
                if (pieces[x][y].ordinal() == 0) {
                    blacks++;
                    //countBdiagNoSe = countBdiagNoSe +2;
                }
                if (pieces[x][y].ordinal() == 1) {
                    whites++;
                    //countWdiagNoSe = countWdiagNoSe +2;
                }

                if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 ) {

                    countBdiagNoSe = countBdiagNoSe +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 ) {

                    countBdiagNoSe = countBdiagNoSe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y+1].ordinal() == 0 ) {

                    countBdiagNoSe = countBdiagNoSe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 ) {

                    countWdiagNoSe = countWdiagNoSe +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 ) {

                    countWdiagNoSe= countWdiagNoSe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y+1].ordinal() == 1 ) {

                    countWdiagNoSe = countWdiagNoSe + blankspaceconsec;
                }

                if (x==0 && y==0){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                if (x==1 && y==1){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                if (x==2 && y==2){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                if (x==3 && y==3){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                if (x==1 && y==0){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                if (x==2 && y==1){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                if (x==3 && y==2){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                y++;


            }
            //System.out.println("Black consec vert " + countBdiagSoNe + "White consec  " + countWdiagSoNe);
            if (blacks == 4 && whites==2){
                countBdiagNoSe += unwinnable;
            }
            if (a==1 && blacks == 4 && whites==1){
                countBdiagNoSe += unwinnable;
            }
            if (blacks == 3 && whites==3){
                countBdiagNoSe += unwinnable;
                countWdiagNoSe += unwinnable;
            }
            if (blacks == 2 && whites==4){
                countWdiagNoSe += unwinnable;
            }
            if (a==1 && blacks == 1 && whites==4){
                countWdiagNoSe += unwinnable;
            }

            int valuew = consecutivevalue(countWdiagNoSe);
            whitescore = whitescore +valuew;
            int valueb = consecutivevalue( countBdiagNoSe);
            blackscore = blackscore + valueb;
            //System.out.println("Black consec DIAGOMAAL " + valueb + "  " +  countBdiagNoSe + "White consec hori  " + valuew + " " +  countWdiagNoSe);
        }
        //NorthWest S EAST Right
        for (int a = 1; a <5 ; a++) {
            int countWdiagNoSe = 0;
            int countBdiagNoSe = 0;
            int x=0;
            int blacks = 0;
            int whites = 0;

            for (int y = a; y <5 ; y++) {
                //System.out.println("x " + x+ " y " +y);
                int r = pieces[x][y].ordinal();
                int s =pieces[x+1][y+1].ordinal();
                //System.out.println("x " + x+ " y " +y);
                //System.out.println("first " + r+ " second " +s);
                if (pieces[x][y].ordinal() == 0 ) {
                    blacks++;
                    //countBdiagNoSe = countBdiagNoSe +2;
                }
                if (pieces[x][y].ordinal() == 1) {
                    whites++;
                    //countWdiagNoSe = countWdiagNoSe +2;
                }

                if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 ) {

                    countBdiagNoSe = countBdiagNoSe +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 ) {

                    countBdiagNoSe = countBdiagNoSe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y+1].ordinal() == 0 ) {

                    countBdiagNoSe = countBdiagNoSe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 ) {

                    countWdiagNoSe = countWdiagNoSe +consecvalue;
                }

                else if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 ) {

                    countWdiagNoSe= countWdiagNoSe + blankspaceconsec;
                }

                else if (pieces[x][y].ordinal() == 2 && pieces[x+1][y+1].ordinal() == 1 ) {

                    countWdiagNoSe = countWdiagNoSe + blankspaceconsec;
                }

                if (x==0 && y==1){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                if (x==1 && y==2){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                if (x==2 && y==3){
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 1 && pieces[x+2][y+2].ordinal() == 0) {

                        countWdiagNoSe = countWdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 0 && pieces[x+2][y+2].ordinal() == 1) {

                        countBdiagNoSe = countBdiagNoSe + disjointset;
                    }
                    if (pieces[x][y].ordinal() == 1 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 1) {

                        countWdiagNoSe = countWdiagNoSe + jointset;
                    }
                    else if (pieces[x][y].ordinal() == 0 && pieces[x+1][y+1].ordinal() == 2 && pieces[x+2][y+2].ordinal() == 0) {

                        countBdiagNoSe = countBdiagNoSe + jointset;
                    }
                }
                x++;


            }
            if (a==1 && blacks == 1 && whites==4){
                countWdiagNoSe += unwinnable;
            }
            if (a==1 && blacks == 4 && whites==1){
                countBdiagNoSe += unwinnable;
            }
            //System.out.println("Black consec vert " + countBdiagSoNe + "White consec  " + countWdiagSoNe);
            int valuew = consecutivevalue(countWdiagNoSe);
            whitescore = whitescore +valuew;
            int valueb = consecutivevalue( countBdiagNoSe);
            blackscore = blackscore + valueb;
            //System.out.println("Black consec DIAGOMAAL " + valueb + "  " +  countBdiagNoSe + "White consec hori  " + valuew + " " +  countWdiagNoSe);
        }





        //System.out.println("Black consec score   " + blackscore +"   " + "White consec scpre  " + whitescore);
        //System.out.println("max player is " + maxplayer + "My colour is " + mycolour);
        int i = -123456789;
        if (mycolour == 0){
            //System.out.println(blackscore-whitescore +"  I am black");


            i=  blackscore-whitescore;
            return i;

        }
        else {
            //System.out.println(whitescore-blackscore +"  I am white");
            i= whitescore-blackscore;
            return i;
        }
        /*
        if(i>0 && i<1000){
            return 100; //i*2;
        }
        else if(i>=1000 && i<10000){
            return 1000; //i*10;
        }
        else if(i>=10000 && i<100000){
            return 10000; //i*50;
        }
        else if(i>=100000){
            return 100000;//i*500;
        }
        else if(i<=0 && i>-100){
            return -100; //i*2;
        }
        else if(i<=-100 && i>-1000){
            return -1000; //i*10;
        }
        else if(i<=-1000 && i>-10000){
            return -10000; //i*50;
        }
        else if(i<=0 && i>-100000){
            return -100000;//i*500;
        }

         */


    }

    //score transformation function
    public int consecutivevalue(int i){ // Transforms value of row,column or diag based on how many moves left to win with a magnitude of 10

       if(i>=125 && i<195){ //3 moves to win
            return i*10;
        }
        else if(i>=195 && i<320){ //2 moves to win
            return i*50;
        }
        else if(i>=320 && i<430){ //1 move to win
            return i*500;
        }
        else if(i>=430){  //winner
            return i*5000;
        }
        else if(i<125){
            return i;
        }
        else{
            return 0;
        }


    }

    //alpha beta implementation used by Student Player
    public MoveNode ab(PentagoBoardState pbs,int myscore, int alpha, int beta,int amimaxplayer, int depth,int requireddepth){

        int bestmovescore;
        PentagoMove bestmove;

        ArrayList<PentagoMove> legalmoves = pbs.getAllLegalMoves();
        Collections.shuffle(legalmoves); //helps avoid worst run time if all good moves are at the end of array list

        MoveNode moveNode =new MoveNode(new PentagoMove(0,0,0,0,0),0);

        if (depth==requireddepth){
            bestmovescore= evaluate(pbs);
            moveNode.MyMove = null;
            moveNode.value = evaluate(pbs);
            return moveNode;

        }
        else if (pbs.getWinner() != Board.NOBODY){ //check win since we are at leafnode
            bestmovescore= evaluate(pbs);
            moveNode.MyMove = null;
            int movescore = evaluate(pbs);
            if (movescore<-1200000){
                moveNode.value = -999999999; //assign loss value
            } //Losing Move
            else if (movescore>1200000){
                moveNode.value = 999999999; //assign win value
            } // Winning Move
            return moveNode;

        }
        //do max player
        if (amimaxplayer ==1){
            int currentvalue = -999999999;
            PentagoMove currentmove =legalmoves.get(0);
            MoveNode currentmovenode = new MoveNode(currentmove,currentvalue);
            for (int i = 0; i < legalmoves.size(); i++) {
                PentagoBoardState newboard = (PentagoBoardState) pbs.clone();
                PentagoMove movetoprocess = legalmoves.get(i);
                newboard.processMove(movetoprocess);


                MoveNode scoreAfterMove = ab(newboard, currentvalue, alpha, beta, 0, depth + 1, requireddepth); // run recursion till leaf node or depth reached
                if (scoreAfterMove.value > currentvalue) { //see if current best value is lower than the new value if so replace
                        currentvalue = scoreAfterMove.value;
                        currentmove = movetoprocess;
                        currentmovenode.value = currentvalue;
                        currentmovenode.MyMove = currentmove;

                        //System.out.println("I am max plyaer at depth:" + depth + "\n current move is: " + currentmove + "\ncurrentvalue is: " + currentvalue);

                    }

                alpha = Math.max(alpha,currentvalue);
                if (alpha>=beta){ // break loop if alpha more than beta
                    //System.out.println("Get pruned my alpha is " + alpha + "  my beta is  " + beta );
                    break;
                }
            }
            //System.out.println("I am max plyaer at depth:" + depth + "\n current move is: " + currentmove + "\ncurrentvalue is: " + currentvalue+" my alpha is " + alpha + "  my beta is  " + beta);

            return currentmovenode;
        }
        //do min player
        else if (amimaxplayer==0){
            int currentvalue = 999999999;
            PentagoMove currentmove =legalmoves.get(0);
            MoveNode currentmovenode = new MoveNode(currentmove,currentvalue);
            for (int i = 0; i < legalmoves.size(); i++) {
                PentagoBoardState newboard = (PentagoBoardState) pbs.clone();
                PentagoMove movetoprocess = legalmoves.get(i);
                newboard.processMove(movetoprocess);

                MoveNode scoreAfterMove = ab(newboard, currentvalue, alpha, beta, 1, depth + 1, requireddepth);
                if (scoreAfterMove.value < currentvalue) {
                    currentvalue = scoreAfterMove.value;
                    currentmove = movetoprocess;
                    currentmovenode.value = currentvalue;
                    currentmovenode.MyMove = currentmove;
                    //System.out.println("I am min plyaer at depth:" + depth + "\n current move is: " + currentmove + "\ncurrentvalue is: " + currentvalue);
                }
                beta = Math.min(beta,currentvalue);
                if (alpha>=beta){
                    //System.out.println("Get pruned my alpha is " + alpha + "  my beta is  " + beta );
                   break;
                }
            }
            return currentmovenode;
        }
        //System.out.println("move val " + moveNode.value);
        return moveNode;
    }

    //checks if I can win in one move and returns that move
    public PentagoMove quickwin(PentagoBoardState boardState) {
        PentagoBoardState pbs = (PentagoBoardState) boardState.clone();
        ArrayList<PentagoMove> legalmoves = pbs.getAllLegalMoves();
        //ArrayList<Integer> scores = new ArrayList<Integer>();
        for (int i = 0; i < legalmoves.size(); i++) { //loop through all my moves and see if they are a winner

            PentagoBoardState newboard = (PentagoBoardState) pbs.clone();
            PentagoMove next = legalmoves.get(i);
            newboard.processMove(next);
            int score = evaluate(newboard);
            if (score > 1200000) { //check if winner if score above 1 200 000
                //System.out.println("FOUND A WINNER" + score);
                return next;
            }
        }
        return null; // if no winner return null
    }
    // TESTER Basic alpha beta with depth 2 so I can give this to Randomplayer for autoplay tests
    public PentagoMove alphabetaw(PentagoBoardState boardState){
        PentagoBoardState pbs = (PentagoBoardState) boardState.clone();
        ArrayList<PentagoMove> legalmoves = pbs.getAllLegalMoves();

        PentagoMove bestmove = legalmoves.get(0);
        PentagoMove bestopponentmove = new PentagoMove(0,0,0,0,0);
        int bestmovescore = -9999999;
        int bestwhitescore = 0;
        //ArrayList<Integer> scores = new ArrayList<Integer>();
        for(int i = 0;i<legalmoves.size()-1;i++){

            PentagoBoardState newboard = (PentagoBoardState) pbs.clone();
            //System.out.println( pbs.getTurnPlayer() + "BEFORE PROCESS");
            PentagoMove next = legalmoves.get(i);

            newboard.processMove(next);

            int score = evaluate(newboard);
            //scores.add(score);
            //System.out.println( pbs.getTurnPlayer() + " AFTER PROCES");

            if(score > 100000){
                newboard.getWinner();
                newboard.printBoard();
                System.out.println("FOUND A WINNER" + score);
                return next;
            }


            PentagoMove currentopponentmove;
            ArrayList<PentagoMove> opponentlegalmoves = newboard.getAllLegalMoves();
            if (opponentlegalmoves.size()!=0){
                currentopponentmove = opponentlegalmoves.get(0);
            }

            int minopponentmovescore = 999999;
            int thismovescore = -9999;
            ArrayList<PentagoMove> bestopponentmoves = new ArrayList<PentagoMove>();
            PentagoMove currentbestopponentmove = new PentagoMove(0,0,0,0,0);

            for(int a = 0;a<opponentlegalmoves.size()-1;a++){

                PentagoBoardState pbsopponent = (PentagoBoardState) newboard.clone();
                //System.out.println( pbs.getTurnPlayer() + "BEFORE PROCESS");
                PentagoMove opponentnext = opponentlegalmoves.get(a);

                pbsopponent.processMove(opponentnext);
                //System.out.println( pbs.getTurnPlayer() + " AFTER PROCES");
                //pbs.printBoard();

                int opponentscore = evaluate(pbsopponent);





                if (minopponentmovescore>opponentscore){
                    //currentopponentmove = opponentnext;
                    minopponentmovescore = opponentscore;
                    bestopponentmoves.add(opponentnext);
                    currentbestopponentmove = opponentnext;
                }


            }
            bestopponentmove = currentbestopponentmove;
            //lvl 3
            /*

            int lvl3minscore =99999;
            PentagoMove currentmaxmove;
            for (int r = 0;r<bestopponentmoves.size()-1;r++) {
                PentagoBoardState pbsopponent = (PentagoBoardState) newboard.clone();
                pbsopponent.processMove(bestopponentmoves.get(r));

                ArrayList<PentagoMove> maxlegalmoves = pbsopponent.getAllLegalMoves();
                if (maxlegalmoves.size() != 0) {
                    currentmaxmove = maxlegalmoves.get(0);
                }
                int opponentscore = evaluate(pbsopponent);
                int findminmaxmovescore = -99999;
                for (int s = 0; s < maxlegalmoves.size() - 1; s++) {

                    PentagoBoardState maxboard = (PentagoBoardState) pbsopponent.clone();
                    //System.out.println( pbs.getTurnPlayer() + "BEFORE PROCESS");
                    PentagoMove maxnext = maxlegalmoves.get(s);

                    maxboard.processMove(maxnext);
                    //System.out.println( pbs.getTurnPlayer() + " AFTER PROCES");
                    //pbs.printBoard();

                    int maxnextscore = evaluate(pbsopponent);
                    if (findminmaxmovescore < maxnextscore) {
                        currentmaxmove = maxnext;
                        findminmaxmovescore = maxnextscore;
                    }
                }
                if (thismovescore<findminmaxmovescore){
                    //currentopponentmove = opponentnext;
                   thismovescore = findminmaxmovescore;
                }

                //opponentscore = maxmovescore;
            }

            //end experiment




            if (mycolour ==1){
                lvl3minscore =minopponentmovescore;
            }
            */
            if (minopponentmovescore>bestmovescore){//minopponentmovescore
                System.out.println("max player move score: "+score + "Min : " + minopponentmovescore);
                bestmovescore = minopponentmovescore;
                bestmove = next;
                bestwhitescore = score;

            }
            else if (minopponentmovescore == bestmovescore){
                if (score > bestwhitescore){
                    System.out.println("max player move score: "+score + "Min : " + minopponentmovescore);
                    bestmovescore = minopponentmovescore;
                    bestmove = next;
                    bestwhitescore = score;
                }



            }


        }
        System.out.println("final max player move score: "+ bestmovescore + "\n My best move: "+bestmove.toPrettyString() + "\n My best move: "+ bestopponentmove.toPrettyString());
        return bestmove;
    }

}