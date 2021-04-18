package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //SQLite
    DatabaseHelper mDatabaseHelper;
    DatabaseHelperPi bDatabaseHelper;
    DatabaseHelperLogPi lDatabaseHelper;
    DatabaseHelperRegisterMode rDatabaseHelper;
    private Button btnTrips, btnCheckBag, btnRegisterMode, btnClear;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTrips = (Button) findViewById(R.id.btnTrips);
        btnCheckBag = (Button) findViewById(R.id.btnCheckBag);
        btnRegisterMode = (Button) findViewById(R.id.btnRegisterMode);
        btnClear = (Button) findViewById(R.id.btnClear);
        mDatabaseHelper = new DatabaseHelper(this);
        bDatabaseHelper = new DatabaseHelperPi(this);
        lDatabaseHelper = new DatabaseHelperLogPi(this);
        rDatabaseHelper = new DatabaseHelperRegisterMode(this);


        //
//            map = new HashMap<>();
//            map.put( "E2004078410B02391140A53F","toothbrush");
//            map.put("E2004078410B01901140A4D7", "laptop");
//            map.put("E2004078410B02151140A50F", "shoes");
//            map.put("E2004078410B02561140A558", "toothpaste");
//            map.put("E2004078410B01921140A4D8", "shirt");
//            map.put("E2004078410B02161140A508", "pants");
//            map.put("E2004078410B02321140A528", "charger");
//            map.put("E2004078410B02141140A507", "jacket");
//            map.put("E2004078410B02411140A540", "sweater");
//            map.put("30342B1B4834739059BFEB5B", "ipad");

//
//

        btnCheckBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckBag.class);
                startActivity(intent);
            }
        });



        btnTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TripsActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterConnect.class);
                startActivity(intent);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteTable();
                lDatabaseHelper.deleteTable();
                rDatabaseHelper.deleteTable();
                bDatabaseHelper.deleteTable();
                Log.d(TAG, "All clear" );
                toastMessage("App all clear");
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
