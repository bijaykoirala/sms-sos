package com.app.smssos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class Dashboard extends ActionBarActivity {
    @InjectView(R.id.ll_add)LinearLayout llAdd;
    @InjectView(R.id.ll_look_up)LinearLayout llLookUp;
    @InjectView(R.id.ll_send_alert)LinearLayout llSendAlert;
    @InjectView(R.id.ll_notifications)LinearLayout llNotifications;
    @InjectView(R.id.ll_settings)LinearLayout llSettings;
    @InjectView(R.id.ll_dashboard)LinearLayout llDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.ll_add)
    public void add(View v){
        startActivity(new Intent(getApplicationContext(), StartingActivity.class));
    }

    @OnClick(R.id.ll_look_up)
    public void lookUp(View v){
        showToast("Under Construction");
    }

    @OnClick(R.id.ll_send_alert)
    public void sendAlert(View v){
        startActivity(new Intent(this, SendAlert.class));
    }

    @OnClick(R.id.ll_notifications)
    public void notifications(View v){

        startActivity(new Intent(getApplicationContext(), Notifications.class));
    }

    @OnClick(R.id.ll_settings)
    public void settings(View v){
        showToast("Under Construction");
    }

    @OnClick(R.id.ll_dashboard)
    public void dashboard(View v){
        showToast("Under Construction");
    }

    public void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();;

    }

}
