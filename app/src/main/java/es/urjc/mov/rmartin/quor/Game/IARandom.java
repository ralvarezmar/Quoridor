package es.urjc.mov.rmartin.quor.Game;

import java.util.ArrayList;
import java.util.Random;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class IARandom extends Player {

    public IARandom(Board board) {
        super(board);
    }
    private static final int PORCENTAJE = 20;

    @Override
    public Box getMove(int destiny,Status player){
        Box cpu=board.getPlayer(player);
        Box move;
        int casillaY;
        do {
            if(cpu==null) { //primer movimiento
                casillaY = (int) (Math.random() * board.game[0].length);
                move = board.getPress(0, casillaY);
            }else{
                cpu.setStatus(Status.FREE);
                ArrayList<Box> boxes = board.aroundBoxes(cpu);
                int rnd = new Random().nextInt(boxes.size());
                move=boxes.get(rnd);
            }
        }
        while (!boxOk(move));
        move.setStatus(Status.PLAYER1);
        return move;
    }

    @Override
    public Box putWall(int destiny,Status play){
        Box wall;
        int casillaX;
        int casillaY;
        do{
            casillaX = (int) (Math.random() * board.game.length);
            casillaY = (int) (Math.random() * board.game.length);
            wall=board.getPress(casillaX,casillaY);
        }while(!boxOk(wall));
        wall.setStatus(Status.WALL);
        return wall;
    }

    @Override
    public synchronized Move askPlay(Box pressed,Status statusPlayer,Status statusOpposite, boolean checked){
        int destinyPlayer;
        int destinyOpposite;
        if(statusPlayer==Status.PLAYER1){
            destinyPlayer=board.game.length-1;
            destinyOpposite=0;
        }else{
            destinyPlayer=0;
            destinyOpposite=board.game.length-1;
        }
        int move = (int) (Math.random() * 100);
        if (move > PORCENTAJE){
            Box casilla= getMove(destinyPlayer, statusPlayer);
            Move m=new Move(casilla.getCoordenate(),true);
            return m;
        }
        Box casilla=putWall(destinyOpposite,Status.PLAYER2);
        Move m=new Move(casilla.getCoordenate(),true);
        return m;
    }

    @Override
    public Move putPlay(Coordinate c, boolean checked) {
        return null;
    }

    @Override
    public boolean isFreeBox(Box pressed,Status player) {
        return false;
    }

}
