package es.urjc.mov.rmartin.quor.Game;

import java.util.Random;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class IAMedium extends Player {
    Board b;
    public IAMedium(Board b){
        super(b);
        this.b=b;}

    @Override
    public Box getMove(int destiny,Status player){
        Random rand = new Random();
        Box cpu=b.getPlayer(player);
        Box move;
        int casillaX;
        int casillaY;
        do {
            move = b.getPress(cpu.getX() + 1, cpu.getY());
            if (move == null || move.getStatus() != Status.FREE) {
                casillaX = rand.nextInt(1 + 1 + 1) - 1;
                if (casillaX != 0) {
                    move = b.getPress(Math.abs(casillaX + cpu.getX()), cpu.getY());
                } else {
                    casillaY = rand.nextInt(1 + 1 + 1) - 1;
                    move = b.getPress(cpu.getX(), Math.abs(casillaY + cpu.getY()));
                }
            }
            cpu.setStatus(Status.FREE);
        }while (!boxOk(move));
        move.setStatus(Status.PLAYER2);
        return move;
    }

    @Override
    public Box putWall(int destiny,Status play){
        Box wall;
        Random rand = new Random();
        int casillaX;
        int casillaY;
        Box player = b.getPlayer(play);

        do{
            if (player==null){
                casillaY = (int) (Math.random() * b.game[0].length);
                wall=b.getPress((int) (b.game[0].length-1),casillaY);
            }else{
                wall=b.getPress(player.getX()-1,player.getY());
                if(wall==null || wall.getStatus()!= Status.FREE){
                    casillaX = rand.nextInt(1 + 1 + 1) - 1;
                    if(casillaX!=0){
                        wall = b.getPress(Math.abs(casillaX + player.getX()), player.getY());
                    }else{
                        casillaY = rand.nextInt(1 + 1 + 1) - 1;
                        wall = b.getPress(player.getX(), Math.abs(casillaY + player.getY()));
                    }
                }
            }
        }while(!boxOk(wall));
        wall.setStatus(Status.WALL);
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
