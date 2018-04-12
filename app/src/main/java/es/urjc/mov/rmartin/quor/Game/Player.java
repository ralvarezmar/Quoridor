package es.urjc.mov.rmartin.quor.Game;

import java.util.ArrayList;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public abstract class Player {
    Board board;
    Player(Board board) {
        this.board=board;
    }

    boolean boxOk(Box box){
        return !((box == null || (box.getX() < 0 && box.getX() > (board.game[0].length-1))) ||
                (box.getY() < 0 && box.getY() > (board.game[0].length-1) || box.getStatus() != Status.FREE));
    }

    boolean isMoveValid(Box pressed, Status player){
        Box b=board.getPlayer(player);
        ArrayList<Box> aroundBoxes= board.aroundBoxes(b);
        /*return ((pressed.getStatus()==Status.FREE) && ((b.getX()==x-1 && b.getY()==y)
                || (b.getX()==x+1 && b.getY()==y) ||
                (b.getY()==y-1 && b.getX()==x) || (b.getY()== y+1  && b.getX()==x))); //LIMPIAR */
        return (pressed.getStatus()==Status.FREE && aroundBoxes.contains(pressed));
    }


    public abstract Box getMove(int destiny);

    public abstract Box putWall(int destiny);

    public abstract boolean isFreeBox(Box pressed,Status player);
    public abstract boolean putWall(Box pressed);
}
