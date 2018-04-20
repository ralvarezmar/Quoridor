package es.urjc.mov.rmartin.quor.Game;

import java.util.Random;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class IAMedium extends Player {
    Board b;
    public IAMedium(Board b){
        super(b);
        this.b=b;}
    private static final int PORCENTAJE = 20;

    @Override
    public Box getMove(int destiny,Status player){
        Random rand = new Random();
        Box cpu=b.getPlayer(player);
        Box move;
        int casillaX;
        int casillaY;
        do {
            move = b.getPress(cpu.getCoordenate().getX() + 1, cpu.getCoordenate().getY());
            if (move == null || move.getStatus() != Status.FREE) {
                casillaX = rand.nextInt(1 + 1 + 1) - 1;
                if (casillaX != 0) {
                    move = b.getPress(Math.abs(casillaX + cpu.getCoordenate().getX()), cpu.getCoordenate().getY());
                } else {
                    casillaY = rand.nextInt(1 + 1 + 1) - 1;
                    move = b.getPress(cpu.getCoordenate().getX(), Math.abs(casillaY + cpu.getCoordenate().getY()));
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
                wall=b.getPress(player.getCoordenate().getX()-1,player.getCoordenate().getY());
                if(wall==null || wall.getStatus()!= Status.FREE){
                    casillaX = rand.nextInt(1 + 1 + 1) - 1;
                    if(casillaX!=0){
                        wall = b.getPress(Math.abs(casillaX + player.getCoordenate().getX()), player.getCoordenate().getY());
                    }else{
                        casillaY = rand.nextInt(1 + 1 + 1) - 1;
                        wall = b.getPress(player.getCoordenate().getX(), Math.abs(casillaY + player.getCoordenate().getY()));
                    }
                }
            }
        }while(!boxOk(wall));
        wall.setStatus(Status.WALL);
        return wall;
    }
    @Override
    public synchronized Move askPlay(Box pressed,Status statusPlayer,Status statusOpposite, boolean checked){
        int destinyPlayer;
        int destinyOpposite;
        if(statusPlayer==Status.PLAYER1){
            destinyPlayer=b.game.length-1;
            destinyOpposite=0;
        }else{
            destinyPlayer=0;
            destinyOpposite=b.game.length-1;
        }
        int move = (int) (Math.random() * 100);
        if (move > PORCENTAJE){
            Box casilla = getMove(destinyPlayer, statusPlayer);
            Move m=new Move(casilla.getCoordenate(),true);
            return m;
        }
       Box casilla= putWall(destinyOpposite,Status.PLAYER2);
        Move m=new Move(casilla.getCoordenate(),false);
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
