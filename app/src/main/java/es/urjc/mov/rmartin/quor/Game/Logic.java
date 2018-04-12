package es.urjc.mov.rmartin.quor.Game;


import es.urjc.mov.rmartin.quor.Graphic.*;

public class Logic {
    public Board board;
    public Logic(int filas, int columnas){
        this.board= new Board(filas,columnas);
    }
}
