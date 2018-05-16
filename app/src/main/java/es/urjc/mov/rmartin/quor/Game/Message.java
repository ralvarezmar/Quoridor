package es.urjc.mov.rmartin.quor.Game;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Message {
    public enum MessageTypes {
        LOGIN, PLAY, OK, OKLOGIN, ERROR
    }
    public static int turnoGlob;

    public abstract MessageTypes type();

    public abstract void writeTo(DataOutputStream odata);

    public static Message ReadFrom(DataInputStream idata) {
        Message message = null;
        try {
            int msg_type = idata.readInt();
            Log.v("remoto", "Mensaje recibido: " + msg_type);
            switch (MessageTypes.values()[msg_type]) {
                case LOGIN:
                    message = new Login(idata);
                    break;
                case PLAY:
                    message = new Play(idata);
                    break;
                case OK:
                    message = new OkMessage();
                    break;
                case OKLOGIN:
                    message = new OkLogin(idata);
                    break;
                case ERROR:
                    message = new ErrorMessage();
                    break;
                default:
                    break;
            }
            return message;
        } catch (IOException e) {
            throw new RuntimeException("Msg: read:" + e);
        }
    }

    public static class ErrorMessage extends Message {
        private static final MessageTypes RMSG = MessageTypes.ERROR;

        @Override
        public MessageTypes type() {
            return RMSG;
        }

        public void writeTo(DataOutputStream odata) {
            try {
                odata.writeInt(type().ordinal());
                odata.flush();
            } catch (IOException e) {
                throw new RuntimeException(this + "write: " + e);
            }
        }
    }

    public static class OkMessage extends Message {
        private static final MessageTypes RMSG = MessageTypes.OK;

        @Override
        public MessageTypes type() {
            return RMSG;
        }

        public void writeTo(DataOutputStream odata) {
            try {
                odata.writeInt(type().ordinal());
                odata.flush();
            } catch (IOException e) {
                throw new RuntimeException(this + "write: " + e);
            }
        }
    }

    public static class OkLogin extends Message{
        private static final MessageTypes TMSG = MessageTypes.OKLOGIN;
        int turno;

        OkLogin(DataInputStream idata) throws IOException{
            this.turno=idata.readInt();
            turnoGlob=turno;
            Log.v("remoto", "Recibido turno: " + turno);
        }

        OkLogin(int turno){
            this.turno=turno;
        }

        @Override
        public MessageTypes type() {
            return TMSG;
        }

        @Override
        public void writeTo(DataOutputStream odata) {
            try {
                odata.writeInt(type().ordinal());
                odata.writeInt(turno);
                odata.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Login extends Message {

        private static final MessageTypes TMSG = MessageTypes.LOGIN;
        String nick;

        Login(DataInputStream idata) throws IOException {
            byte[] buffer=new byte[idata.readInt()];
            idata.readFully(buffer);
            this.nick=new String(buffer,"UTF-8");
        }
        Login(String nick){
            this.nick=nick;
        }

        public String getNick() {
            return nick;
        }
        public void setNick(String nick) {
            this.nick = nick;
        }
        public static MessageTypes getTmsg() {
            return TMSG;
        }
        @Override
        public MessageTypes type() {
            return TMSG;
        }

        public void writeTo(DataOutputStream odata) {
            try {
                odata.writeInt(type().ordinal());
                byte buf[] = nick.getBytes();
                odata.writeInt(buf.length);
                odata.write(buf,0,buf.length);
                odata.flush();
            } catch (IOException e) {
                throw new RuntimeException(this + "write: " + e);
            }
        }
    }
    public static class Play extends Message{
        private static final MessageTypes TMSG = MessageTypes.PLAY;
        String nick;
        int x;
        int y;
        Boolean type;

        public String getNick() {
            return nick;
        }
        public void setNick(String nick) {
            this.nick = nick;
        }
        public int getX() {
            return x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public int getY() {
            return y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public Boolean getType() {
            return type;
        }
        public void setType(Boolean type) {
            this.type = type;
        }
        Play(DataInputStream idata) throws IOException {
            Log.v("remoto", "Recibida jugada");
            byte[] buffer=new byte[idata.readInt()];
            idata.readFully(buffer);
            this.nick=new String(buffer,"UTF-8");
            Log.v("remoto", "Nick: " + this.nick);
            this.x=idata.readInt();
            Log.v("remoto", "x: " + this.x);
            this.y=idata.readInt();
            Log.v("remoto", "x: " + this.y);
            this.type = idata.readBoolean();
            Log.v("remoto", "x: " + this.type);
        }

        Play(String nick,int x,int y,Boolean type){
            this.nick=nick;
            this.x=x;
            this.y=y;
            this.type=type;
        }
        @Override
        public MessageTypes type() {
            return TMSG;
        }

        public void writeTo(DataOutputStream odata){
            try{
                System.out.println("writeTo");
                odata.writeInt(MessageTypes.PLAY.ordinal());
                byte buf[] = nick.getBytes();
                odata.writeInt(buf.length);
                odata.write(buf,0,buf.length);
                odata.writeInt(x);
                odata.writeInt(y);
                odata.writeBoolean(type);
                System.out.println("Exit writeTo");
                odata.flush();
            }catch (IOException e){
                throw new RuntimeException(this + "write: " + e);
            }
        }
    }
}
