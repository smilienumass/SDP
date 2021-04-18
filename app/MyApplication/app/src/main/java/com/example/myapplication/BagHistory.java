package com.example.myapplication;

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
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.UUID;

public class BagHistory extends AppCompatActivity {
    private static final String TAG = "BagHistory";
    DatabaseHelperReadHistory bagDatabaseHelper;
    DatabaseHelperRegisterMode regDatabaseHelper;
    DatabaseHelper curDatabaseHelper;
    private TableLayout tableLayout;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        bagDatabaseHelper = new DatabaseHelperReadHistory(this);
        curDatabaseHelper = new DatabaseHelper(this);
        makeTable();
        populateTable(bagDatabaseHelper);
    }


    private void makeTable(){
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);

//        lDatabaseHelper = new DatabaseHelperPi(this);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText={"NAME", "ID", "TIMESTAMP", "STATUS"};
        for(String c:headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);


    }


    private void populateTable(DatabaseHelperReadHistory database) {
        Log.d(TAG, "populateTable: Displaying data in the TableLayout.");

        //get the data and append to a list
        Cursor data;
        Cursor curdata =  curDatabaseHelper.getData();
        ArrayList registeredItems = new ArrayList();

//
//        if(curdata.getCount() >0) {
//            while (curdata.moveToNext()) {
//
//                // Read columns data
//                String id = curdata.getString(curdata.getColumnIndex("ID"));
//                String name = curdata.getString(curdata.getColumnIndex("name"));
//                registeredItems.add(name);
//                String time= "";
//                String item_status = "";
//
//                ///Check if in bag
//                boolean status = false;
//                data = database.getItemID(name);
//                if (data.getCount()>0){
//                    status = true;
////                    item_status = "IN BAG";
//                    Cursor temp = database.getItemTime(name);
//                    temp.moveToNext();
//                    time= temp.getString(0);
////                    time = temp.getString(0);
//                }
//
////                String item_status;
//                if (status) {
//                    item_status = "IN BAG";
//                } else {
//                    item_status = "NOT IN BAG";
//                }
//
//
//                // data rows
//                TableRow row = new TableRow(this);
//                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
//                        TableLayout.LayoutParams.WRAP_CONTENT));
////                String[] colText={id+"",name,time};
//                String[] colText = {name, id, time, item_status};
//
//                for (String text : colText) {
//                    TextView tv = new TextView(this);
//                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                            TableRow.LayoutParams.WRAP_CONTENT));
//                    tv.setGravity(Gravity.CENTER);
//                    tv.setTextSize(10);
//                    tv.setPadding(5, 5, 5, 5);
//                    tv.setText(text);
//                    row.addView(tv);
//                }
//                tableLayout.addView(row);
//
//            }
//        }
//

        // for extra items in the bag but not in the current list
        // reset data
        data = database.getData();
        if(data.getCount()>0){
            while (data.moveToNext()){
                String extra_id= data.getString(data.getColumnIndex("ID"));
//                    String name= curdata.getString(curdata.getColumnIndex("name"));

                String extra_name = data.getString(data.getColumnIndex("name"));
                String extra_time = data.getString(data.getColumnIndex("timestamp"));
               String extra_item_status = "NOT NEEDED";

//                if()

//                if (!registeredItems.contains(extra_name)){
//                            System.out.println(name + "  " + temp_name);
//                        time = data.getString(data.getColumnIndex("timestamp"));
                    // data rows
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
//                String[] colText={id+"",name,time};
                    String[] colText={extra_name,extra_id,extra_time, extra_item_status};

                    for(String text:colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(10);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }
                    tableLayout.addView(row);

            }
        }
    }
}
