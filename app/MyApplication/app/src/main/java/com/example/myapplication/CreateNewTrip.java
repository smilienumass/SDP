package com.example.myapplication;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    DatabaseHelper mDatabaseHelper;
    DatabaseHelperRegisterMode rDatabaseHelper;
    ListView listViewData;
    ArrayAdapter<String> adapter;
//    String[] arrayRegistered;
    ArrayList arrayRegistered;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mDatabaseHelper.deleteTable();
        int id = item.getItemId();
        if(id == R.id.item_done){
            String itemSelected = "Selected items: \n";
            for(int i=0; i<listViewData.getCount();i++){
                if(listViewData.isItemChecked(i)){
                    itemSelected+= listViewData.getItemAtPosition(i)+"\n";
                    String s = (String) listViewData.getItemAtPosition(i);
                    System.out.println(s);
                    AddData(( String)listViewData.getItemAtPosition(i));
                }
            }

            Toast.makeText(this,itemSelected,Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(CreateNewTrip.this, MainActivity.class);
//            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_trip_menu,menu);
        return true;
        // return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip);

        mDatabaseHelper = new DatabaseHelper(this);
        rDatabaseHelper = new DatabaseHelperRegisterMode(this);
        arrayRegistered = new ArrayList();
//        Cursor data = mDatabaseHelper.getData();
//        if(data.getCount()>0){
//
//        }

        Cursor register_data = rDatabaseHelper.getData();
        if(register_data.getCount()>0){
            while (register_data.moveToNext()){
                arrayRegistered.add(register_data.getString(register_data.getColumnIndex("name")));
            }
        }

        listViewData = findViewById(R.id.listView_data);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,arrayRegistered);
        listViewData.setAdapter(adapter);

//        map = new HashMap<>();
//        map.put( "E2004078410B02391140A53F","toothbrush");
//        map.put("E2004078410B01901140A4D7", "laptop");
//        map.put("E2004078410B02151140A50F", "shoes");
//        map.put("E2004078410B02561140A558", "toothpaste");
//        map.put("E2004078410B01921140A4D8", "shirt");
//        map.put("E2004078410B02161140A508", "pants");
//        map.put("E2004078410B02321140A528", "charger");
//        map.put("E2004078410B02141140A507", "jacket");
//        map.put("E2004078410B02411140A540", "sweater");
//

//        tripName = editTripName.getText().toString();
//        Intent editScreenIntent = new Intent(CreateNewTrip.this, DatabaseHelper.class);
//                    editScreenIntent.putExtra("tripname",tripName);
//        startActivity(editScreenIntent);


        //Adding item to the trip
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String newEntry = editText.getText().toString();
//                if (editText.length() != 0) {
//                    AddData(newEntry);
//                    editText.setText("");
//                } else {
//                    toastMessage("You must put something in the text field!");
//                }
//
//            }
//        });



        //seeing your list
//        btnViewData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CreateNewTrip.this, ListDataActivity.class);
//                startActivity(intent);
//            }
//        });
    }

//
//
////     to add trip items
    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry, rDatabaseHelper);

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
