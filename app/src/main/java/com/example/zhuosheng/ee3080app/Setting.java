package com.example.zhuosheng.ee3080app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Setting extends AppCompatActivity {
    ListView listViewSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // Get ListView object from xml
        listViewSet = (ListView) findViewById(R.id.LVSet);

        // Defined Array values to show in ListView
        String[] values = new String[]{"I.P Address ",
                "Feedback", "About"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listViewSet.setAdapter(adapter);

        // ListView Item Click Listener
        listViewSet.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        Intent newActivity0 = new Intent(Setting.this, ip_add.class);
                        startActivity(newActivity0);
                        break;
                    case 1:Intent newActivity1 = new Intent(Setting.this, feedback.class);
                        startActivity(newActivity1);
                        break;
                    case 2:
                        Intent newActivity2 = new Intent(Setting.this, about.class);
                        startActivity(newActivity2);
                        break;
                }
            }
        });
    }

}

