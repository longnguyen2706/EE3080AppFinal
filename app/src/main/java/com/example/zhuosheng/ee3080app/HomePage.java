package com.example.zhuosheng.ee3080app;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton btncam = (ImageButton) findViewById(R.id.ImgBtnCam);
        btncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Enter Camera Mode",Toast.LENGTH_LONG).show();
                startActivity(new Intent(HomePage.this,Camera.class));

            }

        });

        ImageButton btngal = (ImageButton) findViewById(R.id.ImgBtnGallery);
        btngal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Enter Gallery Mode",Toast.LENGTH_LONG).show();
                startActivity(new Intent(HomePage.this,Gallery.class));
            }

        });

        ImageButton btnh = (ImageButton) findViewById(R.id.imgBtnHistory);
        btnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Enter history Mode",Toast.LENGTH_LONG).show();
                startActivity(new Intent(HomePage.this,History.class));
            }

        });

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(HomePage.this,Setting.class));
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
