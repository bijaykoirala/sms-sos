package com.app.smssos;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class StartingActivity extends ActionBarActivity {
    @InjectView(R.id.et_name)EditText etName;
    @InjectView(R.id.et_zone)EditText etZone;
    @InjectView(R.id.et_district)EditText etDistrict;
    @InjectView(R.id.et_area)EditText etArea;
    @InjectView(R.id.et_phone)EditText etPhone;
    @InjectView(R.id.et_emergency_contact)EditText etemergencyContact;
    @InjectView(R.id.et_ingo)EditText etIngo;
    @InjectView(R.id.btn_get_picture)Button btnGetPicture;
    @InjectView(R.id.et_tole)EditText etTole;
    @InjectView(R.id.et_ward_number)EditText etWardNumber;
    @InjectView(R.id.et_sex)EditText etSex;
    @InjectView(R.id.datePicker)DatePicker datePicker;
    @InjectView(R.id.iv_photo)ImageView ivPicture;

    ContentValues values;
    private Uri imageUri = null;
    public static final int REQUEST_CAMERA = 1;

    String name="", zone="", district="", area="", emergencyContact="", ingo = "";
    String phone="", sex="", wardNumber="", tole="", date="";
    private static final int CAMERA_REQUEST = 985;
    boolean clickedImage;
    Bitmap bitmapImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        ButterKnife.inject(this);
        clickedImage = false;
        etName.setText("Ritu Shrestha");
        etZone.setText("Bagmati");
        etDistrict.setText("Kathmandu");
        etArea.setText("Kathmandu");
        etemergencyContact.setText("Bhagwan Shrestha");
        etIngo.setText("1");
        etPhone.setText("9779812398123");
        etWardNumber.setText("12");
        etSex.setText("M");
        etTole.setText("Random-tole");

    }

    @OnClick(R.id.btn_get_picture)
    public void getPicture(View v){
        clickedImage = true;
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @OnClick(R.id.btnNext)
    public void next(View view) {
        name = etName.getText().toString();
        zone = etZone.getText().toString();
        district = etDistrict.getText().toString();
        area = etArea.getText().toString();
        emergencyContact = etemergencyContact.getText().toString();
        ingo = etIngo.getText().toString();
        phone = etPhone.getText().toString();
        wardNumber = etWardNumber.getText().toString();
        sex = etSex.getText().toString();
        tole = etTole.getText().toString();
        date = datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth();
        JSONObject jObject = new JSONObject();

        try {
            jObject.put("name", name);
            jObject.put("zone", zone);
            jObject.put("district", district);
            jObject.put("area", area);
            jObject.put("emergency_contact", emergencyContact);
            jObject.put("ngo_id", ingo);
            jObject.put("sex", sex);
            jObject.put("ward_no", wardNumber);
            jObject.put("dob", date);
            jObject.put("tole", tole);
            jObject.put("user_phone", phone);


            if (clickedImage && bitmapImage != null)
                jObject.put("image", convertBitmapToBase64String(bitmapImage));

        }
        catch (JSONException jException){
            Log.d("SMS", jException.toString());
        }
        sendData(jObject);

     }

    private void sendData(JSONObject jsonObject) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        NetworkManager nm = new NetworkManager();
        String response = nm.postValuesAndGetResponse(jsonObject, API.REGISTER_URL);

        try {
            JSONObject responseObject = new JSONObject(response);
            String surakchyaCode = responseObject.getString("secret_code");
            Intent intent = new Intent(getApplicationContext(), SurakchyaCode.class);
            intent.putExtra("surakchya_code", surakchyaCode);
            startActivity(intent);
        }
        catch (JSONException ex){
            Toast.makeText(getApplicationContext(), "error parsing response", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmapImage = (Bitmap) data.getExtras().get("data");
            ivPicture.setImageBitmap(bitmapImage);
        }
    }

    public String convertBitmapToBase64String(Bitmap bitmap) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();
        String temp = null;
        try {
            System.gc();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            try {
//                String imageurl = getRealPathFromURI(imageUri);
//                new NetworkManager().uploadFile(null, imageurl);
//                //finish();
//            } catch (Exception e) {
//                Log.d("error", e.toString());
//            }
//
//            // Bundle extras = data.getExtras();
//            // bmp = (Bitmap) extras.get("data");
//
//        }
//
//    }
//
//    public String getRealPathFromURI(Uri contentUri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
}
