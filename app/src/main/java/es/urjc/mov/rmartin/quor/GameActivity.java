package es.urjc.mov.rmartin.quor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import es.urjc.mov.rmartin.quor.Game.Human;
import es.urjc.mov.rmartin.quor.Game.IADijkstra;
import es.urjc.mov.rmartin.quor.Game.Level;
import es.urjc.mov.rmartin.quor.Game.Logic;
import es.urjc.mov.rmartin.quor.Game.Move;
import es.urjc.mov.rmartin.quor.Game.Player;
import es.urjc.mov.rmartin.quor.Game.IAMedium;
import es.urjc.mov.rmartin.quor.Game.IARandom;
import es.urjc.mov.rmartin.quor.Game.PlayerMode;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Coordinate;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class GameActivity extends AppCompatActivity {
    private static String TAG="PRUEBA";
    static final int FILAS = 7;
    static final int COLUMNAS = 7;
    static final double SIZE=1.5;
    static final int SLEEP=2000;
    Logic logic;
    Player playerTop;
    Player playerBottom;
    int jugadas=0;
    int ganadas=0;
    int player1;
    int player2;
    int count=0;
    Level level = Level.HARD;
    Player turn[];
    Player humanTurn[];

    private void paintAgain(){
        design(logic.board.getArrayStatus());
        jugadas++;
        TextView text = (TextView) findViewById(R.id.jugadas);
        String s= getResources().getString(R.string.jugadas);
        s= s+jugadas;
        text.setText(s);
        Box player1=logic.board.getPlayer(Status.PLAYER1);
        Box player2=logic.board.getPlayer(Status.PLAYER2);
        runThread(player1,player2);
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
        logic = new Logic(FILAS,COLUMNAS);
        Thread.currentThread().interrupt();
        count=0;
        setPlayer(player1,player2);
        TableLayout tl=(TableLayout)findViewById(R.id.tabla);
        tl.removeAllViews();
    }

    private class Restart implements View.OnClickListener{
        public void onClick(View button){
            restart();
            paintAgain();
        }
    }

    public class GameBoton implements View.OnClickListener{
        int x;
        int y;
        public GameBoton(int x,int y) {
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
            Move move;
            do {
                move = humanTurn[turno].putPlay(c, check);
                Log.v("Ciclo: ", check + " " + c + " funcion:" + humanTurn[turno].validMove(move,player));
            }while(!humanTurn[turno].validMove(move,player));
            changeStatus(move,player);
                //}while(!(check && humanTurn[turno].isFreeBox(pressed,player)) || !(!check && humanTurn[turno].canWall(pressed)))

            /*ArrayList<Integer> statusArray = logic.board.getArrayStatus();
            doBoard(statusArray);*/
            Log.v(TAG,"boton game");
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
                lr.bottomMargin = -15;
                lr.topMargin = -15;
            }
            row.setLayoutParams(lr);
            tl.addView(row);
            for(int j=0;j<COLUMNAS;j++){
                doBoton(i,j,row,statusArray,contador,lr);
                contador++;
            }
        }
    }

    private void recuperateStatus(Bundle savedInstanceState){
        ArrayList<Integer> statusArray = savedInstanceState.getIntegerArrayList("estados");
        int intLevel = savedInstanceState.getInt("nivel");
        level = Level.values()[intLevel];
        player1=savedInstanceState.getInt("player1");
        player2=savedInstanceState.getInt("player2");
        if(statusArray!=null){
            ganadas=savedInstanceState.getInt("ganadas");
            jugadas=savedInstanceState.getInt("jugadas");
            design(statusArray);
            paintWinner();
            //selectLevel();
        }
    }
    private void paintWinner(){
        TextView text = (TextView) findViewById(R.id.jugadas);
        String s= getResources().getString(R.string.jugadas);
        s= s+jugadas;
        text.setText(s);
        text = (TextView) findViewById(R.id.ganadas);
        s= getResources().getString(R.string.ganadas);
        s= s+ganadas;
        text.setText(s);
    }

    private void setConfiguration(Bundle configuration){
        player1= configuration.getInt("player1");
        player2= configuration.getInt("player2");
        String user= configuration.getString("user");
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
            case REMOTE:
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
            recuperateStatus(savedInstanceState);
            setPlayer(player1,player2);
            return;
        }else{
            setPlayer(player1,player2);
        }
        Box player1=logic.board.getPlayer(Status.PLAYER1);
        Box player2=logic.board.getPlayer(Status.PLAYER2);
        design(statusArray);
        runThread(player1,player2);
    }

    private  void runThread(final Box player1, final Box player2){
        new Thread(new Runnable() {
            public void run(){
                while (true) {
                    synchronized (this){
                        int turno=count%turn.length;
                        Log.v("contador", "Numero: " + count + " Turno: " + turno);
                        Status player;
                        if(turno==1){
                            player=Status.PLAYER2;
                        }else{
                            player=Status.PLAYER1;
                        }
                        if(humanTurn[turno]!=null){
                            try {
                                turn[turno].askPlay(player);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                Date initDate = new Date();
                                Log.v("tiempo", "hora: " + initDate.getTime());
                                Move move;
                                do{
                                    move = turn[turno].askPlay(player);
                                    Log.v("turno", "IA: " + move);
                                }while(move==null);
                                changeStatus(move,player);
                                Thread.sleep(SLEEP);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        count++;
                    }
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            ArrayList<Integer> statusArray = logic.board.getArrayStatus();
                            Log.v("Array", "Estados: " + statusArray);
                            doBoard(statusArray);
                        }
                    });
/*
                    Coordinate cPlayer1=player1.getCoordenate();
                    Coordinate cPlayer2=player2.getCoordenate();
                    if(cPlayer1.getX()==FILAS-1 || cPlayer2.getX()==0){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                restart();
                                paintAgain();
                            }
                        });
                    }*/

                }
            }
        }).start();
    }

    private void changeStatus(Move move,Status player){
        Box boxNow = logic.board.getPlayer(player);
        Box boxFuture = logic.board.getPress(move.getC());
        if(move.getType()){
            boxNow.setStatus(Status.FREE);
            boxFuture.setStatus(player);
            Log.v("Movimiento","Nueva casilla: " + boxFuture + " Antigua: " + boxNow);
        }else{
            boxFuture.setStatus(Status.WALL);
            Log.v("Muro","Nueva casilla: " + boxFuture );
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "On start");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TAG,"On pause");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.v(TAG,"on Resume");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.v(TAG,"on Destroy");
    }

    //SALVAR ESTADO
    @Override
    public void onSaveInstanceState(Bundle state){
        ArrayList<Integer> statusBoard = logic.board.getArrayStatus();
        state.putIntegerArrayList("estados", statusBoard);
        state.putInt("nivel",level.getNum());
        state.putInt("ganadas",ganadas);
        state.putInt("jugadas",jugadas);
        state.putInt("player1",player1);
        state.putInt("player2",player2);
        super.onSaveInstanceState(state);
    }
}
