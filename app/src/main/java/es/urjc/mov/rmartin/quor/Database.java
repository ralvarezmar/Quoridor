package es.urjc.mov.rmartin.quor;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private final static String NAME = "database.db";
    private final static int VERSION = 1;
    public int winners;
    public int played;
    private static final int INICIAL=0;

    public final String DATA = "Data";
    private final String CREATE_TABLASCORE = "CREATE TABLE " + DATA + " (" +
            " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " Nick TEXT, " +
            " Won INTEGER, " +
            " Play INTEGER);";


    Database(final Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // Log.v(Database.class.getName(), "Creating DB.");
        db.execSQL(CREATE_TABLASCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int from, int to) {
        database.execSQL("DROP TABLE IF EXISTS " + DATA);
        database.execSQL(CREATE_TABLASCORE);
    }

    public boolean consultaBD(String nick) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + DATA + " WHERE Nick='"+nick+"'",null);
        if (c.moveToFirst()) {
            return true;
        }
        db.close();
        c.close();
        return false;
    }

    public void putValue(String nick){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nick", nick);
        values.put("Won", INICIAL);
        values.put("Play", INICIAL);
        db.insert(DATA, null,values);
        db.close();
    }

    public DataPlayer getData(String nick){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT _id, Nick, Won, MAX(Play) FROM " + DATA + " WHERE Nick='"+nick+"'", null);
        if (c != null) {
            c.moveToFirst();
        }
        DataPlayer player = new DataPlayer(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3));
        winners = c.getInt(2);
        System.out.print("Database ganadores actual: " + winners);
        played = c.getInt(3);
        System.out.print("Database jugadas actual: " + played);
        db.close();
        c.close();
        return player;
    }

    public void modifyValues(String nick, int won, int play){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT _id, Nick, Won, MAX(Play) FROM " + DATA + " WHERE Nick='"+nick+"'", null);
        if (c != null) {
            c.moveToFirst();
        }
       DataPlayer subject = new DataPlayer(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3));
        int id = c.getInt(0);

        db.close();
        c.close();

        SQLiteDatabase db2 = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put("Nick", nick);
        values.put("Won", won);
        values.put("Play", play);
        db2.update(DATA, values, "_id=" + id, null);
        db2.close();
    }
}