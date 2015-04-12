package com.app.smssos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Bijay Koirala on 4/11/15.
 */
public class SurakchyaCode extends Activity {
    String surakChyaId;
    @InjectView(R.id.tv_surakchya_code)TextView tvSurakChyaCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surakchya_id);
        ButterKnife.inject(this);


        surakChyaId = getIntent().getStringExtra("surakchya_code");
        tvSurakChyaCode.setText(surakChyaId);


    }

    public void done(View v){
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }
}
