package es.urjc.mov.rmartin.quor.Game;


public enum Level {
    EASY(0), MEDIUM(1), HARD(2);

    private int num;

    Level(int num) {
        this.num = num;
    }
    public int getNum() {
        return num;
    }
}
