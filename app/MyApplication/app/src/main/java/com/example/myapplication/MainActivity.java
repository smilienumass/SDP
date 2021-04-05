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



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //SQLite
    DatabaseHelper mDatabaseHelper;
    DatabaseHelperPi lDatabaseHelper;
    private Button btnAdd, btnViewData, btnViewBag, btnTrips, btnCheckBag;
    private EditText editText;
    HashMap<String, String> map;
     //   SQLite

    // Bluetooth
//    Button enablebt,disablebt,scanbt;
//    BluetoothSocket btSocket = null;
//    private BluetoothAdapter BTAdapter;
//    private Set<BluetoothDevice>pairedDevices;
//    ListView lv;
//    public final static String EXTRA_ADDRESS = null;
//    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //// Bluetooth

    // Write a message to the database
//    FirebaseDatabase da = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = da.getReference("message");
//    DatabaseReference myChildRef  = myRef.child("message");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bluetooth
//        enablebt=(Button)findViewById(R.id.button_enablebt);
//        disablebt=(Button)findViewById(R.id.button_disablebt);
//        scanbt=(Button)findViewById(R.id.button_scanbt);  /// list devices
//
//        BTAdapter = BluetoothAdapter.getDefaultAdapter();
//        lv = (ListView)findViewById(R.id.listViewB);
//        if (BTAdapter.isEnabled()){
//            scanbt.setVisibility(View.VISIBLE);
//        }
        // bluetooth


//        DatabaseReference postsRef = myRef.child("laptop");
//        postsRef.setValue("123456");
//        myRef.child("tooth").setValue("3455");
////        postsRef.setValue("123456");
//        myRef.setValue("Hello there!!!");
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        btnViewBag = (Button) findViewById(R.id.btnViewBag);
        btnTrips = (Button) findViewById(R.id.btnTrips);
        btnCheckBag = (Button) findViewById(R.id.btnCheckBag);
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
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });


        btnCheckBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckBag.class);
                startActivity(intent);
            }
        });


        //seeing your bag
//        btnViewBag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    SeeBag();
//                }
//                catch(JSONException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(MainActivity.this, ListCurrentItems.class);
//                startActivity(intent);
//            }
//        });


        btnTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TripsActivity.class);
                startActivity(intent);
            }
        });

    }




    //////////////////////////////////////////Bluetooth /////////////////////////////////////////////
//    public void on(View v){
//        if (!BTAdapter.isEnabled()) {
//            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(turnOn, 0);
//            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_SHORT).show();
//        }
//        scanbt.setVisibility(View.VISIBLE);
//        lv.setVisibility(View.VISIBLE);
//    }
//
//    public void off(View v){
//        BTAdapter.disable();
//        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_SHORT).show();
//        scanbt.setVisibility(View.INVISIBLE);
//        lv.setVisibility(View.GONE);
//    }
//
//    public void deviceList(View v){
//        ArrayList deviceList = new ArrayList();
//        pairedDevices = BTAdapter.getBondedDevices();
//
//        if (pairedDevices.size() < 1) {
//            Toast.makeText(getApplicationContext(), "No paired devices found", Toast.LENGTH_SHORT).show();
//        } else {
//            for (BluetoothDevice bt : pairedDevices) deviceList.add(bt.getName() + " " + bt.getAddress());
//            Toast.makeText(getApplicationContext(), "Showing paired devices", Toast.LENGTH_SHORT).show();
//            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList);
//            lv.setAdapter(adapter);
//            lv.setOnItemClickListener(myListClickListener);
//        }
//    }
//    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            String info = ((TextView) view).getText().toString();
//            String address = info.substring(info.length() - 17);
//            Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, BluetoothConnection.class);
//            intent.putExtra(EXTRA_ADDRESS, address);
//            startActivity(intent);
//        }
//    };
    ///////////////////////////Bluetooth////////




    // to add trip items
    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry, map);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }



////    //Need a check bag button??
    public void SeeBag() throws JSONException {
//        lDatabaseHelper.seeBag(map);
        return;

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
