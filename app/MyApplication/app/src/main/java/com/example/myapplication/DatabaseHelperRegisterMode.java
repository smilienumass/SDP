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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelperRegisterMode extends SQLiteOpenHelper {

    private static final String TAG = "DBRegMode";


    private static final String NAME = "Registered_items";
    private static final String ID = "ID";
    private static final String ITEM = "name";
//    private static final String TIME = "timestamp";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference registered_items_ref = database.getReference("Registered_items_table");

    public DatabaseHelperRegisterMode(Context context) {
        super(context, NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NAME
                + " (" + ID +" TEXT PRIMARY KEY, "
                + ITEM +" TEXT )";

        db.execSQL(createTable);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + NAME);
//        db.delete(NAME, null, null);
        onCreate(db);
    }



    public String addData(  String json, String itemName) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(NAME, null, null);
        ContentValues contentValues = new ContentValues();

        String tag_id="";

        String[] j = json.split("\n");
//        for( int i = 0; i<j.length; i++){
        for( int i = 0; i<1; i++){

            JSONObject jsonObject  = new JSONObject(j[i]);


            tag_id = jsonObject.get("EPC").toString();

            contentValues.put(ID, tag_id);
            contentValues.put(ITEM, itemName );

            DatabaseReference itemsRef = registered_items_ref.child(itemName);
            itemsRef.setValue(j[i]);


            long result = db.insert(NAME, null, contentValues);

            Log.i("Insert", result + "");
        }


//        db.close();
        return tag_id;

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
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + NAME + " WHERE "
//                + ID + " = '" + id + "'" +
//                " AND " + ITEM + " = '" + name + "'";
        String query = "DELETE FROM " + NAME + " WHERE "
                + ITEM + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");

        DatabaseReference mPostReference = registered_items_ref.child(name);
        mPostReference.removeValue();
        db.execSQL(query);
    }



    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + NAME;

        DatabaseReference mPostReference = registered_items_ref;
        mPostReference.removeValue();
        db.execSQL(query);
    }


}
