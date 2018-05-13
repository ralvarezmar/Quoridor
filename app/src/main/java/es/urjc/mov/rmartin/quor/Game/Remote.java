package es.urjc.mov.rmartin.quor.Game;
import android.util.Log;
import android.widget.PopupWindow;

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
    private final String IP="192.168.1.4";
    private final int PORT=2020;

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
                    s = new Socket(IP, PORT);
                    OutputStream output= s.getOutputStream();
                    DataOutputStream o=new DataOutputStream(output);
                    Message login = new Message.Login(nick);
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
            move = new Move(c,play.getType());
        } catch (ConnectException e) {
            System.out.print("connection refused " + e);
        } catch (UnknownHostException e) {
            System.out.print("cannot connnect " + e);
        } catch (IOException e) {
            System.out.print("IO exception" + e);
        }
        return move;
    }


    @Override
    public void putPlay(Move move) {
        OutputStream output;
        try {
            output = s.getOutputStream();
            DataOutputStream o=new DataOutputStream(output);
            int x= move.getC().getX();
            int y=move.getC().getY();
            Boolean type = move.getType();
            Message message = new Message.Play(nick,x,y,type);
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

