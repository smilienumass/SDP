package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;



public class TripsActivity extends AppCompatActivity {
    private static final String TAG = "TripsActivity";

    private Button btnNewTrip, btnCurrTrip, btnSavedTrips;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trips_activity);

        btnNewTrip = (Button) findViewById(R.id.btnNewTrip);
        btnCurrTrip = (Button) findViewById(R.id.btnCurrentTrip);
        btnSavedTrips = (Button) findViewById(R.id.btnSavedTrip);



        btnNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TripsActivity.this, CreateNewTrip.class);
                startActivity(intent);
            }
        });

        btnCurrTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TripsActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });



    }
}
