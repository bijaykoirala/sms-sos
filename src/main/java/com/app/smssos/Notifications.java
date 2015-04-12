package com.app.smssos;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Bijay Koirala on 4/11/15.
 */
public class Notifications extends Activity {
    @InjectView(R.id.lv_notifications)ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        ButterKnife.inject(this);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Help Me : Nitu Shrestha",
                "Help Me : Sabita Tamang",
                "Help Me : Ajashree ",
                "Help Me : Rohini",
                "Help Me : Aishwarya"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

}
