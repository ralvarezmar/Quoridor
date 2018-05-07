package es.urjc.mov.rmartin.quor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    public class Start implements View.OnClickListener{
        EditText ed;
        public Start(EditText ed) {
            this.ed = ed;
        }
        public void onClick(View button){
            Spinner player1 = (Spinner) findViewById(R.id.lista1);
            int selected1 = player1.getSelectedItemPosition();
            Spinner player2 = (Spinner) findViewById(R.id.lista2);
            int selected2 = player2.getSelectedItemPosition();

            Editable edit = ed.getText();
            String s = edit.toString();
            CheckBox rbcrear = (CheckBox) findViewById(R.id.crear);
            CheckBox rbunir = (CheckBox) findViewById(R.id.unir);
            int crear;
            if(rbunir.isChecked()){
                crear = 0;
            }else if(rbcrear.isChecked() || rbcrear.isChecked() && rbunir.isChecked()){
                crear=1;
            }else{
                crear=2;
            }
            Intent game = new Intent(MainActivity.this,GameActivity.class);
            game.putExtra("player1",selected1);
            game.putExtra("player2",selected2);
            game.putExtra("user",s);
            game.putExtra("crear",crear);
            //Bundle msg = new Bundle();
            startActivity(game);
        }
    }

    public class Help implements View.OnClickListener{
        public void onClick(View button){
            Intent help = new Intent(MainActivity.this,HelpActivity.class);
            startActivity(help);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button butStart = (Button) findViewById(R.id.start);
        EditText user = (EditText) findViewById(R.id.user);
        Button butHelp = (Button) findViewById(R.id.help);


        butStart.setOnClickListener(new Start(user));
        butHelp.setOnClickListener(new Help());

    }
}
