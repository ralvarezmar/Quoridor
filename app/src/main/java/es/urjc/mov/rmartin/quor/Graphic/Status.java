package es.urjc.mov.rmartin.quor.Graphic;

/**
 * Created by rmartin on 12/04/18.
 */

public enum Status {
    FREE(0),WALL(1),PLAYER2(2),PLAYER1(3);
    int num;
    Status(int num){
        this.num=num;
    }
}
