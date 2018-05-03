package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public Remote(Board board,String nick) {
        super(board);
        conect(nick);
    }
    private Socket s;
    private String id;
    private void conect(final String nick) {
        final Thread c = new Thread() {
            @Override
            public void run() {
                //ObjectOutputStream o;
                OutputStream o;
                ObjectInputStream out;
                o = null;
                try {
                    s = new Socket("10.0.2.2", 2020);
                    o = s.getOutputStream();
                    id=nick;
                    byte buf[] = nick.getBytes();
                    o.write(buf, 0, buf.length);
                    //o = new ObjectOutputStream(s.getOutputStream());
                    //Coordinate c= new Coordinate(3,4);
                    //Move m = new Move(c,true);
                    // o.writeObject(m);
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


    @Override
    public Move askPlay(Status statusPlayer) throws InterruptedException {
        Socket s = null;
        ObjectInputStream o;
        Move move = null;
        o = null;
        try {
            //s = new Socket("10.0.2.2", 2020);
             o = new ObjectInputStream(s.getInputStream());
            move = (Move) o.readObject();
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
    private void sendMove(Move m) throws IOException {
        OutputStream input= s.getOutputStream();
        DataOutputStream o=new DataOutputStream(input);
        try {
            byte buf[] = id.getBytes();
            o.write(buf, 0, buf.length);
            int x= m.getC().getX();
            int y= m.getC().getY();
            Boolean type = m.getType();
            o.writeInt(x);
            o.writeInt(y);
            o.writeBoolean(type);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void putPlay(Move move) {
        //ObjectOutputStream o;
        OutputStream o;
        o = null;
        try {
            //s = new Socket("10.0.2.2", 2020);

            /*o = new ObjectOutputStream(s.getOutputStream());
            Log.v("Remoto", "Mando: " + move);
            o.writeObject(move);*/
        } finally {
            if(o!=null){
                try {
                    o.close();
                    //s.close();
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

