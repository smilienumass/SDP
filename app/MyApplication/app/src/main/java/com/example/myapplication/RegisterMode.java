package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothSocket;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.bluetooth.BluetoothDevice;

import org.json.JSONException;

public class RegisterMode extends AppCompatActivity{
    private static final String TAG = "RegisterMode";

    Button addRegister, btnDone;
    private EditText editText;
    private  TextView tagId;
    DatabaseHelperRegisterMode rDatabaseHelper;

    public final static String MESSAGE = null;
    public final static String EXTRA_ADDRESS = null;

    public BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    public String message;

    public class ConnectThread extends Thread {
        private ConnectThread(BluetoothDevice device, String msg, DatabaseHelperRegisterMode database, String itemName) throws IOException, JSONException {

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
                } catch (IOException closeException) {

                }
            }
            send(msg, database, itemName);
        }


        public void send(String msg, DatabaseHelperRegisterMode database, String itemName) throws IOException, JSONException {
            // msg = "read";
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
            receive(database, itemName);
        }

        public void sendVerify(String msg) throws IOException, JSONException {
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
        }
        public void receive(DatabaseHelperRegisterMode database, String itemName) throws IOException, JSONException {

            InputStream mmInputStream = mmSocket.getInputStream();

            StringWriter w = new StringWriter();
            byte[] buffer = new byte[65536];
            int bytes;
            try {
                bytes = mmInputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "Received: " + readMessage);
                String id;
                if(!readMessage.equals("No Items read from bag")){
                    id = database.addData(readMessage, itemName);
                    tagId.setText(id);
                    sendVerify("recReg");
                }
                else{
                    toastMessage("No item detected");
                }
//                boolean b = database.addData(readMessage, itemName);
//                message = readMessage;

                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Problems occurred!");
                return;
            }
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_mode);

        addRegister = (Button) findViewById(R.id.btnAddRegister);
        btnDone = (Button) findViewById(R.id.btnDone);
        editText = (EditText) findViewById(R.id.editRegisterText);
        tagId = (TextView) findViewById(R.id.tagid);
        rDatabaseHelper = new DatabaseHelperRegisterMode(this);

        ////////////////////////bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Intent intent = getIntent();
        final String address = intent.getStringExtra(CheckBag.EXTRA_ADDRESS);   ///change
        final BluetoothDevice device = BTAdapter.getRemoteDevice(address);
        /////

        addRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntryName = editText.getText().toString();
                if (editText.length() != 0) {
                    try {
                        new RegisterMode.ConnectThread(device,"register", rDatabaseHelper, newEntryName).start();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    editText.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(RegisterMode.this, MainActivity.class);
                startActivity(intent);
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
