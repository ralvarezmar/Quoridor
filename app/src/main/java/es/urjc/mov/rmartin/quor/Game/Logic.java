package es.urjc.mov.rmartin.quor.Game;


import es.urjc.mov.rmartin.quor.Graphic.*;

public class Logic {
    public Board board;
    public Logic(int filas, int columnas, int player1, int player2){
        this.board= new Board(filas,columnas,player1,player2);
    }
}
