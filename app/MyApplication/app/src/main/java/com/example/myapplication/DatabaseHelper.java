package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



// This is for saving items entered on the app

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelper";
    private static String TABLE_NAME = "items_table";
    private static final String ID = "ID";
    private static final String ITEM = "name";


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference items_list_ref = database.getReference("Items_table");


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
//        TABLE_NAME = table_name;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL2 +" TEXT)";
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + ID +" TEXT PRIMARY KEY, " +
                ITEM +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item, DatabaseHelperRegisterMode rDatabaseHelper) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ///// Think about making sure item name is all lowercase


        String id = "null";
        Cursor data = rDatabaseHelper.getData();


        //get matching id
        if(data.getCount()>0){
            while (data.moveToNext()){
                String temp_name = data.getString(data.getColumnIndex("name"));
                if (temp_name.equals(item)){
                    System.out.println(item + "  " + temp_name);
                    id = data.getString(data.getColumnIndex("ID"));
                    break;
                }
            }

        }
//
//        for (Map.Entry<String, String> e : map.entrySet()){
////            System.out.println(e.getKey() + " " + e.getValue());
//            String val = e.getValue();
//            if(val.equals(item)){
//                id = e.getKey();
//                break;
//            }
//        }
        //checking if it's not already in database
        Cursor checking_data = getData();

        if(checking_data.getCount()>0){
            while (checking_data.moveToNext()){
                String checking_id = checking_data.getString(checking_data.getColumnIndex("ID"));
                if (checking_id.equals(id)){
                    System.out.println(checking_id + "  " + id);
                    break;
                }
                else {
                    contentValues.put(ID, id);
                    contentValues.put(ITEM, item);

                    Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

                    long result = db.insert(TABLE_NAME, null, contentValues);
                    db.close();
                    //if date as inserted incorrectly it will return -1
                    if (result == -1) {
                        return false;
                    } else {
                        ///for updating firebase
                        DatabaseReference postsRef = items_list_ref.child(item);
                        postsRef.setValue(id);
                        return true;
                    }

                }
            }
        }
        else{
            contentValues.put(ID, id);
            contentValues.put(ITEM, item);

            Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

            long result = db.insert(TABLE_NAME, null, contentValues);
             db.close();
        //if date as inserted incorrectly it will return -1
            if (result == -1) {
                return false;
            } else {
                ///for updating firebase
                DatabaseReference postsRef = items_list_ref.child(item);
                postsRef.setValue(id);
                return true;
            }
        }






        db.close();
        return true;

    }


    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
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
        String query = "SELECT " + ID + " FROM " + TABLE_NAME +
                " WHERE " + ITEM + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, String id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + ITEM +
                " = '" + newName + "' WHERE " + ID + " = '" + id + "'" +
                " AND " + ITEM + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(String id, String name){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + ITEM + " = '" + name + "'";
//                " AND "  + ITEM + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");

        DatabaseReference mPostReference = items_list_ref.child(name);
        mPostReference.removeValue();

        db.execSQL(query);
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;

        DatabaseReference mPostReference = items_list_ref;
        mPostReference.removeValue();
        db.execSQL(query);
    }

}
