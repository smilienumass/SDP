package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;


public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave,btnDelete, btnStatus;
    private EditText editable_item;

    DatabaseHelper mDatabaseHelper;
    DatabaseHelper pDatabseHelper;

    private String selectedName;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnStatus = (Button) findViewById(R.id.btnStatus);
        editable_item = (EditText) findViewById(R.id.editable_item);
        mDatabaseHelper = new DatabaseHelper(this);
        pDatabseHelper = new DatabaseHelper(this);
        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");

        //set the text to show the current selected name
        editable_item.setText(selectedName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editable_item.getText().toString();
                if(!item.equals("")){
                    mDatabaseHelper.updateName(item,selectedID,selectedName);
                }else{
                    toastMessage("You must enter a name");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(selectedID,selectedName);
                editable_item.setText("");
                toastMessage("removed from database");
            }
        });

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(EditDataActivity.this, ItemsInfo.class);
                infoIntent.putExtra("id",selectedID);
                infoIntent.putExtra("name",selectedName);
                startActivity(infoIntent);


                ///Displaying status for all items
//                Cursor trip_item = mDatabaseHelper.getData();
//                Cursor logged_item = pDatabseHelper.getData();
//                HashMap<String, Boolean> status = new HashMap<>();
//                if(trip_item.getCount()>0){
//
//
//
//                    while(trip_item.moveToNext()){
//                        String name= trip_item.getString(trip_item.getColumnIndex("name"));
//                        status.put(name, false);
//
//                        while (logged_item.moveToNext()) {
//                            String temp_name = logged_item.getString(logged_item.getColumnIndex("name"));
//                            if(name.equals(temp_name)) {
//                                status.replace(name, status.get(name), true);
//                            }
//                        }
//
//                    }
//
//                }
//
//                for (Map.Entry<String, Boolean> e : status.entrySet()){
//                      System.out.println(e.getKey() + " " + e.getValue());
//                }
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


