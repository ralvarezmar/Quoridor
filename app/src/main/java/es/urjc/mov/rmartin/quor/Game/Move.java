package es.urjc.mov.rmartin.quor.Game;

import es.urjc.mov.rmartin.quor.Graphic.Coordinate;

public class Move {
    private Coordinate c;
    private Boolean type;
    public Move(Coordinate c, Boolean type){
        this.c=c;
        this.type=type;
    }

    public Coordinate getC() {
        return c;
    }

    public void setC(Coordinate c) {
        this.c = c;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }
}
