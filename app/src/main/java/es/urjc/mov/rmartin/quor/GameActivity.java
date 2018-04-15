package es.urjc.mov.rmartin.quor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import es.urjc.mov.rmartin.quor.Game.Dijkstra;
import es.urjc.mov.rmartin.quor.Game.Human;
import es.urjc.mov.rmartin.quor.Game.IADijkstra;
import es.urjc.mov.rmartin.quor.Game.Level;
import es.urjc.mov.rmartin.quor.Game.Logic;
import es.urjc.mov.rmartin.quor.Game.Player;
import es.urjc.mov.rmartin.quor.Game.IAMedium;
import es.urjc.mov.rmartin.quor.Game.IARandom;
import es.urjc.mov.rmartin.quor.Game.PlayerMode;
import es.urjc.mov.rmartin.quor.Graphic.Box;
import es.urjc.mov.rmartin.quor.Graphic.Status;

public class GameActivity extends AppCompatActivity {
    private static String TAG="PRUEBA";
    static final int FILAS = 7;
    static final int COLUMNAS = 7;
    static final int PORCENTAJE = 20;
    static final double SIZE=1.5;
    static final int destinyPlayer1=FILAS-1;
    static final int destinyPlayer2=0;
    Logic logic;
    Player playerTop;
    Player playerBottom;
    int jugadas=0;
    int ganadas=0;
    int player1;
    int player2;
    Level level = Level.HARD;

