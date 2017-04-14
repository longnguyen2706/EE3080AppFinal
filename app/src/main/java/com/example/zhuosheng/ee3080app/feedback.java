package com.example.zhuosheng.ee3080app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Kaizen on 4/7/2017.
 */

public class feedback extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        Button btnClick = (Button) findViewById(R.id.BtnFBSend);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(getApplicationContext(),"Sent",Toast.LENGTH_LONG).show();
            }

        });
}
}
