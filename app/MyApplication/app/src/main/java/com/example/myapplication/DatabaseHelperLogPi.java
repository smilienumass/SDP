package com.example.myapplication;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class DatabaseHelperLogPi extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelperLogPi";

    private static final String NAME = "logs_table";
    private static final String TIME = "timestamp";
//    private static final String ITEM = "name";
    private static final String DETECTION = "detection";

    private static final String MAGNITUDE = "magnitude";
    private static final String GYROSCOPE = "gyroscope";
    private static final String ACCELEROMETER = "accelerometer";
    private static final String TEMPERATURE = "temperature";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference logs_ref = database.getReference("Logs_table");

    public DatabaseHelperLogPi(Context context) {
        super(context, NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NAME
                + " (" + TIME +" TIMESTAMP, "
                + DETECTION +" TEXT, "
                + MAGNITUDE +" TEXT, "
                + GYROSCOPE +" TEXT, "
                + ACCELEROMETER +" TEXT, "
                + TEMPERATURE +" TEXT)";

        db.execSQL(createTable);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + NAME);
//        db.delete(NAME, null, null);
        onCreate(db);
    }


    public boolean seeLog( String json) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(NAME, null, null);
        ContentValues contentValues = new ContentValues();


        String[] j = json.split("\n");
        for( int i = 0; i<j.length; i++){
            JSONObject jsonObject  = new JSONObject(j[i]);


            String detect = jsonObject.get("detection").toString();
//            contentValues.put(DETECTION, detection);

//            contentValues.put(ITEM, map.get(tag_id));
            contentValues.put(TIME, jsonObject.get("time").toString());
            contentValues.put(DETECTION, detect);
            contentValues.put(MAGNITUDE, jsonObject.get("magnitude").toString());
            contentValues.put(GYROSCOPE, jsonObject.get("Gyroscope").toString());
            contentValues.put(ACCELEROMETER, jsonObject.get("Accelerometer").toString());
            contentValues.put(TEMPERATURE, jsonObject.get("Temperature").toString());
//            DatabaseReference itemsRef = logged_items_ref.child(tag_id);
////            itemsRef.setValue(tag_id);
//            DatabaseReference infoRef  = itemsRef.child("Item Name");
//            infoRef.setValue(map.get(tag_id));
//            infoRef.child("Timestamp").setValue(jsonObject.get("Timestamp").toString());
            DatabaseReference itemsRef = logs_ref.child(jsonObject.get("time").toString());
            itemsRef.setValue(j[i]);


            long result = db.insert(NAME, null, contentValues);

            Log.i("Insert", result + "");
        }


        db.close();
        return true;

    }


    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DETECTION + " FROM " + NAME +
                " WHERE " + TIME + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + NAME;
        DatabaseReference mPostReference = logs_ref;
        mPostReference.removeValue();

        db.execSQL(query);
    }



}
