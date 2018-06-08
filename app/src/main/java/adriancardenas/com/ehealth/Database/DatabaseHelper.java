package adriancardenas.com.ehealth.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import adriancardenas.com.ehealth.Database.DatabaseMetadata.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "agenda.db";
    private static final String CREATE = "CREATE TABLE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(CREATE + " %s (%s TEXT PRIMARY KEY, %s FLOAT NOT NULL)", TABLES.WEIGHT, WEIGHT_COLUMS.ID, WEIGHT_COLUMS.VALUE));
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLES.WEIGHT);
        onCreate(db);
    }
}
