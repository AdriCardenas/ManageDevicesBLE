package adriancardenas.com.ehealth.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import adriancardenas.com.ehealth.Database.DatabaseMetadata.*;

import java.util.HashMap;

public class DatabaseOperations {
    private static DatabaseHelper databaseHelper;
    private static DatabaseOperations databaseOperations = new DatabaseOperations();

    private DatabaseOperations(){}

    public static DatabaseOperations getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseOperations;
    }

    public boolean insertWeight(String date, String value){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIGHT_COLUMS.ID, date);
        contentValues.put(WEIGHT_COLUMS.VALUE, value);
        try{
            sqLiteDatabase.insertWithOnConflict(TABLES.WEIGHT, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }catch (SQLiteConstraintException exception){
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public HashMap<String, Float> getWeights(){
        HashMap<String, Float> weights = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", TABLES.WEIGHT);
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        while (cursor.moveToNext()){
            weights.put(cursor.getString(0),cursor.getFloat(1));
        }

        cursor.close();
        return weights;
    }

    public Float getLastWeight(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", TABLES.WEIGHT);
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        float weight = 0;
        if (cursor.moveToLast()){
                  weight = cursor.getFloat(1);
        }
        cursor.close();
        return weight;
    }
}
