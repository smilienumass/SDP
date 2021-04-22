package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//this is for the saving the items from the pi

public class DatabaseHelperPi extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelperPi";

    private static final String NAME = "logged_items_table";
    private static final String ID = "ID";
    private static final String ITEM = "name";
    private static final String TIME = "timestamp";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference logged_items_ref = database.getReference("Logged_items_table");

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
        db.delete(NAME, null, null);
        onCreate(db);
    }

    public boolean seeBag( DatabaseHelperRegisterMode database, String json) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(NAME, null, null);
        db.execSQL("DELETE FROM " + NAME);
        ContentValues contentValues = new ContentValues();


//        String json = "{\"RSSI\":-27,\"Phase\":33,\"Readcount\":10,\"EPC\":\"E2004078410B02391140A53F\",\"Timestamp\":\"2020-11-06T13:00:01.336-0500\"}\n"

        String[] j = json.split("\n");
        for( int i = 0; i<j.length; i++){
            JSONObject jsonObject  = new JSONObject(j[i]);
            String tag_id = jsonObject.get("EPC").toString();
//            System.out.println(tag_id);

            contentValues.put(TIME, jsonObject.get("Timestamp").toString());
            contentValues.put(ID, tag_id);
            String item = null;

            Cursor data = database.getData();
            if(data.getCount()>0){
                while (data.moveToNext()){
                    String temp_id = data.getString(data.getColumnIndex("ID"));
                    if (temp_id.equals(tag_id)){
                        System.out.println(item + "  " + temp_id);
                        item = data.getString(data.getColumnIndex("name"));
                        break;
                    }
                }
            }

//            contentValues.put(ITEM, map.get(tag_id));
            if(item==null){
                item = "NA";
            }

            contentValues.put(ITEM, item);
//            DatabaseReference itemsRef = logged_items_ref.child(map.get(tag_id));

            DatabaseReference itemsRef = logged_items_ref.child(item);
            itemsRef.setValue(j[i]);

            long result = db.insert(NAME, null, contentValues);

//            Log.i("Insert", result + "");
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
        String query = "SELECT " + ID + " FROM " + NAME +
                " WHERE " + ITEM + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the Time that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemTime(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + TIME + " FROM " + NAME +
                " WHERE " + ITEM + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

//    /**
//     * Updates the name field
//     * @param newName
//     * @param id
//     * @param oldName
//     */
//    public void updateName(String newName, int id, String oldName){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + NAME + " SET " + ITEM +
//                " = '" + newName + "' WHERE " + ID + " = '" + id + "'" +
//                " AND " + ITEM + " = '" + oldName + "'";
//        Log.d(TAG, "updateName: query: " + query);
//        Log.d(TAG, "updateName: Setting name to " + newName);
//        db.execSQL(query);
//    }

//    /**
//     * Delete from database
//     * @param id
//     * @param name
//     */
//    public void deleteName(int id, String name){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + NAME + " WHERE "
//                + ID + " = '" + id + "'" +
//                " AND " + ITEM + " = '" + name + "'";
//        Log.d(TAG, "deleteName: query: " + query);
//        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
//        db.execSQL(query);
//    }


    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + NAME;
        DatabaseReference mPostReference = logged_items_ref;
        mPostReference.removeValue();

        db.execSQL(query);
    }

}





//            jsonObject.get("Phase").toString(),
//                            jsonObject.get("Readcount").toString(),
//                            jsonObject.get("EPC").toString(),
//                            jsonObject.get("Timestamp").toString());