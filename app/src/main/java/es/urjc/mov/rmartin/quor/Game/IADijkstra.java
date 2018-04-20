package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;
import es.urjc.mov.rmartin.quor.R;


public class IADijkstra extends Player {
    Board b;
    public IADijkstra(Board b){
        super(b);
        this.b=b;}
    private static final int PORCENTAJE = 20;

    private Box getRandom(Box cpu){
        Box move;
        Random rand = new Random();
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
        move.setStatus(Status.PLAYER1);
        return move;
    }

    @Override
    public Box getMove(int destiny,Status player){
        Box cpu=b.getPlayer(player);
        Box move;
        int casillaY;
        if(cpu==null) { //primer movimiento
           // Log.v("veces","random");
            do {
                casillaY = (int) (Math.random() * b.game[0].length);
                move = b.getPress(0, casillaY);
            }while(move.getStatus()!=Status.FREE);
        }else{
            Dijkstra dijkstra=new Dijkstra(b,cpu);
            ArrayList<Box> way = dijkstra.doWay(destiny);
            if(way.size()==0){
                move=getRandom(cpu);
                return move;
            }
            Log.v("veces",way.toString());
            move=way.get(way.size()-1);
            //way.remove(way.size()-1);
            cpu.setStatus(Status.FREE);
        }
        move.setStatus(player);
        return move;
    }


    @Override
    public Box putWall(int destiny,Status play){
        Box wall;
        Box player = b.getPlayer(play);

        Dijkstra dijkstra1=new Dijkstra(b,player);
        ArrayList<Box> way = dijkstra1.doWay(destiny);
        wall= way.get(way.size()-1);
        wall.setStatus(Status.WALL);
        way = dijkstra1.doWay(destiny);
        if(way.size()==0) {
            wall.setStatus(Status.FREE);
            wall=null;
        }
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
            Box b= getMove(destinyPlayer, statusPlayer);
            Move m=new Move(b.getCoordenate(),true);
            return m;
        }
        Box b= putWall(destinyOpposite,Status.PLAYER2);
        Move m=new Move(b.getCoordenate(),false);
        return m;
        //DEVUELVE TIPO JUGADA Y EN EL GAMEACTIVITY CAMBIO LOS ESTADOS DESPUES DE COMPROBAR QUE LA JUGADA SEA VALIDA
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
