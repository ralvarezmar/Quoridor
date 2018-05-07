package es.urjc.mov.rmartin.quor.Game;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

public abstract class Message {
    public enum MessageTypes{
        LOGIN, MOVE, OK, ERROR
    }


    private static final MessageTypes[] messages = MessageTypes.values();

    public abstract MessageTypes type();

    public abstract void writeTo(DataOutputStream odata);

    public static Message ReadFrom(DataInputStream idata){
        Message message = null;
        try {
            int msg_type = idata.readInt();
            switch(messages[msg_type]){
                case LOGIN:
                    message = new Tlogin(idata);
                    break;
                case MOVE:
                    message = new Tplayer(idata);
                    break;
                case OK:
                    message = new OkMessage();
                    break;
                case ERROR:
                    message = new ErrorMessage();
                    break;
                default:
                    break;
            }
            return message;
        } catch (EOFException e) {
            throw new RuntimeException("EOF");
        } catch (IOException e) {
            throw new RuntimeException("Msg: read:" + e);
        }
        throw new RuntimeException("Msg: read: unknown messagge");
    }

    public static class ErrorMessage extends Message{
        private static final MessageTypes RMSG = MessageTypes.ERROR;

        @Override
        public MessageTypes type() {
            return RMSG;
        }
        public void writeTo(DataOutputStream odata){
            try{
                odata.writeInt(type().ordinal());
                odata.flush();
            }catch (IOException e){
                throw new RuntimeException(this + "write: " + e);
            }
        }
    }

    public static class OkMessage extends Message{
        private static final MessageTypes RMSG = MessageTypes.OK;

        @Override
        public MessageTypes type() {
            return RMSG;
        }

        public void writeTo(DataOutputStream odata){
            try{
                odata.writeInt(type().ordinal());
                odata.flush();
            }catch (IOException e){
                throw new RuntimeException(this + "write: " + e);
            }
        }
    }

}
/*

    public abstract void writeTo(DataOutputStream odata);

    public static Msg ReadFrom(DataInputStream idata){
        Msg msg = null;
        try {
            int msg_type = idata.readInt();
            switch(messages[msg_type]){
                case TLOGIN:
                    msg = new Tlogin(idata);
                    break;
                case TPLAYER:
                    msg = new Tplayer(idata);
                    break;
                case ROK:
                    msg = new Rok();
                    break;
                case RERROR:
                    msg = new Rerror();
                    break;
                default:
                    break;
            }
            if(msg != null){
                return msg;
            }
        } catch (EOFException e) {
            throw new RuntimeException("EOF");
        } catch (IOException e) {
            throw new RuntimeException("Msg: read:" + e);
        }
        throw new RuntimeException("Msg: read: unknown messagge");
    }

    public static class Tlogin extends Msg{

        private static final MessageTypes TMSG = MessageTypes.TLOGIN;
        //public Worker worker;

		/*public Tadd(Worker worker){
			this.worker = worker;
		}

		public Tadd(DataInputStream idata){
			this.worker = Worker.readFrom(idata);
		}

		public Worker getWorker() {
			return worker;
		}

        public Tlogin(DataInputStream idata) {
            // TODO Auto-generated constructor stub
        }

        @Override
        public MessageTypes type() {
            return TMSG;
        }

        public void writeTo(DataOutputStream odata){
            try{
                odata.writeInt(type().ordinal());
                //worker.writeTo(odata);
                odata.flush();
            }catch (IOException e){
                throw new RuntimeException(this + "write: " + e);
            }
        }
    }

    public static class Tplayer extends Msg{

        private static final MessageTypes TMSG = MessageTypes.TPLAYER;
        public int id;

        public Tplayer(DataInputStream idata) {
            // TODO Auto-generated constructor stub
        }

        /*public Tremove(int id){
            this.id = id;
        }

        public Tremove(DataInputStream idata) throws IOException{
            this.id = idata.readInt();
        }

        public int getId(){
            return id;
        }

        @Override
        public MessageTypes type() {
            return TMSG;
        }

        public void writeTo(DataOutputStream odata){
            try{
                odata.writeInt(type().ordinal());
                odata.writeInt(id);
                odata.flush();
            }catch (IOException e){
                throw new RuntimeException(this + "write: " + e);
            }
        }
    }
*/