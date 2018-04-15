package es.urjc.mov.rmartin.quor.Game;

public enum PlayerMode {
    HUMAN(0), CPU(1),REMOTE(2);

    private int num;
    PlayerMode(int num) {
        this.num=num;
    }
    public int getNum() {
        return num;
    }

    public static PlayerMode getVal(int val){
        return PlayerMode.values()[val];
    }
}
