package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;
import es.urjc.mov.rmartin.quor.R;

public class Human extends Player {
    Board b;
    public Human(Board b){
        super(b);
        this.b=b;}

    public boolean isFirstMove(Box pressed,Box b){
        return (b==null && (pressed.getCoordenate().getX()==(this.b.game.length-1)) && pressed.getStatus()== Status.FREE);
    }

    @Override
    public boolean isFreeBox(Box pressed,Status player){
        Box b=board.getPlayer(player);
        if(isFirstMove(pressed,b)) {
            pressed.setStatus(player);
            return true;
        }else if(isMoveValid(pressed, player)) {
            pressed.setStatus(player);
            b.setStatus(Status.FREE);
            return true;
        }
        return false;
    }

    private boolean canWall(Box player1,Box player2){
        Dijkstra play1 = new Dijkstra(board,player1);
        ArrayList wayPlayer1 = play1.doWay(0);
        Dijkstra play2 = new Dijkstra(board,player2);
        ArrayList wayPlayer2 = play2.doWay(board.game.length-1);
        if(wayPlayer1.size()==0 || wayPlayer2.size()==0){
            return false;
        }
        return true;
    }

    private boolean putWall(Box pressed){
        return pressed.getStatus()==Status.FREE;
    }

    @Override
    public synchronized Move askPlay(Box pressed,Status statusPlayer,Status statusOpposite, boolean checked){
        if(checked && isFreeBox(pressed,statusPlayer)){
            Move m= new Move(pressed.getCoordenate(),false);
            return m;
        } else if(!checked){//pared
            if(putWall(pressed)){
                Log.v("askPlay","entra if");
                pressed.setStatus(Status.WALL);
                Box p1=b.getPlayer(statusPlayer);
                Box p2=b.getPlayer(statusOpposite);
                if(canWall(p2,p1)){
                    Move m= new Move(pressed.getCoordenate(),false);
                    return m;
                }
                pressed.setStatus(Status.FREE);
                return null;
                //pressed.setStatus(Status.FREE);
            }
        }
        return null;
    }//quitar el cambio de estados aqui y comprobar si es valida o no después de devolver la jugada(en game activity)

    @Override
    public Move putPlay(Coordinate c, boolean checked) {
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
Si es un humano el pedir jugada se va a quedar tostao hasta que se la rellenes con el meter jugada, que eso lo haces desde el onClick
Y si es ia pues rellena la jugada como siempre
 */