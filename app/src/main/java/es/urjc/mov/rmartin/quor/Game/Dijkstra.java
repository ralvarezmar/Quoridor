package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;

import java.util.ArrayList;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class Dijkstra {
    private Board board;
    private Box box;
    public Dijkstra(Board board, Box box){
        this.board=board;
        this.box=box;
    }
    private class Nodo{
        Nodo prev;
        Box box;
        int lenght;

        Nodo(Nodo prev, Box box, int lenght) {
            this.prev=prev;
            this.box=box;
            this.lenght=lenght;
        }
        public String toString(){
            String s;
            if(this.prev.box!=null){
                s=this.prev.box.getCoordenate().getX()+ " "+this.prev.box.getCoordenate().getY();
            }else{
                s="null";
            }
            return "{prev: " + s + " box: "+this.box.toString() + " lenght: " + this.lenght + "}";
        }
    }


    private static final int INFINITO = -1;
    private static final int NOVISITADO = 99999999;
    private Nodo[][] matriz;
    private Integer lastyVisited=0;

    private Nodo[][] setBoard(Board b){
        Nodo[][] matriz=new Nodo[b.game.length][b.game.length];
        Nodo n;
        int x=0;
        int y=0;
        if(box!=null){
            x=box.getCoordenate().getX();
            y=box.getCoordenate().getY();
        }
        for(int i=0;i<b.game.length;i++){
            for(int j=0;j<b.game[i].length;j++){
                if(b.game[i][j].getStatus()== Status.FREE || (x==i && y==j)){
                    n=new Nodo(null,b.game[i][j],NOVISITADO);
                }else{
                    n=new Nodo(null,null,INFINITO);
                }
                matriz[i][j]=n;
            }
        }
        return matriz;
    }



    private void visit(Box pos,Nodo posAnt, int peso,int destiny){
        int x=0;
        int y=0;
        if(pos!=null){
            x=pos.getCoordenate().getX();
            y=pos.getCoordenate().getY();
        }
        int pesoViejo=matriz[x][y].lenght;
        //Log.v("visit ",x + " " + y);
        //Log.v("ANTES ","Peso viejo: " + pesoViejo + "peso nuevo: " + peso);
        if (pesoViejo != INFINITO && (pesoViejo>peso || pesoViejo==NOVISITADO)) {
            matriz[x][y].prev = posAnt;
            matriz[x][y].lenght = peso;
            matriz[x][y].box = pos;
            ArrayList<Box> around = board.aroundBoxes(pos);
            for (Box posAct : around) {
                visit(posAct, matriz[x][y], peso + 1,destiny);
                if (posAct.getCoordenate().getX() == destiny) {
                    lastyVisited=posAct.getCoordenate().getY();
                    return;
                }
            }
        }
    }

    public ArrayList<Box> doWay(int destiny) {
        ArrayList<Box> way = new ArrayList<>();
        matriz=setBoard(board);
        visit(this.box,null,0,destiny);
        Nodo previo=matriz[destiny][lastyVisited];
        while(previo.prev!=null){
            way.add(previo.box);
            previo=previo.prev;
        }
       /* if(way.size()==0){
            return null;
        }
        Box lastBox=way.get(0);
        if(lastBox.getCoordenate().getX()!=destiny){
            return null;
        }*/
        return way;
    }
}
