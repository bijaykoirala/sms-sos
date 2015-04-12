package com.app.smssos;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Bijay Koirala on 4/11/15.
 */
public class SendAlert extends Activity {
    String surakChyaId;
    @InjectView(R.id.et_zone)EditText etZone;
    @InjectView(R.id.et_district)EditText etDistrict;
    @InjectView(R.id.et_area)EditText etArea;
    @InjectView(R.id.et_age)EditText etAge;
    @InjectView(R.id.et_message)EditText etMessage;
    @InjectView(R.id.tv_number_of_messages)TextView tvNumberOfMessages;

    String zone, district, area, age, message, numberOfMessages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_alert);
        ButterKnife.inject(this);

        etZone.setText("Bagmati");
        etDistrict.setText("Sindupalchowk");
        etAge.setText("12");
        etArea.setText("Kathmandu");
        etMessage.setText("Beware of Hari Bahadur offering dishwashing job in Delhi. Contact MaitiNepal");


        zone = etZone.getText().toString();
        district = etDistrict.getText().toString();
        area = etArea.getText().toString();
        age = etAge.getText().toString();
        message = etMessage.getText().toString();
        int r = (int) (Math.random() * (1000 - 10)) + 10;
        tvNumberOfMessages.setText("The message will be sent to "+r+" messages");



    }

    public void done(View v){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("zone", zone);
            jsonObject.put("district", district);
            jsonObject.put("area", area);
            jsonObject.put("age", age);
            jsonObject.put("message", etMessage.getText().toString());

            NetworkManager nm = new NetworkManager();
            String response = nm.postValuesAndGetResponse(jsonObject, API.SEND_ALERT);

            JSONObject responseObject = new JSONObject(response);
            String surakchyaCode = responseObject.getString("success");
            Toast.makeText(getApplicationContext(), surakchyaCode+" ", Toast.LENGTH_SHORT).show();
            finish();
        }
        catch (JSONException expt){
            Log.d("SendAlert", "message");

        }


    }
}
