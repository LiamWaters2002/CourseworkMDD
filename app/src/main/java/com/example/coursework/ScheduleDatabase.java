package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ScheduleDatabase extends SQLiteOpenHelper {

    private Context context;
    private String TABLE_NAME = "locations";
    private String COLUMN_ID = "id";
    private String COLUMN_NAME = "name";
    private String COLUMN_TYPE = "type";
    private String COLUMN_LATITUDE = "latitude";
    private String COLUMN_LONGITUDE = "longitude";
    private String COLUMN_TIME = "time";
    private String COLUMN_WEATHER_PREFERENCE = "weather_preference";



    public ScheduleDatabase(@Nullable Context context){
        super(context, "schedule.db", null, 1);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_TIME + " STRING, " +
                COLUMN_WEATHER_PREFERENCE + " STRING);";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    void addLocation(String locationName, double latitude, double longitude, String time, String placeType, String weatherPreference){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, locationName);
        contentValues.put(COLUMN_LATITUDE, latitude);
        contentValues.put(COLUMN_LONGITUDE, longitude);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_TYPE, placeType);
        contentValues.put(COLUMN_WEATHER_PREFERENCE, weatherPreference);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Fail adding to database", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successful: Added to database", Toast.LENGTH_SHORT).show();
        }
    }

    protected Cursor readDatabase(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if(sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    protected void deleteRow(int id){
        String query = "DELETE FROM " +  TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(query);
        Toast.makeText(context, "Deleted element", Toast.LENGTH_SHORT).show();
    }

    protected void deleteRow(String time){
        String query = "DELETE FROM " +  TABLE_NAME + " WHERE " + COLUMN_TIME + " = " + time;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(query);
    }


    protected Cursor fetchRow(String time){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TIME + " = " + time;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        if(sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }
}
