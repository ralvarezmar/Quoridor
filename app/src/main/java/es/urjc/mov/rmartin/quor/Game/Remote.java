package es.urjc.mov.rmartin.quor.Game;
import android.os.StrictMode;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    //private final String IP="10.1.137.144";
    private final String IP="192.168.1.4";
    private final int PORT=2020;
    public DataInputStream in;
    private DataOutputStream out;

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
                try {
                    s = new Socket(IP, PORT);
                    in = new DataInputStream(s.getInputStream());
                    out = new DataOutputStream(s.getOutputStream());
                    Message login = new Message.Login(nick);
                    login.writeTo(out);
                    Message answer = Message.ReadFrom(in);
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
    public Move askPlay(Status statusPlayer) {
        System.out.print("Entra en askPlay");
        Message answer = Message.ReadFrom(in);
        System.out.print("Jugada recibida: " + answer);
        Message.Play play = (Message.Play) answer;
        Coordinate c= new Coordinate(play.getX(),play.getY());
        return new Move(c,play.getType());
    }


    @Override
    public void putPlay(Move move) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.print("Entra en putPlay");
        int x= move.getC().getX();
        int y=move.getC().getY();
        Boolean type = move.getType();
        Message message = new Message.Play(nick,x,y,type);
        System.out.print("Mando jugada: " + move);
        System.out.print("Mensaje: " + message);
        message.writeTo(out);
        System.out.print("Jugada mandada");
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

