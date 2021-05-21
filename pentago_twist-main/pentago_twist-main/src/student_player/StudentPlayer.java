package student_player;

import boardgame.Move;

import pentago_twist.PentagoCoord;
import pentago_twist.PentagoMove;
import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

import java.util.ArrayList;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260930902");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...

       MyTools evaluator = new MyTools(boardState.getTurnPlayer()); //initialize a new evaluator class with the colour of the max player
        long startTime = System.nanoTime();



       int currentturn  =boardState.getTurnNumber();


        ArrayList<PentagoMove> legalmoves = new ArrayList<PentagoMove>();
        legalmoves = boardState.getAllLegalMoves();
        PentagoMove myMove = legalmoves.get(0); //default

        //check if we can triple power play
        if (currentturn<2) {

            int mycolour = 99;
            int enemycolour = 99;
            //based on ordinal value 0 is Black and 1 is White
            if (boardState.getTurnPlayer() == 0) {
                mycolour = 1;
                enemycolour = 0;
            } else {
                mycolour = 0;
                enemycolour = 1;
            }

            int openingStrategy = 0;
            int a = boardState.getPieceAt(3, 4).ordinal();
            int b = boardState.getPieceAt(4, 4).ordinal();

            int f = boardState.getPieceAt(4, 1).ordinal();
            int g = boardState.getPieceAt(3, 1).ordinal();



            if (a != enemycolour && b != enemycolour) {
                openingStrategy = 1;
            }
            else if (f != enemycolour && g != enemycolour ) {
                openingStrategy = 2;
            }




            if (openingStrategy ==0){ //opponent blocked opening
                MyTools.MoveNode moveNodecurrentmove = evaluator.ab(boardState, 0, -999999999, 999999999, 1, 1, 2);
                myMove = moveNodecurrentmove.MyMove;
            }
            else if (openingStrategy ==1 && currentturn ==0){ //do opening 1
                myMove = new PentagoMove(3,4,0,0,260930902);
            }
            else if (openingStrategy ==1 && currentturn ==1){
                myMove = new PentagoMove(4,4,0,0,260930902);
            }
            else if (openingStrategy ==2 && currentturn ==0){ //do opening 2
                myMove = new PentagoMove(3,1,1,1,260930902);
            }
            else if (openingStrategy ==2 && currentturn ==1){
                myMove = new PentagoMove(4,1,3,0,260930902);
            }


        }


        if (currentturn >=2 && currentturn<=14 ) {
            int requireddepth = 3;
            evaluator.jointset = 0;// to avoid getting false wins
            evaluator.blankspaceconsec = 0;
            PentagoMove quickwin = evaluator.quickwin(boardState);
            if (quickwin!=null){
                myMove = quickwin;
            }
            else {
                evaluator.jointset = 70; //reset to default
                evaluator.blankspaceconsec = 25;
                MyTools.MoveNode moveNodecurrentmove = evaluator.ab(boardState, 0, -999999999, 999999999, 1, 1, requireddepth);
                //System.out.println("Chosen Move: " + moveNodecurrentmove.value);
                myMove = moveNodecurrentmove.MyMove;
            }

        }
        //allow alphabeta to go into depth 4 as now branching factor is lower and will not timeout
        else if (currentturn >14){
            evaluator.jointset = 0;// to avoid getting false wins
            evaluator.blankspaceconsec = 0;
            PentagoMove quickwin = evaluator.quickwin(boardState);
            if (quickwin!=null){
                myMove = quickwin;
            }
            else {
                int requireddepth = 0;
                if (boardState.getTurnPlayer() == 1){
                    requireddepth =4 ;
                }
                else {
                    requireddepth = 4;
                }
                evaluator.jointset = 70; //reset to default
                evaluator.blankspaceconsec = 25;
                MyTools.MoveNode moveNodecurrentmove = evaluator.ab(boardState, 0, -999999999, 999999999, 1, 1, requireddepth);
                //System.out.println("Chosen Move: " + moveNodecurrentmove.value);
                myMove = moveNodecurrentmove.MyMove;
            }
        }
        //System.out.println("current turn is = " + currentturn);



        // Return your move to be processed by the server.
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime; //time checks
        //boardState.printBoard();
        //System.out.println(currentturn+"eval  " + evaluator.evaluate(boardState));
        //System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);
        return myMove;

    }

}

