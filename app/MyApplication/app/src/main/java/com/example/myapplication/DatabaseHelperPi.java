package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


public class DatabaseHelperPi extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelperPi";

    private static final String NAME = "logged_items_table";
    private static final String ID = "ID";
    private static final String ITEM = "name";
    private static final String TIME = "timestamp";




    public DatabaseHelperPi(Context context) {
        super(context, NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NAME
                + " (" + ID +" TEXT PRIMARY KEY, "
                + ITEM +" TEXT, "
                + TIME +" TIMESTAMP)";

        db.execSQL(createTable);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + NAME);
//        db.delete(NAME, null, null);
        onCreate(db);
    }

    public boolean seeBag(HashMap<String, String> map) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAME, null, null);
        ContentValues contentValues = new ContentValues();


        String json = "{\"RSSI\":-27,\"Phase\":33,\"Readcount\":10,\"EPC\":\"E2004078410B02391140A53F\",\"Timestamp\":\"2020-11-06T13:00:01.336-0500\"}\n" +
                "{\"RSSI\":-57,\"Phase\":56,\"Readcount\":9,\"EPC\":\"E2004078410B01901140A4D7\",\"Timestamp\":\"2020-11-06T13:00:01.355-0500\"}\n" +
                "{\"RSSI\":-45,\"Phase\":45,\"Readcount\":10,\"EPC\":\"E2004078410B02151140A50F\",\"Timestamp\":\"2020-11-06T13:00:01.365-0500\"}\n" +
                "{\"RSSI\":-36,\"Phase\":132,\"Readcount\":10,\"EPC\":\"E2004078410B02561140A558\",\"Timestamp\":\"2020-11-06T13:00:01.340-0500\"}\n" +
                "{\"RSSI\":-46,\"Phase\":137,\"Readcount\":11,\"EPC\":\"E2004078410B01921140A4D8\",\"Timestamp\":\"2020-11-06T13:00:01.377-0500\"}\n" +
                "{\"RSSI\":-37,\"Phase\":106,\"Readcount\":10,\"EPC\":\"E2004078410B02161140A508\",\"Timestamp\":\"2020-11-06T13:00:01.347-0500\"}\n" +
                "{\"RSSI\":-49,\"Phase\":56,\"Readcount\":11,\"EPC\":\"E2004078410B02321140A528\",\"Timestamp\":\"2020-11-06T13:00:01.384-0500\"}" ;


        String[] j = json.split("\n");
        for( int i = 0; i<j.length; i++){
            JSONObject jsonObject  = new JSONObject(j[i]);

            String tag_id = jsonObject.get("EPC").toString();
            contentValues.put(ID, tag_id);

            contentValues.put(ITEM, map.get(tag_id));
            contentValues.put(TIME, jsonObject.get("Timestamp").toString());
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
//    public Cursor getItemID(String name){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT " + ID + " FROM " + NAME +
//                " WHERE " + ITEM + " = '" + name + "'";
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
//    public void updateName(String newName, int id, String oldName){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + NAME + " SET " + ITEM +
//                " = '" + newName + "' WHERE " + ID + " = '" + id + "'" +
//                " AND " + ITEM + " = '" + oldName + "'";
//        Log.d(TAG, "updateName: query: " + query);
//        Log.d(TAG, "updateName: Setting name to " + newName);
//        db.execSQL(query);
//    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
//    public void deleteName(int id, String name){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + NAME + " WHERE "
//                + ID + " = '" + id + "'" +
//                " AND " + ITEM + " = '" + name + "'";
//        Log.d(TAG, "deleteName: query: " + query);
//        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
//        db.execSQL(query);
//    }
}





//        String json = "{\"RSSI\":-27,\"Phase\":33,\"Readcount\":10,\"EPC\":\"E2004078410B02391140A53F\",\"Timestamp\":\"2020-11-06T13:00:01.336-0500\"}\n" +
//                "{\"RSSI\":-57,\"Phase\":56,\"Readcount\":9,\"EPC\":\"E2004078410B01901140A4D7\",\"Timestamp\":\"2020-11-06T13:00:01.355-0500\"}\n" +
//                "{\"RSSI\":-45,\"Phase\":45,\"Readcount\":10,\"EPC\":\"E2004078410B02151140A50F\",\"Timestamp\":\"2020-11-06T13:00:01.365-0500\"}\n" +
//                "{\"RSSI\":-36,\"Phase\":132,\"Readcount\":10,\"EPC\":\"E2004078410B02561140A558\",\"Timestamp\":\"2020-11-06T13:00:01.340-0500\"}\n" +
//                "{\"RSSI\":-46,\"Phase\":137,\"Readcount\":11,\"EPC\":\"E2004078410B01921140A4D8\",\"Timestamp\":\"2020-11-06T13:00:01.377-0500\"}\n" +
//                "{\"RSSI\":-37,\"Phase\":106,\"Readcount\":10,\"EPC\":\"E2004078410B02161140A508\",\"Timestamp\":\"2020-11-06T13:00:01.347-0500\"}\n" +
//                "{\"RSSI\":-49,\"Phase\":56,\"Readcount\":11,\"EPC\":\"E2004078410B02321140A528\",\"Timestamp\":\"2020-11-06T13:00:01.384-0500\"}\n" +
//                "{\"RSSI\":-34,\"Phase\":106,\"Readcount\":10,\"EPC\":\"E2004078410B02141140A507\",\"Timestamp\":\"2020-11-06T13:00:01.351-0500\"}\n" +
//                "{\"RSSI\":-30,\"Phase\":106,\"Readcount\":11,\"EPC\":\"E2004078410B02411140A540\",\"Timestamp\":\"2020-11-06T13:00:01.332-0500\"}";


//            jsonObject.get("Phase").toString(),
//                            jsonObject.get("Readcount").toString(),
//                            jsonObject.get("EPC").toString(),
//                            jsonObject.get("Timestamp").toString());