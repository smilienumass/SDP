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
    DatabaseHelperReadHistory hDatabaseHelper;
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
        hDatabaseHelper = new DatabaseHelperReadHistory(this);


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
                hDatabaseHelper.deleteTable();
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
