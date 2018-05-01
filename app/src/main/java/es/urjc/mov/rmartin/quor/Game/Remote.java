package es.urjc.mov.rmartin.quor.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;


public class Remote extends Player{
    public Remote(Board board) {
        super(board);
    }

    public void conecting(final String name) {
        Thread c = new Thread() {
            @Override
            public void run() {
                Socket s;
                OutputStream o;
                o = null;
                try {
                    s = new Socket("10.0.2.1", 8080);
                    o = s.getOutputStream();
                    ObjectOutputStream clientOutputStream = new
                            ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream clientInputStream = new
                            ObjectInputStream(s.getInputStream());

                    byte buf[] = name.getBytes();
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
    @Override
    public Move askPlay(Status statusPlayer) throws InterruptedException {
        Socket s;
        ObjectInputStream o;
        Move move = null;
        o = null;
        try {
            s = new Socket("10.0.2.2", 8080);
             o = new ObjectInputStream(s.getInputStream());
            move = (Move) o.readObject();
            //byte buf[] = name.getBytes();
            //o.write(buf, 0, buf.length);
        } catch (ConnectException e) {
            System.out.print("connection refused " + e);
        } catch (UnknownHostException e) {
            System.out.print("cannot connnect " + e);
        } catch (IOException e) {
            System.out.print("IO exception" + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(o!=null){
                try {
                    o.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return move;
    }

    @Override
    public void putPlay(Move move) {
        Socket s;
        ObjectOutputStream o;
        o = null;
        try {
            s = new Socket("10.0.2.2", 8080);
            o = new ObjectOutputStream(s.getOutputStream());
            o.writeObject(move);
        } catch (ConnectException e) {
            System.out.print("connection refused " + e);
        } catch (UnknownHostException e) {
            System.out.print("cannot connnect " + e);
        } catch (IOException e) {
            System.out.print("IO exception" + e);
        } finally {
            if(o!=null){
                try {
                    o.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Box getMove(int destiny, Status player) {
        return null;
    }

    @Override
    public Box putWall(int destiny, Status play) {
        return null;
    }



    @Override
    public boolean validMove(Move m, Status status) {
        return false;
    }
}

