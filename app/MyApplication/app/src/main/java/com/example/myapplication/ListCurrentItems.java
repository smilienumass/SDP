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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.HashMap;


public class ListCurrentItems extends AppCompatActivity {

    private static final String TAG = "ListCurrentItems";
    DatabaseHelperLogPi lDatabaseHelper;
    DatabaseHelperReadHistory bagDatabaseHelper;
    DatabaseHelper curDatabaseHelper;
    DatabaseHelperRegisterMode regDatabaseHelper;
    DatabaseHelperPi bDatabaseHelper;

    FirebaseDatabase firedatabase = FirebaseDatabase.getInstance();
    DatabaseReference bag_history_ref = firedatabase.getReference("bag_history_table");

    private ListView lListView;
    private TableLayout tableLayout;
    private ListView dListView;
    public final static String MESSAGE = null;
    public final static String EXTRA_ADDRESS = null;
    public ArrayList<String> list;

    public BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    public String checkRecLog, checkRecRead, previous;


    public class ConnectThread extends Thread {
        private ConnectThread(BluetoothDevice device, String msg, DatabaseHelperPi database) throws IOException, JSONException {
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
                } catch (IOException closeException) {

                }
            }
            send(msg, database);
        }


        public void send(String msg, DatabaseHelperPi database) throws IOException, JSONException {
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
            receive(database);
        }

//        public void sendVerify(String msg) throws IOException, JSONException {
//            OutputStream mmOutputStream = mmSocket.getOutputStream();
//            mmOutputStream.write(msg.getBytes());
//        }

        public void receive(DatabaseHelperPi database) throws IOException, JSONException {
            InputStream mmInputStream = mmSocket.getInputStream();

            StringWriter w = new StringWriter();
            byte[] buffer = new byte[65536];
            int bytes;

            try {
                bytes = mmInputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "Received: " + readMessage);

                if(!readMessage.equals("No Items read from bag")){
                    database.deleteTable();
                    database.seeBag(regDatabaseHelper, readMessage);
                }
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Problems occurred!");
                return;
            }
        }

    }


    public class ConnectThreadHistory extends Thread {
        private ConnectThreadHistory(BluetoothDevice device, String msg, DatabaseHelperReadHistory database) throws IOException, JSONException {
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
                } catch (IOException closeException) {

                }
            }
            send(msg, database);
        }

        public void send(String msg, DatabaseHelperReadHistory database) throws IOException, JSONException {
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
            receive(database);
        }

        public void sendVerify(String msg) throws IOException, JSONException {
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
        }

        public void receive(DatabaseHelperReadHistory database) throws IOException, JSONException {
            InputStream mmInputStream = mmSocket.getInputStream();

            StringWriter w = new StringWriter();
            byte[] buffer = new byte[65536];
            int bytes;

            try {
                bytes = mmInputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "Received: " + readMessage);

                checkRecRead = readMessage;
                if(!readMessage.equals("No Items read from bag")) {
//                    list.add(readMessage);
//                    TextView itemsInBag = (TextView) findViewById(R.id.items);
//                    itemsInBag.setText("\nIncoming Data: \n" + readMessage + "");
                    database.addToDB(regDatabaseHelper, readMessage);
                    sendVerify("recTags");
                }
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Problems occurred!");
                return;
            }
        }
    }


    public class ConnectThreadLogs extends Thread {

        private ConnectThreadLogs(BluetoothDevice device, String msg, DatabaseHelperLogPi database) throws IOException, JSONException {
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
                } catch (IOException closeException) {
                }
            }
            send(msg, database);
        }

        public void send(String msg, DatabaseHelperLogPi database) throws IOException, JSONException {
            // msg = "read";
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
            receive(database);
        }
        public void sendVerify(String msg) throws IOException, JSONException {
            // msg = "read";
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
        }

        public void receive(DatabaseHelperLogPi database) throws IOException, JSONException {

            InputStream mmInputStream = mmSocket.getInputStream();

            StringWriter w = new StringWriter();
            byte[] buffer = new byte[65536];
//            byte[] buffer = new byte[2^32];
            int bytes;

            try {
                bytes = mmInputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "Received: " + readMessage);
                checkRecLog = readMessage;
                if(!readMessage.equals("No events")) {
                    database.seeLog(readMessage);
                    sendVerify("recLogs");
                }
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
        Button checkLogBtn = (Button) findViewById(R.id.btnReadLogs);
        Button btnBagHistory =  (Button) findViewById(R.id.btnBagHistory);

        bagDatabaseHelper = new DatabaseHelperReadHistory(this);
        lDatabaseHelper = new DatabaseHelperLogPi(this);
        curDatabaseHelper = new DatabaseHelper(this);
        regDatabaseHelper = new DatabaseHelperRegisterMode(this);
        bDatabaseHelper  = new DatabaseHelperPi(this);
        checkRecLog = "start";
        checkRecRead = "start";
//        makeTable();
        viewBagButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final BluetoothDevice device = BTAdapter.getRemoteDevice(address);
                try {
                    new ListCurrentItems.ConnectThread(device, "read", bDatabaseHelper).start();

                    // add / show last entry
                    populateTable(bDatabaseHelper);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        checkLogBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final BluetoothDevice device = BTAdapter.getRemoteDevice(address);
                try {
                    while(!checkRecLog.equals("No events")) {
                        new ListCurrentItems.ConnectThreadLogs(device, "checkLog", lDatabaseHelper).start();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ListCurrentItems.this, LogEvents.class);
                startActivity(intent);
            }
        });

        btnBagHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final BluetoothDevice device = BTAdapter.getRemoteDevice(address);
                try {
                    while(!checkRecRead.equals("No Items read from bag")) {
                        new ListCurrentItems.ConnectThreadHistory(device, "readHistory", bagDatabaseHelper).start();
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ListCurrentItems.this, BagHistory.class);
                startActivity(intent);
//                Intent intent1  = new Intent(ListCurrentItems.this, BagHistory.class);
//                startActivity(intent1);
            }

        });

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        ////////////////////////// bluetooth /////////////////////////////////////
    }


    private void makeTable(){
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);

