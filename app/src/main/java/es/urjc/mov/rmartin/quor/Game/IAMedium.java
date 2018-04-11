package es.urjc.mov.rmartin.quor.Game;

import java.util.Random;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;

public class IAMedium extends Player {
    Board b;
    public IAMedium(Board b){
        super(b);
        this.b=b;}

    @Override
    public Box getMove(int destiny){
        Random rand = new Random();
        Box cpu=b.getCpu();
        Box move;
        int casillaX;
        int casillaY;
        System.out.println("Dificultad media");
        do {
            if(cpu==null) { //primer movimiento
                casillaY = (int) (Math.random() * b.game[0].length);
                move = b.getPress(0, casillaY);
            }else{
                move = b.getPress(cpu.getX() + 1, cpu.getY());
                if (move == null || move.getStatus() != Box.Status.FREE) {
                    casillaX = rand.nextInt(1 + 1 + 1) - 1;
                    if (casillaX != 0) {
                        move = b.getPress(Math.abs(casillaX + cpu.getX()), cpu.getY());
                    } else {
                        casillaY = rand.nextInt(1 + 1 + 1) - 1;
                        move = b.getPress(cpu.getX(), Math.abs(casillaY + cpu.getY()));
                    }
                }
                cpu.setStatus(Box.Status.FREE);
            }
        }while (!boxOk(move));
        move.setStatus(Box.Status.CPU);
        return move;
    }

    @Override
    public Box putWall(int destiny){
        Box wall;
        Random rand = new Random();
        int casillaX;
        int casillaY;
        Box player = b.getPlayer(Box.Status.PLAYER);

        do{
            if (player==null){
                casillaY = (int) (Math.random() * b.game[0].length);
                wall=b.getPress((int) (b.game[0].length-1),casillaY);
            }else{
                wall=b.getPress(player.getX()-1,player.getY());
                if(wall==null || wall.getStatus()!= Box.Status.FREE){
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
