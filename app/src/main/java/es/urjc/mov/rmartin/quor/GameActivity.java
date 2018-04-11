package es.urjc.mov.rmartin.quor;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import es.urjc.mov.rmartin.quor.Graphic.Box;

import static es.urjc.mov.rmartin.quor.Graphic.Box.Status.PLAYER;


public class GameActivity extends AppCompatActivity {
    private static String TAG="PRUEBA";
    static final int FILAS = 7;
    static final int COLUMNAS = 7;
    static final int PORCENTAJE = 20;
    static final double SIZE=1.5;
    static final int destinyPlayer1=FILAS-1;
    static final int destinyPlayer2=0;
    Logic logic;
    Player ia;
    Player human;
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

    private void selectLevel(){
        switch (level){
            case EASY:
                ia = new IARandom(logic.board);
                break;
            case MEDIUM:
                ia = new IAMedium(logic.board);
                break;
            case HARD:
                ia = new IADijkstra(logic.board);
                break;
        }
    }

    private void restart(){
        logic = new Logic(FILAS,COLUMNAS,player1,player2);
        selectLevel();
        human = new Human(logic.board);
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
    private boolean action(Box player, Box pressed,int destiny){
        Switch butMov=(Switch) findViewById(R.id.eleccion);
        Log.v(TAG, "accion");
        if(butMov.isChecked() && human.isFreeBox(pressed)){//Movimiento
            if(pressed.getX()==destinyPlayer2 && !human.isFreeBox(pressed)){
                restart();
                paintAgain();
                ganadas++;
                TextView text = (TextView) findViewById(R.id.ganadas);
                String s= getResources().getString(R.string.ganadas);
                s= s+ganadas;
                text.setText(s);
                return true;
            }
            if (player != null) {
                ImageButton img = (ImageButton) findViewById(player.getId());
                img.setImageResource(R.drawable.square);
                img.setBackgroundResource(0);
            }
            ImageButton img = (ImageButton) findViewById(pressed.getId());
            img.setBackgroundResource(R.drawable.pawn_player_back);
            return true;
        } else if(!butMov.isChecked()){//pared
            if(human.putWall(pressed)){
                pressed.setStatus(Box.Status.WALL);
                if(canWall(player,logic.board.getCpu())){
                    ImageButton img=(ImageButton) findViewById(pressed.getId());
                    img.setImageResource(R.drawable.square_red);
                    return true;
                }
                pressed.setStatus(Box.Status.FREE);
            }
        }
        return false;
    }

    private void getMove(Box cpu,int destiny){
        Box nextBox=ia.getMove(destiny);
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
        img.setBackgroundResource(R.drawable.pawn_computer_back);
        int posicion=nextBox.getX();
        if(posicion==destiny){
            restart();
            paintAgain();
        }
    }

    private void getWall(int destiny){
        Box nextBox=ia.putWall(destiny);
        if(nextBox==null){
            pcMoves(destinyPlayer1);
            return;
        }
        ImageButton img=(ImageButton) findViewById(nextBox.getId());
        img.setImageResource(R.drawable.square_red);
    }

    private void pcMoves(int destiny){
        int move = (int) (Math.random() * 100);
        Box cpu= logic.board.getCpu();
        if (move > PORCENTAJE){ //MOVIMIENTO
            getMove(cpu,destiny);
            return;
        } //PARED
        getWall(destinyPlayer2);
    }

    public class GameBoton implements View.OnClickListener{
        int x;
        int y;
        public GameBoton(int x,int y) {
            this.x = x;
            this.y= y;
        }
        public void onClick(View button){
            Box player = logic.board.getPlayer(PLAYER);
            Box pressed = logic.board.getPress(x,y);
            boolean moveOk=action(player,pressed,destinyPlayer2);
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

    private Box.Status[] setBoxes(Box.Status[] boxes){
        boxes[0]= Box.Status.FREE;
        boxes[1]=Box.Status.WALL;
        boxes[2]= PLAYER;
        boxes[3]=Box.Status.CPU;
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
        backs[2]= R.drawable.pawn_player_back;
        backs[3]=R.drawable.pawn_computer_back;
        return backs;
    }

    private void doBoton(int x, int y, TableRow row, ArrayList<Integer> statusArray, int id, TableRow.LayoutParams lr){
        Box.Status boxes[]=new Box.Status[4];
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

        if(statusArray!=null){
            ganadas=savedInstanceState.getInt("ganadas");
            jugadas=savedInstanceState.getInt("jugadas");
            design(statusArray);
            paintWinner();
            selectLevel();
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
    
/*
    private void typeOfPlayer(int type,Player player){
        switch (type){
            case 0:
                player=new Human(logic.board);
                break;
            case 1:
                player=new IADijkstra(logic.board);
                break;
            case 2:
                player=new Remote(logic.board)
                break;
        }
    }*/

    private void setConfiguration(Bundle configuration){
        player1= configuration.getInt("player1")+2;
        player2= configuration.getInt("player2")+2;
        String user= configuration.getString("user");
        Log.v("RECUPERANDO: ", player1 + " " + player2 + " " + user);
    }

    private void setPlayer(int player1,int player2){
        switch (Box.Status.values()[player1]){
            case PLAYER:
                LinearLayout ll = (LinearLayout) findViewById(R.id.topBoard);
                ll.setGravity(Gravity.CENTER);
                TextView pared = new TextView(this);
                ll.addView(pared);
                Switch selected = new Switch(this);
                selected.setChecked(true);
                break;
            case CPU:

                break;
            case REMOTE:

                break;
        }
        switch (Box.Status.values()[player2]){
            case PLAYER:
                LinearLayout ll = (LinearLayout) findViewById(R.id.bottomBoard);
                ll.setGravity(Gravity.CENTER);
                TextView pared = new TextView(this);
                ll.addView(pared);
                Switch selected = new Switch(this);
                selected.setChecked(true);
                break;
            case CPU:

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
        if(configuration!=null){
            setConfiguration(configuration);
            setPlayer(player1,player2);
        }
        logic = new Logic(FILAS,COLUMNAS,player1,player2);
        human = new Human(logic.board);
        ArrayList<Integer> statusArray = logic.board.getArrayStatus();
        if(savedInstanceState!=null){
            recuperateStatus(savedInstanceState);
            return;
        }
        selectLevel();
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
        super.onSaveInstanceState(state);
    }

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
        ia = new IARandom(logic.board);
        level = Level.EASY;
        Toast msg = Toast.makeText(GameActivity.this,"Nivel fácil",Toast.LENGTH_LONG);
        msg.show();
    }

    private void medium() {
        ia = new IAMedium(logic.board);
        level = Level.MEDIUM;
        Toast msg = Toast.makeText(GameActivity.this,"Nivel medio",Toast.LENGTH_SHORT);
        msg.show();
    }
    private void hard() {
        ia = new IADijkstra(logic.board);
        level = Level.HARD;
        Toast msg = Toast.makeText(GameActivity.this,"Nivel dificil",Toast.LENGTH_SHORT);
        msg.show();
    }

    private void ayuda(){
        Intent help = new Intent(GameActivity.this,HelpActivity.class);
        startActivity(help);
    }
}