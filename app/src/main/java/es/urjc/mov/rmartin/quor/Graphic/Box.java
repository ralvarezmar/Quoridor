package es.urjc.mov.rmartin.quor.Graphic;

public class Box {

    public enum Status{FREE,WALL,PLAYER,CPU,REMOTE,PLAYER1,PLAYER2}
    private int x;
    private int y;
    private int id;
    private Status status;

    Box(int x,int y,int id,Status status){
        this.x=x;
        this.y=y;
        this.id=id;
        this.status=status;
    }

    @Override
    public String toString(){
        return "{x: " + this.x + " y: "+this.y +"}";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
