package es.urjc.mov.rmartin.quor.Game;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import es.urjc.mov.rmartin.quor.Graphic.Coordinate;

public class Message {
    private String id;
    private Move m;
    private Socket s;
    Message(Socket s, String id, Move m){
        this.s=s;
        this.id=id;
        this.m=m;
    }

    void sendMove() throws IOException {
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
    }

    public Move receiveMessage(){
        InputStream input;
        Move move = null;
        try {
            input = s.getInputStream();
            DataInputStream o=new DataInputStream(input);
            byte[] nick = new byte[512];
            int x;
            int y;
            Boolean type;
            o.read(nick,0,nick.length);
            x=o.readInt();
            y=o.readInt();
            type=o.readBoolean();
            Coordinate c= new Coordinate(x,y);
            move = new Move(c,type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return move;
    }
}
