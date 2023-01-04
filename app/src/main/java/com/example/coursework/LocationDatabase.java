package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class LocationDatabase extends SQLiteOpenHelper {

    private Context context;
    private String TABLE_NAME = "locations";
    private String COLUMN_ID = "id";
    private String COLUMN_NAME = "name";
    private String COLUMN_LATITUDE = "latitude";
    private String COLUMN_LONGITUDE = "Longitude";
    private String COLUMN_PRICE_RANGE = "price_range";
    private String COLUMN_WEATHER_PREFERENCE = "preference";



    public LocationDatabase(@Nullable Context context){
        super(context, "Locations.db", null, 1);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_PRICE_RANGE + " INTEGER, " +
                COLUMN_WEATHER_PREFERENCE + " TEXT);";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    void addLocation(String locationName, double latitude, double longitude, String weatherPreference, int priceRange){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, locationName);
        contentValues.put(COLUMN_LATITUDE, latitude);
        contentValues.put(COLUMN_LONGITUDE, longitude);
        contentValues.put(COLUMN_PRICE_RANGE, priceRange);
        contentValues.put(COLUMN_WEATHER_PREFERENCE, weatherPreference);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Fail adding to database", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successful: Added to database", Toast.LENGTH_SHORT).show();
        }
    }
}
