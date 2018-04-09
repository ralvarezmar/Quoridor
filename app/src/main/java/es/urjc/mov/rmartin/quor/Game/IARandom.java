package es.urjc.mov.rmartin.quor.Game;

import java.util.ArrayList;
import java.util.Random;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;

public class IARandom extends Player {

    public IARandom(Board board) {
        super(board);
    }

    @Override
    public Box getMove(){
        Box cpu=board.getCpu();
        Box move;
        int casillaY;
        do {
            if(cpu==null) { //primer movimiento
                casillaY = (int) (Math.random() * board.game[0].length);
                move = board.getPress(0, casillaY);
            }else{
                cpu.setStatus(Box.Status.FREE);
                ArrayList<Box> boxes = board.aroundBoxes(cpu);
                int rnd = new Random().nextInt(boxes.size());
                move=boxes.get(rnd);
            }
        }
        while (!boxOk(move));
        move.setStatus(Box.Status.CPU);
        return move;
    }


    @Override
    public Box putWall(){
        Box wall;
        int casillaX;
        int casillaY;
        do{
            casillaX = (int) (Math.random() * board.game.length);
            casillaY = (int) (Math.random() * board.game.length);
            wall=board.getPress(casillaX,casillaY);
        }while(!boxOk(wall));
        wall.setStatus(Box.Status.WALL);
        return wall;
    }

    @Override
    public boolean isFreeBox(Box pressed) {
        return false;
    }

    @Override
    public boolean putWall(Box pressed) {
        return false;
    }
}
