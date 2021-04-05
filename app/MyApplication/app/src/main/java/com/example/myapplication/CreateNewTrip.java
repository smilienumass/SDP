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


public class CreateNewTrip extends AppCompatActivity {

    private static final String TAG = "CreateNewTrip";
    public String tripName = " ";

    DatabaseHelper mDatabaseHelper;
    DatabaseHelperPi lDatabaseHelper;
    private Button btnAdd, btnViewData, btnDelete;
    private EditText editText;
    private EditText editTripName;
    HashMap<String, String> map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip);

        editTripName = (EditText) findViewById(R.id.editName);
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        mDatabaseHelper = new DatabaseHelper(this);
        lDatabaseHelper = new DatabaseHelperPi(this);



        map = new HashMap<>();
        map.put( "E2004078410B02391140A53F","toothbrush");
        map.put("E2004078410B01901140A4D7", "laptop");
        map.put("E2004078410B02151140A50F", "shoes");
        map.put("E2004078410B02561140A558", "toothpaste");
        map.put("E2004078410B01921140A4D8", "shirt");
        map.put("E2004078410B02161140A508", "pants");
        map.put("E2004078410B02321140A528", "charger");
        map.put("E2004078410B02141140A507", "jacket");
        map.put("E2004078410B02411140A540", "sweater");


        tripName = editTripName.getText().toString();
//        Intent editScreenIntent = new Intent(CreateNewTrip.this, DatabaseHelper.class);
//                    editScreenIntent.putExtra("tripname",tripName);
//        startActivity(editScreenIntent);


        //Adding item to the trip
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if (editText.length() != 0) {
                    AddData(newEntry);
                    editText.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });



        //seeing your list
        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewTrip.this, ListDataActivity.class);
                startActivity(intent);
            }
        });
    }



    // to add trip items
    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry, map);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


}
