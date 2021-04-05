package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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



public class CheckBag extends AppCompatActivity{
    private static final String TAG = "CheckBag";
    //

    // Bluetooth
    Button enablebt1,disablebt1,scanbt1;
    BluetoothSocket btSocket = null;
    private BluetoothAdapter BTAdapter;
    private Set<BluetoothDevice>pairedDevices;
    ListView lv;
    public final static String EXTRA_ADDRESS = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //// Bluetooth


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_bag);

        //Bluetooth


        enablebt1=(Button)findViewById(R.id.button_enablebt2);
        disablebt1=(Button)findViewById(R.id.button_disablebt2);
        scanbt1=(Button)findViewById(R.id.button_scanbt2);  /// list devices

        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView)findViewById(R.id.listViewDevice);
        if (BTAdapter.isEnabled()){
            scanbt1.setVisibility(View.VISIBLE);
        }
        // bluetooth




    }

    //////////////////////////////////////////Bluetooth /////////////////////////////////////////////
    public void on(View v){
        if (!BTAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_SHORT).show();
        }
        scanbt1.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);
    }

    public void off(View v){
        BTAdapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_SHORT).show();
        scanbt1.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.GONE);
    }

    public void deviceList(View v){
        ArrayList deviceList = new ArrayList();
        pairedDevices = BTAdapter.getBondedDevices();

        if (pairedDevices.size() < 1) {
            Toast.makeText(getApplicationContext(), "No paired devices found", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice bt : pairedDevices) deviceList.add(bt.getName() + " " + bt.getAddress());
            Toast.makeText(getApplicationContext(), "Showing paired devices", Toast.LENGTH_SHORT).show();
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(myListClickListener);
        }
    }
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CheckBag.this, ListCurrentItems.class);
            intent.putExtra(EXTRA_ADDRESS, address);
            startActivity(intent);
        }
    };
    ///////////////////////////Bluetooth////////
}
