package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class Human extends Player {
    Board b;
    public Human(Board b){
        super(b);}

    public boolean isFirstMove(Box pressed,Box b){
        return (b==null && (pressed.getX()==(this.b.game.length-1)) && pressed.getStatus()== Status.FREE);
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
            Log.v("SWITCH", "entra2");
            return true;
        }
        return false;
    }

    @Override
    public boolean putWall(Box pressed){
        return pressed.getStatus()==Status.FREE;
    }

    @Override
    public Box getMove(int destiny) {
        return null;
    }

    @Override
    public Box putWall(int destiny) {
        return null;
    }
}
