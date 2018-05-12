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
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;


public class Remote extends Player{
    private String nick;
    private Socket s;

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
               // Socket s;
                try {
                    Log.v("Red", "antes de new Socket");
                    s = new Socket("10.1.128.74", 2020);
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
        ObjectInputStream o;
        Move move = null;
        o = null;
        try {
            DataInputStream input = new DataInputStream(s.getInputStream());
            Message answer = Message.ReadFrom(input);
            Message.Play play = (Message.Play) answer;
            Coordinate c= new Coordinate(play.getX(),play.getY());

        } catch (ConnectException e) {
            System.out.print("connection refused " + e);
        } catch (UnknownHostException e) {
            System.out.print("cannot connnect " + e);
        } catch (IOException e) {
            System.out.print("IO exception" + e);
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
        OutputStream output= null;
        try {
            output = s.getOutputStream();
            DataOutputStream o=new DataOutputStream(output);
            //AÑADIR NICK AL MENSAJE
            int x= move.getC().getX();
            int y=move.getC().getY();
            Boolean type = move.getType();
            Message message = new Message.Play(x,y,type);
            message.writeTo(o);
        } catch (IOException e) {
            e.printStackTrace();
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

