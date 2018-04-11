package es.urjc.mov.rmartin.quor.Game;

import java.util.ArrayList;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;

public abstract class Player {
    Board board;

    Player(Board board) {
        this.board=board;
    }

    boolean boxOk(Box box){
        return !((box == null || (box.getX() < 0 && box.getX() > (board.game[0].length-1))) ||
                (box.getY() < 0 && box.getY() > (board.game[0].length-1) || box.getStatus() != Box.Status.FREE));
    }

    boolean isMoveValid(Box pressed, Box.Status player){
        Box b=board.getPlayer(player);
        ArrayList<Box> aroundBoxes= board.aroundBoxes(b);
        return (b!= null && pressed.getStatus()==Box.Status.FREE && aroundBoxes.contains(pressed));
    }

    public abstract Box getMove(int destiny);

    public abstract Box putWall(int destiny);

    public abstract boolean isFreeBox(Box pressed);
    public abstract boolean putWall(Box pressed);
}
