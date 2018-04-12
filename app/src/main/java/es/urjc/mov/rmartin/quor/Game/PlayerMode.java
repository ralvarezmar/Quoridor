package es.urjc.mov.rmartin.quor.Game;

/**
 * Created by rmartin on 12/04/18.
 */

public enum PlayerMode {
    CPU(1), HUMAN(2), REMOTE(3);

    private int num;
    PlayerMode(int num) {
        this.num=num;
    }
    public int getNum() {
        return num;
    }
}
