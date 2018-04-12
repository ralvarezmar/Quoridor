package es.urjc.mov.rmartin.quor.Graphic;

import java.util.ArrayList;


public class Board{
    public Box game[][];
    public Board(int filas, int columnas){
        game=new Box[filas][columnas];
        Box b;
        int id=0;
        for(int i=0;i<filas;i++){
            for(int j=0;j<columnas;j++){
                if(j==(columnas-1)/2 && i==0){
                    b=new Box(i,j,id, Status.PLAYER1);
                }else if(j==(columnas-1)/2 && i==filas-1) {
                    b = new Box(i, j, id, Status.PLAYER2);
                }else{
                    b = new Box(i, j, id, Status.FREE);
                }
                game[i][j]=b;
                id++;
            }
        }
    }
    public Box getPlayer(Status player){
        for (int i = 0; i < game.length; i++) {
            for(int j= 0;j <game[i].length;j++){
                Box b = game[i][j];
                if (b.getStatus() == player) {
                    return b;
                }
            }
        }
        return null;
    }

    public ArrayList<Integer> getArrayStatus(){
        ArrayList<Integer> casillasEstado = new ArrayList<>();
        for (int i = 0; i < game.length; i++) {
            for(int j= 0;j <game[i].length;j++) {
                Box item = game[i][j];
               // System.out.println(item.getStatus() + " " + item.getStatus().ordinal());
                casillasEstado.add(item.getStatus().ordinal());
            }
        }
        return casillasEstado;
    }

    public Box getPress(int x,int y){
        if(x>=0 && x<game.length && y>=0 && y<game[0].length){
            return game[x][y];
        }
        return null;
    }

    private Boolean boxValid(Box box, int i, int j){
        int x = box.getX();
        int y = box.getY();
        return (i+x>=0 && i+x<(game[0].length) && (j+y>=0 && j+y<(game[0].length)) &&
                ((i==-1 && j==0) || (i==1 && j==0) ||(j==1 && i==0)||(j==-1  && i==0)) &&
                game[i+x][j+y].getStatus() == Status.FREE);
    }

    public ArrayList<Box> aroundBoxes(Box box){
        ArrayList<Box> boxes = new ArrayList<>();
        if(box==null){
            for(int i=0;i<game.length-1;i++){
                if(game[game.length-1][i].getStatus()==Status.FREE){
                    boxes.add(game[game.length-1][i]);
                }
            }
            return boxes;
        }
        int x= box.getX();
        int y= box.getY();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(boxValid(box,i,j)){
                    boxes.add(game[x+i][y+j]);
                }
            }
        }
        return boxes;
    }
}
