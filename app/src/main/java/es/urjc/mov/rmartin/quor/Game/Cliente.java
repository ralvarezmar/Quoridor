package es.urjc.mov.rmartin.quor.Game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;


public class Cliente {
    public void conexion() {
        final Thread c = new Thread() {
            @Override
            public void run() {
                Socket s;
                ObjectOutputStream o;
                o = null;
                try {
                    s = new Socket("10.0.2.2", 2020);
                    o = new ObjectOutputStream(s.getOutputStream());

                    //o = s.getOutputStream();
                    Coordinate c= new Coordinate(3,4);
                    Box b = new Box(c,34, Status.FREE);
                    System.out.println(b);
                    o.writeObject(b);
                    //byte buf[] = "ENTRAAAA".getBytes("UTF-8");
                    //o.write(buf, 0, buf.length);
                } catch (ConnectException e) {
                    System.out.println("connection refused" + e);
                } catch (UnknownHostException e) {
                    System.out.println("cannot connect to host " + e);
                } catch (IOException e) {
                    System.out.println("IO exception" + e);
                } finally {
                    if (o != null) {
                        try {
                            o.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        c.start();
    }
}
