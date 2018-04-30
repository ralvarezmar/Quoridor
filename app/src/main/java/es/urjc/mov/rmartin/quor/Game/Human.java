package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;

import java.util.ArrayList;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class Human extends Player {
    Board b;
    public Human(Board b){
        super(b);
        this.b=b;}

    private static final Object monitor = new Object();


    private boolean checkWall(Box player1,Box player2){
        Dijkstra play1 = new Dijkstra(board,player1);
        ArrayList wayPlayer1 = play1.doWay(0);
        Dijkstra play2 = new Dijkstra(board,player2);
        ArrayList wayPlayer2 = play2.doWay(board.game.length-1);
        if(wayPlayer1.size()<=1 || wayPlayer2.size()<=1){
            return false;
        }
        return true;
    }

    @Override
    public Move askPlay(Status statusPlayer) throws InterruptedException {
        Log.v("askPlay","Duerme humano");
        synchronized (monitor){
            monitor.wait();
        }
        return null;
    }


    public boolean validMove(Move m, Status status){
        Box player1=b.getPlayer(Status.PLAYER1);
        Box player2=b.getPlayer(Status.PLAYER2);
        Coordinate c= m.getC();
        Box pressed=b.getPress(c);
        if(!m.getType() && checkWall(player1,player2)){
           // pressed.setStatus(Status.WALL);
            return true;
        }else if(m.getType() && isMoveValid(pressed,status)){
            //pressed.setStatus(status);
            //b.getPlayer(status).setStatus(Status.FREE);
            return true;
        }
        return false;
    }

    @Override
    public void putPlay(Move move) {
        //Log.v("Thread", "Pido jugada");
        synchronized (monitor){
            monitor.notify();
        }
    }

    @Override
    public Box getMove(int destiny,Status player) {
        return null;
    }

    @Override
    public Box putWall(int destiny,Status play) {
        return null;
    }
}
