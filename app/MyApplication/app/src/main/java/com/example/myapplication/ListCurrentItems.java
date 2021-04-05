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
import java.util.HashMap;
import java.util.UUID;






public class ListCurrentItems extends AppCompatActivity {

    private static final String TAG = "ListCurrentItems";
    DatabaseHelperPi lDatabaseHelper;
    DatabaseHelperPi bDatabaseHelper;
    private ListView lListView;
    private TableLayout tableLayout;





    //////////////// bluetooth //////////
    public BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
//    private static final String TAG = "BluetoothConnection";
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    public String message;
    HashMap<String, String> map;
    public class ConnectThread extends Thread {

        private ConnectThread(BluetoothDevice device, String msg, DatabaseHelperPi database) throws IOException, JSONException {
            /*if (mmSocket != null) {
                if(mmSocket.isConnected()) {
                    send();
                }
            }*/
            map = new HashMap<>();
            message = "";
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
                tmp = mmDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
            BTAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                Log.v(TAG, "Connection exception!");
                try {
                    mmSocket.close();
                    /*mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);
                    mmSocket.connect();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } */
                } catch (IOException closeException) {

                }
            }
            send(msg, database);
        }

        public void send(String msg, DatabaseHelperPi database) throws IOException, JSONException {
            // msg = "read";
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
            receive(database);
        }

        public void receive(DatabaseHelperPi database) throws IOException, JSONException {

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

            InputStream mmInputStream = mmSocket.getInputStream();

            StringWriter w = new StringWriter();
            // IOUtils.copy()
            //int len = mmInputStream.available();
            //len = len/8;
            byte[] buffer = new byte[65536];
            int bytes;

            try {
                bytes = mmInputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "Received: " + readMessage);
//                TextView itemsInBag = (TextView) findViewById(R.id.items);

                database.seeBag(map,readMessage);
                message = readMessage;

//                itemsInBag.setText("\nIncoming Data: \n" + readMessage + "");

                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Problems occurred!");
                return;
            }
        }

    }
    /////////////////////////////// bluetooth///////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //folder
        setContentView(R.layout.logged_items_layout);


        ////////////////////////bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Intent intent = getIntent();
        final String address = intent.getStringExtra(CheckBag.EXTRA_ADDRESS);   ///change
        Button viewBagButton = (Button) findViewById(R.id.btnReadBag);
        Button chegLogBtn = (Button) findViewById(R.id.btnReadLogs);
        bDatabaseHelper = new DatabaseHelperPi(this);
        lDatabaseHelper = new DatabaseHelperPi(this);

        viewBagButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final BluetoothDevice device = BTAdapter.getRemoteDevice(address);
                try {
                    new ListCurrentItems.ConnectThread(device,"read", bDatabaseHelper).start();
                    makeTable(bDatabaseHelper);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        chegLogBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final BluetoothDevice device = BTAdapter.getRemoteDevice(address);
                try {
                    new ListCurrentItems.ConnectThread(device,"checkLog",lDatabaseHelper).start();
                    makeTable(lDatabaseHelper);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        ////////////////////////// bluetooth /////////////////////////////////////



//        lListView = (ListView) findViewById(R.id.listView);
//        tableLayout=(TableLayout)findViewById(R.id.tableLayout);
//
//        lDatabaseHelper = new DatabaseHelperPi(this);


//        populateListView();

        // Add header row
//        TableRow rowHeader = new TableRow(this);
//        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
//        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//        String[] headerText={"ID","NAME", "TIMESTAMP"};
//        for(String c:headerText) {
//            TextView tv = new TextView(this);
//            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.WRAP_CONTENT));
//            tv.setGravity(Gravity.CENTER);
//            tv.setTextSize(18);
//            tv.setPadding(5, 5, 5, 5);
//            tv.setText(c);
//            rowHeader.addView(tv);
//        }
//        tableLayout.addView(rowHeader);
//
//        populateTable();
    }


    private void makeTable(DatabaseHelperPi database){
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);

//        lDatabaseHelper = new DatabaseHelperPi(this);
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

        populateTable(database);
    }

    private void populateTable(DatabaseHelperPi database) {
        Log.d(TAG, "populateTable: Displaying data in the TableLayout.");

        //get the data and append to a list
        Cursor data = database.getData();


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


///// bluetooth
    @Override
    protected void onStop() {
        super.onStop();
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
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
