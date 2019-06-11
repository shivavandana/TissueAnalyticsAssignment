package com.shiva.tissueanalyticsassignment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    //Global declaration of variables
    Button btnFirstSite, btnSecondSite;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar implementation
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tissue Analytics");
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Fetching button ids
        btnFirstSite= (Button)findViewById(R.id.btn_chama);
        btnSecondSite= (Button)findViewById(R.id.btn_pueblo);

        //Checking Internet connection
        verifyInternetConnection();

        //Implementation of site1 onClickListener
        btnFirstSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSecondActivity("site1");
            }
        });

        //Implementation of site2 onClickListener
        btnSecondSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSecondActivity("site2");
           }
        });
    }


    //Implementation of method that directs to the second Activity
    public void moveToSecondActivity(String site){
        //Intent to Second Activity
        intent= new Intent(this, DetailActivity.class);
        Bundle bundle=new Bundle();
        //Passing values to second activity
        bundle.putString("sitenumber",site);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Method to Verify Internet Connection
    public void verifyInternetConnection()
    {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.internet_error))
                    .setPositiveButton("OK", null).show();
        }
    }
}


