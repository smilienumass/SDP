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

public class LogInfo extends AppCompatActivity{
    private static final String TAG = "LogInfo";
    DatabaseHelperLogPi lDatabaseHelper;

    private String selectedDetection;
    private String selectedTime;

    private ListView mListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        lDatabaseHelper = new DatabaseHelperLogPi(this);
        Intent receivedIntent = getIntent();
        selectedDetection = receivedIntent.getStringExtra("detection"); //NOTE: -1 is just the default value
        selectedTime = receivedIntent.getStringExtra("time");

        showLogInfo();

    }

    private void showLogInfo() {
        Cursor data = lDatabaseHelper.getData();

        if(data.getCount() >0) {
            while (data.moveToNext()) {

                String temp_time = data.getString(data.getColumnIndex("timestamp"));
                if(temp_time.equals(selectedTime)) {
                    System.out.println(selectedTime + "  " + temp_time);


                    ArrayList<String> info = new ArrayList<>();
                    String time = data.getString(data.getColumnIndex("timestamp"));
                    String det = data.getString(data.getColumnIndex("detection"));
                    String mag = data.getString(data.getColumnIndex("magnitude"));
                    String gyr = data.getString(data.getColumnIndex("gyroscope"));
                    String acc = data.getString(data.getColumnIndex("accelerometer"));
                    String temp = data.getString(data.getColumnIndex("temperature"));

                    info.add("Timestamp: " + time);
                    info.add("Detection: " + det);
                    info.add("Magnitude: " + mag);
                    info.add("Gyroscope: " + gyr);
                    info.add("Accelerometer: " + acc);
                    info.add("Temperature: " + temp);

                    ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, info);
                    mListView.setAdapter(adapter);
                }



            }
        }

    }
}
