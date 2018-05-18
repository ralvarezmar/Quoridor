package es.urjc.mov.rmartin.quor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import es.urjc.mov.rmartin.quor.Game.Human;
import es.urjc.mov.rmartin.quor.Game.IADijkstra;
import es.urjc.mov.rmartin.quor.Game.Level;
import es.urjc.mov.rmartin.quor.Game.Logic;
import es.urjc.mov.rmartin.quor.Game.Message;
import es.urjc.mov.rmartin.quor.Game.Move;
import es.urjc.mov.rmartin.quor.Game.Player;
import es.urjc.mov.rmartin.quor.Game.IAMedium;
import es.urjc.mov.rmartin.quor.Game.IARandom;
import es.urjc.mov.rmartin.quor.Game.PlayerMode;
import es.urjc.mov.rmartin.quor.Game.Remote;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class GameActivity extends AppCompatActivity {
    static final int FILAS = 7;
    static final int COLUMNAS = 7;
    static final double SIZE=1.5;
    static final int SLEEP=2000;
    static final int MINSIZE=-15;
    Logic logic;
    Player playerTop;
    Player playerBottom;
    //int jugadas=0;
    //int ganadas=0;
    int player1;
    int player2;
    int count=0;
    Level level = Level.HARD;
    Player turn[];
    Player humanTurn[];
    Player remoteTurn;
    public String user;
    int crear;
    Thread t;
    volatile boolean finish = true;
    Database database;

    private void paintAgain(){
        design(logic.board.getArrayStatus());
        //jugadas++;
        //database.played++;
        TextView text = (TextView) findViewById(R.id.jugadas);
        String s= getResources().getString(R.string.jugadas);
        s= s+database.played;
        text.setText(s);
    }

    private Player selectLevel(Player player){
        switch (level){
            case EASY:
                player = new IARandom(logic.board);
                break;
            case MEDIUM:
                player = new IAMedium(logic.board);
                break;
            case HARD:
                player = new IADijkstra(logic.board);
                break;
        }
        return player;
    }

    private void restart(){
        t.interrupt();
        finish=false;
        TableLayout tl=(TableLayout)findViewById(R.id.tabla);
        tl.removeAllViews();
        logic = new Logic(FILAS,COLUMNAS);
        count=0;
        setPlayer(player1,player2);
        Log.v("Database", "ANTES DE INCREMENTAR Partidas jugadas: " + database.played);
        database.played++;
        Log.v("Database", "DESPUES DE INCREMENTAR Partidas jugadas: " + database.played);
        paintWinner();
    }


    private class Restart implements View.OnClickListener{
        public void onClick(View button){
            restart();
            paintAgain();
            finish=true;
            runThread();
        }
    }

    public class GameBoton implements View.OnClickListener{
        int x;
        int y;
        GameBoton(int x,int y) {
            this.x = x;
            this.y= y;
        }
        public void onClick(View button){
            int turno=count%2;
            if(humanTurn[turno]==null){
                System.out.print("NO ES TU TURNO");
                return;
            }
            Switch eleccion;
            Status player;
            if(turno==1){
                eleccion = (Switch) findViewById(R.id.eleccionbottom);
                player=Status.PLAYER2;
            }else {
                eleccion = (Switch) findViewById(R.id.eleccionTop);
                player=Status.PLAYER1;
            }
            Coordinate c=new Coordinate(x,y);
            Boolean check = eleccion.isChecked();
            Move move=new Move(c,check);
            if(humanTurn[turno].validMove(move,player)){
                humanTurn[turno].putPlay(move);
                changeStatus(move,player);
                if(remoteTurn!=null){
                    remoteTurn.putPlay(move);
                    System.out.println("Despues de PUTPLAY");
                }
            }

        }
    }

    private void design(ArrayList<Integer> statusArray){
        doBoard(statusArray);
        ImageButton butRestart = (ImageButton) findViewById(R.id.restart);
        butRestart.setOnClickListener(new Restart());
    }

    private Status[] setBoxes(Status[] boxes){
        boxes[0]= Status.FREE;
        boxes[1]=Status.WALL;
        boxes[2]=Status.PLAYER1;
        boxes[3]= Status.PLAYER2;
        return boxes;
    }
    private int[] setImages(int[] images){
        images[0]= R.drawable.square;
        images[1]=R.drawable.square_red;
        images[2]=R.drawable.square;
        images[3]=R.drawable.square;
        return images;
    }
    private int[] setBackgrounds(int[] backs){
        backs[0]= 0;
        backs[1]= 0;
        backs[2]=R.drawable.pawn_player1_back;
        backs[3]= R.drawable.pawn_player2_back;
        return backs;
    }

    private void doBoton(int x, int y, TableRow row, ArrayList<Integer> statusArray, int id, TableRow.LayoutParams lr){
        Status boxes[]=new Status[4];
        int images[]=new int[4];
        int backs[]=new int[4];

        ImageButton imgButton=new ImageButton(this);
        imgButton.setImageResource(R.drawable.square);
        imgButton.setBackgroundColor(Color.TRANSPARENT);
        imgButton.setLayoutParams(lr);
        imgButton.setScaleX((float)(SIZE));
        imgButton.setScaleY((float)(SIZE));
        imgButton.setId(id);
        row.addView(imgButton);
        imgButton.setOnClickListener(new GameBoton(x,y));
        Box b = logic.board.getPress(x,y);
        boxes=setBoxes(boxes);
        images=setImages(images);
        backs=setBackgrounds(backs);
        int num = statusArray.get(id);
        b.setStatus(boxes[num]);
        imgButton.setImageResource(images[num]);
        imgButton.setBackgroundResource(backs[num]);
    }

    private void doBoard(ArrayList<Integer> statusArray){
        int contador = 0;
        TableLayout tl = (TableLayout) findViewById(R.id.tabla);
        tl.removeAllViews();
        for(int i=0;i<FILAS;i++){
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lr = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            if(getResources().getConfiguration().orientation!=1) {
                lr.bottomMargin = MINSIZE;
                lr.topMargin = MINSIZE;
            }else{
                lr.leftMargin = -10;
                lr.rightMargin = -10;
            }
            row.setLayoutParams(lr);
            tl.addView(row);
            for(int j=0;j<COLUMNAS;j++){
                doBoton(i,j,row,statusArray,contador,lr);
                contador++;
            }
        }
    }

    private void recoverStatus(Bundle savedInstanceState){
        ArrayList<Integer> statusArray = savedInstanceState.getIntegerArrayList("estados");
        int intLevel = savedInstanceState.getInt("nivel");
        level = Level.values()[intLevel];
        player1=savedInstanceState.getInt("player1");
        player2=savedInstanceState.getInt("player2");
        if(statusArray!=null){
            design(statusArray);
            paintWinner();
            //selectLevel();
        }
    }
    private void paintWinner(){
        TextView text = (TextView) findViewById(R.id.jugadas);
        String s= getResources().getString(R.string.jugadas);
        Log.v("Database", "Partidas jugadas: " + database.played);
        s= s+database.played;
        text.setText(s);
        text = (TextView) findViewById(R.id.ganadas);
        s= getResources().getString(R.string.ganadas);
        Log.v("Database", "Partidas ganadas: " + database.winners);
        s= s+database.winners;
        text.setText(s);
        database.modifyValues(user,database.winners,database.played);
        database.getData(user);
    }

    private void setConfiguration(Bundle configuration){
        player1= configuration.getInt("player1");
        player2= configuration.getInt("player2");
        user= configuration.getString("user");
    }

    private void setPlayer(int player1,int player2){
        turn=new Player[2];
        humanTurn=new Human[2];
        switch (PlayerMode.getVal(player1)){
            case HUMAN:
                Switch eleccion=(Switch) findViewById(R.id.eleccionTop);
                TextView movimiento=(TextView) findViewById(R.id.textMoveTop);
                TextView wall = (TextView) findViewById(R.id.textWallTop);
                movimiento.setVisibility(View.VISIBLE);
                eleccion.setVisibility(View.VISIBLE);
                wall.setVisibility(View.VISIBLE);
                playerTop = new Human(logic.board);
                humanTurn[0]=playerTop;
                break;
            case CPU:
                playerTop=selectLevel(playerTop);
                break;
            case REMOTE:
                Log.v("Red", "nuevo cliente");
                playerTop=new Remote(logic.board,user);
                remoteTurn=playerTop;
                ImageButton butRestart = (ImageButton) findViewById(R.id.restart);
                butRestart.setVisibility(View.INVISIBLE);
                //count = Message.turnoGlob;
                break;
        }
        turn[0]=playerTop;
        switch (PlayerMode.getVal(player2)){
            case HUMAN:
                Switch eleccion=(Switch) findViewById(R.id.eleccionbottom);
                TextView movimiento=(TextView) findViewById(R.id.textMoveBottom);
                TextView wall = (TextView) findViewById(R.id.textWallBottom);
                movimiento.setVisibility(View.VISIBLE);
                eleccion.setVisibility(View.VISIBLE);
                wall.setVisibility(View.VISIBLE);
                playerBottom = new Human(logic.board);
                humanTurn[1]=playerBottom;
                break;
            case CPU:
                playerBottom=selectLevel(playerBottom);
                break;
        }
        turn[1]=playerBottom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Bundle configuration = getIntent().getExtras();
        logic = new Logic(FILAS,COLUMNAS);
        if(configuration!=null){
            setConfiguration(configuration);
        }
        ArrayList<Integer> statusArray = logic.board.getArrayStatus();
        if(savedInstanceState!=null){
            recoverStatus(savedInstanceState);
            setPlayer(player1,player2);
            return;
        }else{
            setPlayer(player1,player2);
        }
        database = new Database(getApplicationContext());

        design(statusArray);
        Log.v("Database", "Creo base de datos: "+ user);
        if(database.consultaBD(user)){
            Log.v("Database", "get data");
            database.getData(user);
        }else{
            Log.v("Database", "put Value");
            database.putValue(user);
        }
        paintWinner();
        if(remoteTurn!=null){
            count=Message.turnoGlob;
            paintTurn(count);
        }
        runThread();
    }

    private void movePC(int turno,Status player){
        try {
            Move move;
            do {
                move = turn[turno].askPlay(player);
            }while(move==null);
            Thread.sleep(SLEEP);
            if(turn[(turno+1)%2]==remoteTurn){
                remoteTurn.putPlay(move);
                Thread.sleep(SLEEP);
            }
            Log.v("turno", "IA: " + move);
            changeStatus(move,player);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void paintBoardThread(int turno){
        ArrayList<Integer> statusArray = logic.board.getArrayStatus();
        doBoard(statusArray);
        Box p1 = logic.board.getPlayer(Status.PLAYER1);
        Box p2 = logic.board.getPlayer(Status.PLAYER2);
         Coordinate cPlayer1 = p1.getCoordenate();
         Coordinate cPlayer2 =  p2.getCoordenate();
         if((cPlayer1.getX()==FILAS-1 || cPlayer2.getX()==0) && remoteTurn==null){
             finish=false;
             if(cPlayer2.getX()==0){
                Log.v("Database", "ANTES DE INCREMENTAR Partidas ganadas: " + database.winners);
                database.winners++;
                Log.v("Database", "DESPUÉS DE INCREMENTAR Partidas ganadas: " + database.winners);
             }
            restart();
            paintAgain();
            paintWinner();
            finish=true;
            runThread();
        }
        paintTurn(turno);
    }

    private Status choosePlayer(int turno){
        if(turno==1){
            return Status.PLAYER2;
        }else{
            return Status.PLAYER1;
        }
    }
    private void runThread(){
        t = new Thread(new Runnable() {
        public void run(){
            while (finish) {
                final int turno;
                synchronized (this) {
                    turno = count % turn.length;
                }
                Log.v("turno", "Numero: " + count + " Turno: " + turno);
                Status player;
                player=choosePlayer(turno);
                if(humanTurn[turno]!=null){
                    try {
                        turn[turno].askPlay(player);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(turn[turno]==remoteTurn) {
                     try {
                         Move move = turn[turno].askPlay(player);
                         changeStatus(move,Status.PLAYER1);
                         Thread.sleep(SLEEP);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                }else{
                   movePC(turno,player);
                }
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                       paintBoardThread(turno);
                    }
                });
                synchronized (this) {
                    count++;
                }
            }
        }
        });
        t.start();
    }


    private void paintTurn(int turno){
        ImageView bottom =(ImageView) findViewById(R.id.imageViewBottom);
        ImageView top =(ImageView) findViewById(R.id.imageViewTop);
        if(turno==0){
            bottom.setVisibility(View.VISIBLE);
            top.setVisibility(View.INVISIBLE);
        }else{
            bottom.setVisibility(View.INVISIBLE);
            top.setVisibility(View.VISIBLE);
        }
    }

    private void changeStatus(Move move,Status player){
        Box boxFuture = logic.board.getPress(move.getC());
        if(move.getType()){
            Box boxNow = logic.board.getPlayer(player);
            boxFuture.setStatus(player);
            boxNow.setStatus(Status.FREE);
            Log.v("Movimiento","Nueva casilla: " + boxFuture + " Antigua: " + boxNow);
        }else{
            boxFuture.setStatus(Status.WALL);
            Log.v("Muro","Nueva casilla: " + boxFuture );
        }
    }

    private void printBoard(ArrayList statusArray){
        int c=0;
        for(int i = 0; i < logic.board.game.length; i++) {
            for(int j= 0;j < logic.board.game[i].length;j++) {
               System.out.print(statusArray.get(c));
               System.out.print(" ");
            }
            System.out.println(" ");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onDestroy(){
        finish=false;
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        ArrayList<Integer> statusBoard = logic.board.getArrayStatus();
        state.putIntegerArrayList("estados", statusBoard);
        state.putInt("nivel",level.getNum());
        state.putInt("player1",player1);
        state.putInt("player2",player2);
        super.onSaveInstanceState(state);
    }


    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.view.MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conf,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.map:
                map();
                return true;
            case R.id.help:
                ayuda();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void map() {
           android.content.Intent map = new android.content.Intent(GameActivity.this,MapsActivity.class);
           startActivity(map);
    }

    private void ayuda(){
        android.content.Intent help = new android.content.Intent(GameActivity.this,HelpActivity.class);
        //Bundle msg = new Bundle();
        startActivity(help);
    }
}

