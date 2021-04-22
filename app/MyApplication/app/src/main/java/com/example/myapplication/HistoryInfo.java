package com.example.myapplication;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TableLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;



public class HistoryInfo extends AppCompatActivity {
    private static final String TAG = "HistoryInfo";

    DatabaseHelperReadHistory hDatabaseHelper;
    DatabaseHelper tripDatabaseHelper;
    private String selectedItem;
    private String selectedTime;

    private ListView mListView;
//    DatabaseHelper curDatabaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        hDatabaseHelper = new DatabaseHelperReadHistory(this);
        tripDatabaseHelper = new DatabaseHelper(this);
        Intent receivedIntent = getIntent();
        selectedItem = receivedIntent.getStringExtra("name"); //NOTE: -1 is just the default value
        selectedTime = receivedIntent.getStringExtra("time");

        showHistoryInfo();
    }


    private void showHistoryInfo() {
        Cursor data = hDatabaseHelper.getData();
        Cursor idCursor;

        if(data.getCount() >0) {
            while (data.moveToNext()) {

                boolean inTrip = false;
                String temp_time = data.getString(data.getColumnIndex("timestamp"));

                if(temp_time.equals(selectedTime)) {
                    System.out.println(selectedTime + "  " + temp_time);

                    ArrayList<String> info = new ArrayList<>();
                    String time = data.getString(data.getColumnIndex("timestamp"));
                    String[] t = time.split(" ");
                    String finalTime  = t[0];
                    String name = data.getString(data.getColumnIndex("name"));
                    String id = data.getString(data.getColumnIndex("ID"));
                    String status="";
//                            = data.getString(data.getColumnIndex("gyroscope"));
//                    String acc = data.getString(data.getColumnIndex("accelerometer"));
//                    String temp = data.getString(data.getColumnIndex("temperature"));

                    Cursor newCursor;
                    Cursor curdata =  tripDatabaseHelper.getData();


                    if(curdata.getCount() >0) {
                        //  IF data bringing on trip
                        while (curdata.moveToNext()){
                            String tempName = curdata.getString(curdata.getColumnIndex("name"));
                            String temp = curdata.getString(curdata.getColumnIndex("id"));

                            ///Check if in bag
                            boolean sta = false;
                            idCursor = hDatabaseHelper.getItemID(name);
                            if (temp.equals(id)){
                                inTrip = true;
                            }
                            // that id exist in read database
                            if (idCursor.getCount()>0) {
                                sta = true;
                            }

                            /// get status
                            if (sta) {
                                status = "IN BAG";
                            } else {
                                if (!inTrip){
                                    status = "NOT NEEDED";
                                }
                                else {
                                    status = "NOT IN BAG";
                                }
                            }
                        }
                    }
                    //orr
//                    Cursor s = tripDatabaseHelper.getStatus(name);

                    info.add("Timestamp: " + time);
                    info.add("Name: " + name);
                    info.add("ID: " + id);
                    info.add("Status: " + status);
                    ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, info);
                    mListView.setAdapter(adapter);
                }



            }
        }

    }



}
