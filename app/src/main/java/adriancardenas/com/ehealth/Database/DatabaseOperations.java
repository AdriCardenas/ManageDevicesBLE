package adriancardenas.com.ehealth.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import adriancardenas.com.ehealth.Database.DatabaseMetadata.*;
import adriancardenas.com.ehealth.Utils.Constants;

import java.util.HashMap;

public class DatabaseOperations {
    private static DatabaseHelper databaseHelper;
    private static DatabaseOperations databaseOperations = new DatabaseOperations();

    private DatabaseOperations() {
    }

    public static DatabaseOperations getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseOperations;
    }

    public boolean insertWeight(String date, String value) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIGHT_COLUMS.ID, date);
        contentValues.put(WEIGHT_COLUMS.VALUE, value);
        try {
            sqLiteDatabase.insertWithOnConflict(TABLES.WEIGHT, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteConstraintException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public HashMap<String, Float> getWeights() {
        HashMap<String, Float> weights = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", TABLES.WEIGHT);
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            weights.put(cursor.getString(0), cursor.getFloat(1));
        }

        cursor.close();
        return weights;
    }

    public Float getLastWeight() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", TABLES.WEIGHT);
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        float weight = 0;
        if (cursor.moveToLast()) {
            weight = cursor.getFloat(1);
        }
        cursor.close();
        return weight;
    }

    public HashMap<String, Integer> getSteps() {
        HashMap<String, Integer> steps = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", TABLES.STEPS);
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            steps.put(cursor.getString(0), cursor.getInt(1));
        }

        cursor.close();
        return steps;
    }

    public boolean insertSteps(String date, String value) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STEPS_COLUMS.ID, date);
        contentValues.put(STEPS_COLUMS.VALUE, value);
        try {
            sqLiteDatabase.insertWithOnConflict(TABLES.STEPS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteConstraintException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public HashMap<String, Integer> getHeartRates() {
        HashMap<String, Integer> rates = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", TABLES.HEART);
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            rates.put(cursor.getString(0), cursor.getInt(1));
        }

        cursor.close();
        return rates;
    }

    public boolean insertHeartRate(String date, String value) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HEART_RATE_COLUMS.ID, date);
        contentValues.put(HEART_RATE_COLUMS.VALUE, value);
        try {
            sqLiteDatabase.insertWithOnConflict(TABLES.HEART, null, contentValues, SQLiteDatabase.CONFLICT_ABORT);
        } catch (SQLiteConstraintException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public String getMeanSteps() {
        int mean = 0;
        if(!getSteps().isEmpty()){
            for(int value : getSteps().values()){
                mean+=value;
            }
            mean/=getSteps().values().size();
        }
        return String.valueOf(mean);
    }
}
