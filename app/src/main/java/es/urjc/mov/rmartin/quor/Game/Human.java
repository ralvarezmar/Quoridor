package es.urjc.mov.rmartin.quor.Game;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class Human extends Player {
    Board b;
    public Human(Board b){
        super(b);
        this.b=b;}

    public boolean isFirstMove(Box pressed,Box b){
        return (b==null && (pressed.getX()==(this.b.game.length-1)) && pressed.getStatus()== Status.FREE);
    }

    @Override
    public boolean isFreeBox(Box pressed){
        Box b=board.getPlayer(Status.PLAYER2);
        if(isFirstMove(pressed,b)) {
            pressed.setStatus(Status.PLAYER2);
            return true;
        }else if(isMoveValid(pressed, Status.PLAYER2)) {
            pressed.setStatus(Status.PLAYER2);
            b.setStatus(Status.FREE);
            return true;
        }
        return false;
    }

    @Override
    public boolean putWall(Box pressed){
        return pressed.getStatus()==Status.FREE;
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
