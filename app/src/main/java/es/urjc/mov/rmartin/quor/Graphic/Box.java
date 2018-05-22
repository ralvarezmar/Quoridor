package es.urjc.mov.rmartin.quor.Graphic;

import java.io.Serializable;
import java.util.Objects;

public class Box implements Serializable{

    private Coordinate c;
    private int id;
    private Status status;

    public Box(Coordinate c, int id, Status status){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return id == box.id &&
                Objects.equals(c, box.c) &&
                status == box.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(c, id, status);
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