//        lDatabaseHelper = new DatabaseHelperPi(this);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//        String[] headerText={"NAME", "ID", "TIMESTAMP", "STATUS"};
        String[] headerText={"NAME", "TIMESTAMP", "STATUS"};

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

    private void populateTable(DatabaseHelperPi database) {
        Log.d(TAG, "populateTable: Displaying data in the TableLayout.");

        //get the data and append to a list
        Cursor data;
        Cursor curdata =  curDatabaseHelper.getData();
        ArrayList registeredItems = new ArrayList();
        makeTable();
        tableLayout.removeAllViews();
        makeTable();

        if(curdata.getCount() >0) {
            //  IF data bringing on trip
            while (curdata.moveToNext()) {
                // Read columns data
                String id = curdata.getString(curdata.getColumnIndex("ID"));
                String name = curdata.getString(curdata.getColumnIndex("name"));
                registeredItems.add(name);
                String time= "";
                String item_status = "";

                ///Check if in bag
                boolean status = false;
                data = database.getItemID(name);
                if (data.getCount()>0){
                    status = true;
//                    item_status = "IN BAG";
                    Cursor t = database.getItemTime(name);
                    t.moveToNext();
                    time= t.getString(0);
//                    time = temp.getString(0);
                }

                if (status) {
                    item_status = "IN BAG";
                } else {
                    item_status = "NOT IN BAG";
                }
//                String oldStatus = curdata.getString(curdata.getColumnIndex("status"));
//                curDatabaseHelper.updateStatus(item_status,id, oldStatus);

//                userRef.child().update({'dateOfBirth': moment(value.dateOfBirth).toDate().getTime()})
                DatabaseReference mPostReference= bag_history_ref;
                HashMap<String, String> updateStatus = new HashMap<>();

//                bag_history_ref.child().updateChildren({'dateOfBirth': moment(value.dateOfBirth).toDate().getTime()})

                // data rows
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
//                String[] colText={id+"",name,time};
//                String[] colText = {name, id, time, item_status};
                String[] colText = {name, time, item_status};


                for (String text : colText) {
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


                if (!registeredItems.contains(extra_name)){
//                            System.out.println(name + "  " + temp_name);
//                        time = data.getString(data.getColumnIndex("timestamp"));
                    // data rows
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
//                String[] colText={id+"",name,time};
//                    String[] colText={extra_name,extra_id,extra_time, extra_item_status};
                    String[] colText={extra_name,extra_time, extra_item_status};



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
