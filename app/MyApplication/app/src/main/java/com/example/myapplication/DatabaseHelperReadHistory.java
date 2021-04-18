package com.example.myapplication;


import android.annotation.SuppressLint;
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


public class DatabaseHelperReadHistory extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelperReadHistory";

    private static final String NAME = "bag_history_table";
    private static final String TIME = "timestamp";
    private static final String ID = "ID";
    private static final String ITEM = "name";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference bag_history_ref = database.getReference("bag_history_table");

    public DatabaseHelperReadHistory(Context  context)  {super(context, NAME, null, 1);}

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NAME
                + " (" + TIME +" TIMESTAMP, "
                + ID +" TEXT , "
                + ITEM +" TEXT)";

        db.execSQL(createTable);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + NAME);
//        db.delete(NAME, null, null);
        onCreate(db);
    }


    public boolean addToDB( DatabaseHelperRegisterMode database, String json) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(NAME, null, null);

        ContentValues contentValues = new ContentValues();


//        String json = "{\"RSSI\":-27,\"Phase\":33,\"Readcount\":10,\"EPC\":\"E2004078410B02391140A53F\",\"Timestamp\":\"2020-11-06T13:00:01.336-0500\"}\n"

        String[] j = json.split("\n");
        for( int i = 0; i<j.length; i++){
            JSONObject jsonObject  = new JSONObject(j[i]);
            String tag_id = jsonObject.get("EPC").toString();
//

            contentValues.put(TIME, jsonObject.get("Timestamp").toString());

            contentValues.put(ID, tag_id+ " "+  jsonObject.get("Timestamp").toString());
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
            contentValues.put(ITEM, item);
//            contentValues.put(ITEM, map.get(tag_id));

            if(item==null){
                item = "NA";
            }


//            contentValues.put(TIME, jsonObject.get("Timestamp").toString());

//            DatabaseReference itemsRef = logged_items_ref.child(map.get(tag_id));

            DatabaseReference itemsRef = bag_history_ref.child(item);
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

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + NAME;
        DatabaseReference mPostReference= bag_history_ref;
        mPostReference.removeValue();

        db.execSQL(query);
    }


}
