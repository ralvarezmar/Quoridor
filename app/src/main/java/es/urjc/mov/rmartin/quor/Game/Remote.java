package es.urjc.mov.rmartin.quor.Game;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import es.urjc.mov.rmartin.quor.Graphic.Board;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;


public class Remote extends Player{
    private String nick;
    public Remote(Board board,String nick) {
        super(board);
        this.board=board;
        this.nick=nick;
        conect();
    }

    private void conect() {
        Thread c = new Thread() {
            @Override
            public void run() {
                Socket s;
                try {
                    Log.v("Red", "antes de new Socket");
                    s = new Socket("10.0.2.2", 2020);
                    OutputStream output= s.getOutputStream();
                    DataOutputStream o=new DataOutputStream(output);
                    Log.v("Red", "antes de hacer mensaje");
                    Message login = new Message.Login(nick);
                    Log.v("Red", "antes de mandar");
                    login.writeTo(o);
                    DataInputStream input = new DataInputStream(s.getInputStream());
                    Message answer = Message.ReadFrom(input);
                } catch (ConnectException e) {
                    System.out.println("connection refused" + e);
                } catch (UnknownHostException e) {
                    System.out.println("cannot connect to host " + e);
                } catch (IOException e) {
                    System.out.println("IO exception" + e);
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
            Message message;
            //s = new Socket("10.0.2.2", 2020);
            // o = new ObjectInputStream(s.getInputStream());
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

    //METODO A CLASE MENSAJE
    /*private void sendMove(Move m) throws IOException {
        OutputStream output= s.getOutputStream();
        DataOutputStream o=new DataOutputStream(output);
        try {
            byte buf[] = id.getBytes();
            int x= m.getC().getX();
            int y= m.getC().getY();
            Boolean type = m.getType();
            o.write(buf, 0, buf.length);
            o.writeInt(x);
            o.writeInt(y);
            o.writeBoolean(type);
            o.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void putPlay(Move move) {

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

