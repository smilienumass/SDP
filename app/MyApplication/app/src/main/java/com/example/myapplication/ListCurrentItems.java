package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.TableLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ListCurrentItems extends AppCompatActivity {

    private static final String TAG = "ListCurrentItems";
    DatabaseHelperPi lDatabaseHelper;
    private ListView lListView;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //folder
        setContentView(R.layout.logged_items_layout);

//        lListView = (ListView) findViewById(R.id.listView);
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);

        lDatabaseHelper = new DatabaseHelperPi(this);


//        populateListView();

        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText={"ID","NAME", "TIMESTAMP"};
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

        populateTable();


    }

    private void populateTable() {
        Log.d(TAG, "populateTable: Displaying data in the TableLayout.");

        //get the data and append to a list
        Cursor data = lDatabaseHelper.getData();


        if(data.getCount() >0)
        {
            while (data.moveToNext()) {
                // Read columns data
                String id= data.getString(data.getColumnIndex("ID"));
                String name= data.getString(data.getColumnIndex("name"));
                String time= data.getString(data.getColumnIndex("timestamp"));
                // data rows
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
//                String[] colText={id+"",name,time};
                String[] colText={id,name, time};

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
//        db.setTransactionSuccessful();

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
