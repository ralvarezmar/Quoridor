package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;


public class IADijkstra extends Player {
    Board b;
    public IADijkstra(Board b){
        super(b);
        this.b=b;}


    private Box getRandom(Box cpu){
        Box move;
        Random rand = new Random();
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
        move.setStatus(Status.PLAYER1);
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
        //wall= way.get(way.size()-1);
        if(way.size()==0) {
            wall.setStatus(Status.FREE);
            wall=null;
        }
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
