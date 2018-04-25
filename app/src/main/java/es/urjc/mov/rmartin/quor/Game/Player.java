package es.urjc.mov.rmartin.quor.Game;

import java.util.ArrayList;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public abstract class Player {
    Board board;

    Player(Board board) {
        this.board=board;
    }

    boolean boxOk(Box box){
        int x= box.getCoordenate().getX();
        int y= box.getCoordenate().getY();
        return !((box == null || (x < 0 && x > (board.game[0].length-1))) ||
                (y < 0 && y > (board.game[0].length-1) || box.getStatus() != Status.FREE));
    }

    boolean isMoveValid(Box pressed, Status player){
        Box b=board.getPlayer(player);
        ArrayList<Box> aroundBoxes= board.aroundBoxes(b);
        return (b!= null && pressed.getStatus()==Status.FREE && aroundBoxes.contains(pressed));
    }


    public abstract Box getMove(int destiny,Status player);

    public abstract Box putWall(int destiny,Status play);

    public abstract boolean isFreeBox(Box pressed,Status player);

    public abstract Move askPlay(Status statusPlayer) throws InterruptedException;

    public abstract Move putPlay(Coordinate c, boolean checked);

    public abstract boolean canWall(Box pressed);
}
