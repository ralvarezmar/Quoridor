package es.urjc.mov.rmartin.quor.Graphic;

public class Box {

    private Coordinate c;
    private int id;
    private Status status;

    Box(Coordinate c,int id,Status status){
        this.c=c;
        this.id=id;
        this.status=status;
    }

    public Coordinate getCoordenate() {
        return c;
    }

    public void setCoordenate(Coordinate c) {
        this.c = c;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Box{" +
                "c=" + c +
                ", id=" + id +
                ", status=" + status +
                '}';
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
