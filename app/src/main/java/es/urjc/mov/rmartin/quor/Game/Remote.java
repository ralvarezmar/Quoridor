package es.urjc.mov.rmartin.quor.Game;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Remote {
    public void conecting() {
        Thread c = new Thread() {
            @Override
            public void run() {
                Socket s;
                OutputStream o;
                o = null;
                try {
                    s = new Socket("10.0.2.1", 8080);



                    o = s.getOutputStream();
                    byte buf[] = "hola hola".getBytes();
                    o.write(buf, 0, buf.length);




                } catch (ConnectException e) {
                    System.out.print("connection refused " + e);
                } catch (UnknownHostException e) {
                    System.out.print("cannot connnect " + e);
                } catch (IOException e) {
                    System.out.print("IO exception" + e);
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

