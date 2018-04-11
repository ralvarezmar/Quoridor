package es.urjc.mov.rmartin.quor.Game;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;

public class Human extends Player {
    Board b;
    public Human(Board b){
        super(b);
        this.b=b;}

    public boolean isFirstMove(Box pressed,Box b){
        return (b==null && (pressed.getX()==(this.b.game.length-1)) && pressed.getStatus()== Box.Status.FREE);
    }

    @Override
    public boolean isFreeBox(Box pressed){
        Box b=board.getPlayer(Box.Status.PLAYER);
        if(isFirstMove(pressed,b)) {
            pressed.setStatus(Box.Status.PLAYER);
            return true;
        }else if(isMoveValid(pressed, Box.Status.PLAYER)) {
            pressed.setStatus(Box.Status.PLAYER);
            b.setStatus(Box.Status.FREE);
            return true;
        }
        return false;
    }

    @Override
    public boolean putWall(Box pressed){
        return pressed.getStatus()==Box.Status.FREE;
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