    private void paintAgain(){
        design(logic.board.getArrayStatus());
        jugadas++;
        TextView text = (TextView) findViewById(R.id.jugadas);
        String s= getResources().getString(R.string.jugadas);
        s= s+jugadas;
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
        logic = new Logic(FILAS,COLUMNAS);
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

    private boolean canWall(Box player1,Box player2){
        Dijkstra play1 = new Dijkstra(logic.board,player1);
        ArrayList wayPlayer1 = play1.doWay(0);
        Dijkstra play2 = new Dijkstra(logic.board,player2);
        ArrayList wayPlayer2 = play2.doWay(logic.board.game.length-1);
        if(wayPlayer1.size()==0 || wayPlayer2.size()==0){
            Toast msg = Toast.makeText(GameActivity.this,"Camino bloqueado, no se puede poner muro",Toast.LENGTH_SHORT);
            msg.show();
            return false;
        }
        return true;
    }

    private boolean humanAction(Player player,Box player2, Box player1, Box pressed,int destiny,int pawn){
        Switch butMov=(Switch) findViewById(R.id.eleccionbottom);
        Log.v(TAG, "accion");
        if(butMov.isChecked() && player.isFreeBox(pressed)){//Movimiento
            if(pressed.getX()==destiny && !player.isFreeBox(pressed)){
                restart();
                paintAgain();
                ganadas++;
                TextView text = (TextView) findViewById(R.id.ganadas);
                String s= getResources().getString(R.string.ganadas);
                s= s+ganadas;
                text.setText(s);
                return true;
            }
            if (player2 != null) {
                ImageButton img = (ImageButton) findViewById(player2.getId());
                img.setImageResource(R.drawable.square);
                img.setBackgroundResource(0);
            }
            ImageButton img = (ImageButton) findViewById(pressed.getId());
            img.setBackgroundResource(pawn);
            return true;
        } else if(!butMov.isChecked()){//pared
            if(player.putWall(pressed)){
                pressed.setStatus(Status.WALL);
                if(canWall(player2,player1)){
                    ImageButton img=(ImageButton) findViewById(pressed.getId());
                    img.setImageResource(R.drawable.square_red);
                    return true;
                }
                pressed.setStatus(Status.FREE);
            }
        }
        return false;
    }

    private void getMove(Player player, Status status, int destiny,int pawn){
        Box cpu= logic.board.getPlayer(status);
        Box nextBox=player.getMove(destiny,status);
        if(nextBox==null){
            Toast msg = Toast.makeText(GameActivity.this,"Camino bloqueado",Toast.LENGTH_SHORT);
            msg.show();
            return;
        }
        if(cpu!=null){
            ImageButton img=(ImageButton) findViewById(cpu.getId());
            img.setImageResource(R.drawable.square);
            img.setBackgroundResource(0);
        }
        ImageButton img=(ImageButton) findViewById(nextBox.getId());

        img.setBackgroundResource(pawn);
        int posicion=nextBox.getX();
        if(posicion==destiny){
            restart();
            paintAgain();
        }
    }

    private void getWall(Player player, int destiny,Status status){
        Box nextBox=player.putWall(destiny,status);
        if(nextBox==null){
            pcMoves(destinyPlayer1);
            return;
        }
        ImageButton img=(ImageButton) findViewById(nextBox.getId());
        img.setImageResource(R.drawable.square_red);
    }

    private void pcMoves(int destiny){
        int move = (int) (Math.random() * 100);
        if (move > PORCENTAJE){
            getMove(playerTop, Status.PLAYER1, destiny,R.drawable.pawn_player1_back);
            return;
        }
        getWall(playerTop,destinyPlayer2,Status.PLAYER2);
    }

    public class GameBoton implements View.OnClickListener{
        int x;
        int y;
        public GameBoton(int x,int y) {
            this.x = x;
            this.y= y;
        }
        public void onClick(View button){
            Box player2 = logic.board.getPlayer(Status.PLAYER2);
            Box pressed = logic.board.getPress(x,y);
            Box player1 = logic.board.getPlayer(Status.PLAYER1);
            boolean moveOk=humanAction(playerBottom,player2,player1,pressed,destinyPlayer2,R.drawable.pawn_player2_back);
            if(moveOk){
                pcMoves(destinyPlayer1);
            }
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
        Log.v("RECUPERANDO: ", player1 + " " + player2 + " " + user);
    }

    private void setPlayer(int player1,int player2){
        switch (PlayerMode.getVal(player1)){
            case HUMAN:
                Switch eleccion=(Switch) findViewById(R.id.eleccionTop);
                TextView movimiento=(TextView) findViewById(R.id.textMoveTop);
                TextView wall = (TextView) findViewById(R.id.textWallTop);
                movimiento.setVisibility(View.VISIBLE);
                eleccion.setVisibility(View.VISIBLE);
                wall.setVisibility(View.VISIBLE);
                playerTop = new Human(logic.board);
                break;
            case CPU:
                playerTop=selectLevel(playerTop);
                break;
            case REMOTE:
                break;
        }
        switch (PlayerMode.getVal(player2)){
            case HUMAN:
                Switch eleccion=(Switch) findViewById(R.id.eleccionbottom);
                TextView movimiento=(TextView) findViewById(R.id.textMoveBottom);
                TextView wall = (TextView) findViewById(R.id.textWallBottom);
                movimiento.setVisibility(View.VISIBLE);
                eleccion.setVisibility(View.VISIBLE);
                wall.setVisibility(View.VISIBLE);
                playerBottom = new Human(logic.board);
                break;
            case CPU:
                playerBottom=selectLevel(playerBottom);
                break;
            case REMOTE:
                break;
        }
    }

    //syncronize para pedir jugadas
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
        }
        setPlayer(player1,player2);
        design(statusArray);
        Log.v(TAG, "On create");
        /*
        new Thread(new Runnable() {
            public void run() {


            }
        }).start();*/
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
/*
    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conf,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.facil:
                easy();
                return true;
            case R.id.medio:
                medium();
                return true;
            case R.id.hard:
                hard();
                return true;
            case R.id.help:
                ayuda();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void easy() {
        playerTop = new IARandom(logic.board);
        level = Level.EASY;
        Toast msg = Toast.makeText(GameActivity.this,"Nivel fácil",Toast.LENGTH_LONG);
        msg.show();
    }

    private void medium() {
        playerTop = new IAMedium(logic.board);
        level = Level.MEDIUM;
        Toast msg = Toast.makeText(GameActivity.this,"Nivel medio",Toast.LENGTH_SHORT);
        msg.show();
    }
    private void hard() {
        playerTop = new IADijkstra(logic.board);
        level = Level.HARD;
        Toast msg = Toast.makeText(GameActivity.this,"Nivel dificil",Toast.LENGTH_SHORT);
        msg.show();
    }

    private void ayuda(){
        Intent help = new Intent(GameActivity.this,HelpActivity.class);
        startActivity(help);
    }
    */
}
