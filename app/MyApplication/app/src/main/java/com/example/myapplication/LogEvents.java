package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.TableLayout;
import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LogEvents extends AppCompatActivity {
    private static final String TAG = "LogEvents";

    DatabaseHelperLogPi lDatabaseHelper;
    private ListView mListView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_events);

        lDatabaseHelper = new DatabaseHelperLogPi(this);
        mListView = (ListView) findViewById(R.id.listViewLog);

        populateListView();
    }


    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = lDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();

        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            String info = data.getString(data.getColumnIndex("timestamp"))+ " " + data.getString(data.getColumnIndex("detection"));
//            listData.add(data.getString(data.getColumnIndex("timestamp")));
            listData.add(info);
        }
        ArrayList<String> listDataReverse = new ArrayList<>();
        for (int j = listData.size() - 1; j >= 0; j--) {
            listDataReverse.add(listData.get(j));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDataReverse);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                String[] temp = name.split(" ");
                String time = temp[0];
                String detect = temp[1];

                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = lDatabaseHelper.getItemID(time); //get the id associated with that name
                String itemDetection = " ";
                while(data.moveToNext()) {
//                    itemDetection = data.getString(1);

                }
                itemDetection = detect;
                if(!itemDetection.equals(" ")){
                    Log.d(TAG, "onItemClick: The ID is: " + itemDetection);
                    Intent editScreenIntent = new Intent(LogEvents.this, LogInfo.class);
                    editScreenIntent.putExtra("detection",itemDetection);
                    editScreenIntent.putExtra("time",time);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No Detection associated with that time");
                }
            }
        });
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
