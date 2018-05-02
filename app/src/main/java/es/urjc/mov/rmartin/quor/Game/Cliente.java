package es.urjc.mov.rmartin.quor.Game;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import es.urjc.mov.rmartin.quor.Graphic.Coordinate;

public class Cliente {
    public void conexion() {
        final Thread c = new Thread() {
            @Override
            public void run() {
                Socket s;
                ObjectOutputStream o;
                ObjectInputStream out;
                o = null;
                try {
                    s = new Socket("10.0.2.2", 2020);
                    o = new ObjectOutputStream(s.getOutputStream());

                    //o = s.getOutputStream();
                    Coordinate c= new Coordinate(3,4);
                    Move m = new Move(c,true);
                    o.writeObject(m);
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
