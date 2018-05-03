package es.urjc.mov.rmartin.quor.Game;


public class Message {
    String id;
    Move m;
    Message(String id, Move m){
      this.id=id;
      this.m=m;
    }

    public byte[] serialize(){
        byte[] message;
        message=id.getBytes();

        return message;
    }
}
