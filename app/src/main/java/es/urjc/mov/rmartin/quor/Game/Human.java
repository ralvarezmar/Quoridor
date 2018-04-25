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


    @Override
    public boolean isFreeBox(Box pressed,Status player){
        //Box b=board.getPlayer(player);
       return isMoveValid(pressed, player);
       /*     pressed.setStatus(player);
            b.setStatus(Status.FREE);
            return true;
        }
        return false;*/
    }

    private boolean checkWall(Box player1,Box player2){
        Dijkstra play1 = new Dijkstra(board,player1);
        ArrayList wayPlayer1 = play1.doWay(0);
        Dijkstra play2 = new Dijkstra(board,player2);
        ArrayList wayPlayer2 = play2.doWay(board.game.length-1);
        if(wayPlayer1.size()==0 || wayPlayer2.size()==0){
            return false;
        }
        return true;
    }
    @Override
    public boolean canWall(Box pressed){
        Box player1=board.getPlayer(Status.PLAYER1);
        Box player2=board.getPlayer(Status.PLAYER2);
        return checkWall(player1,player2) && pressed.getStatus()==Status.FREE;
    }

    @Override
    public synchronized Move askPlay(Status statusPlayer) throws InterruptedException {
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
            return true;
        }if(m.getType() && isFreeBox(pressed,status)){
            return true;
        }
        return false;
    }

    @Override
    public  Move putPlay(Coordinate c, boolean checked) {
        //Log.v("Thread", "Pido jugada");
        synchronized (monitor){
            monitor.notify();
        }
        //monitor.notifyAll();
        Move move = new Move(c,checked);
        return move;
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

/*
Tu desde el hilo que lanzas pides jugada da igual el que sea
Si es un humano el pedir jugada se va a quedar dormido hasta que se la rellenes con el meter jugada que lo despierta, que eso lo haces desde el onClick
Y si es ia pues rellena la jugada como siempre
 */