package es.urjc.mov.rmartin.quor;

class DataPlayer {
    private int id;
    private String nick;
    private int ganadas;
    private int jugadas;
    DataPlayer(int id, String nick, int ganadas, int jugadas) {
        this.id=id;
        this.nick=nick;
        this.ganadas=ganadas;
        this.jugadas=jugadas;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
