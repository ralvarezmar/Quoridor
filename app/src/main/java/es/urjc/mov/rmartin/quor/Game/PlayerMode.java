package es.urjc.mov.rmartin.quor.Game;

/**
 * Created by rmartin on 12/04/18.
 */

public enum PlayerMode {
    HUMAN(0),CPU(1),REMOTE(2);

    private int num;
    PlayerMode(int num) {
        this.num=num;
    }
    public int getNum() {
        return num;
    }

    public static PlayerMode getValue(int val){
        if(val>2){
            return null;
        }
        return PlayerMode.values()[val];
    }
}
