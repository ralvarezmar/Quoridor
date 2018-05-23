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
    private final String IP="10.1.128.237";
    //private final String IP="192.168.1.7";
    //private final String IP="10.0.2.2";
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

    private Move changeToPlayer(Move m){
        Coordinate c = m.getC();
        int x = c.getX();
        int y = c.getY();
        int newX= (board.game.length-1)-x;
        int newY= (board.game[1].length-1)-y;
        Coordinate newC = new Coordinate(newX,newY);
        return new Move(newC,m.getType());
    }

    @Override
    public Move askPlay(Status statusPlayer) {
        Message answer = Message.ReadFrom(in);
        if(answer==null){
            return null;
        }
        else if(answer.type()== Message.MessageTypes.PLAY){
            Message.Play play = (Message.Play) answer;
            Coordinate c= new Coordinate(play.getX(),play.getY());
            Move move= new Move(c,play.getType());
            return changeToPlayer(move);
        }
        return null;
    }

    public void closeSocket(){
        try {
            Thread.sleep(5000);
            in.close();
            out.close();
            s.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void putPlay(Move move) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int x= move.getC().getX();
        int y=move.getC().getY();
        Boolean type = move.getType();
        Message message = new Message.Play(nick,x,y,type);
        message.writeTo(out);
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

