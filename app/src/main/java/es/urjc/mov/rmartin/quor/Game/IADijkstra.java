package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;


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
        }while (!boxOk(move));
        move.setStatus(Box.Status.CPU);
        return move;
    }

    @Override
    public Box getMove(int destiny){
        Box cpu=b.getCpu();
        Box move;
        int casillaY;
        if(cpu==null) { //primer movimiento
            Log.v("veces","random");
            do {
                casillaY = (int) (Math.random() * b.game[0].length);
                move = b.getPress(0, casillaY);
            }while(move.getStatus()!=Box.Status.FREE);
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
            cpu.setStatus(Box.Status.FREE);
        }
        move.setStatus(Box.Status.CPU);
        return move;
    }


    @Override
    public Box putWall(int destiny){ //int destiny
        Box wall;
        Random rand = new Random();
        int casillaX;
        int casillaY;
        Box player = b.getPlayer(Box.Status.PLAYER);
        /*do{
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
        }while(!boxOk(wall));*/
        Dijkstra dijkstra1=new Dijkstra(b,player);
        ArrayList<Box> way = dijkstra1.doWay(destiny);
        wall= way.get(way.size()-1);
        wall.setStatus(Box.Status.WALL);
        way = dijkstra1.doWay(destiny);
        //wall= way.get(way.size()-1);
        if(way.size()==0) {
            wall.setStatus(Box.Status.FREE);
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
