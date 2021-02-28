package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ItemsInfo extends AppCompatActivity{

    private static final String TAG = "ItemsInfo";

    DatabaseHelper mDatabaseHelper;
    DatabaseHelperPi pDatabseHelper;

    private String selectedName;
    private int selectedID;

    private ListView mListView;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
//        mDatabaseHelper = new DatabaseHelper(this);
        pDatabseHelper = new DatabaseHelperPi(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");

        itemStatus();

    }

    private void itemStatus() {
        Cursor data = pDatabseHelper.getData();
        Boolean status = false;
        if(data.getCount()>0){
            while(data.moveToNext()){

                String temp_name = data.getString(data.getColumnIndex("name"));
                if(temp_name.equals(selectedName)){
                    System.out.println(selectedName + "  "+ temp_name);
                    status = true;
                    break;
                }
            }

            ArrayList<String> info = new ArrayList<>();
            info.add("STATUS:");
            if(status.equals(true)){
                info.add("IN BAG");
            }
            else{
                info.add("NOT IN BAG");
            }
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, info);
            mListView.setAdapter(adapter);
        }

    }

}
