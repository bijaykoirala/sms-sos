package com.app.smssos;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by Bijay Koirala on 2/21/15.
 */
public class NetworkManager {
    private static final String TAG = "SMS-SOS";
    JSONObject postObj;
    String url;

    public void postData(JSONObject postObj, String url) {
        this.postObj = postObj;
        this.url = url;
    }

    public String postValuesAndGetResponse(JSONObject jObject, String urlPost) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(urlPost);
        httppost.setHeader("Content-Type", "application/json");
        try {

            Log.d("MSG", jObject.toString());

//            Log.d(TAG, "Posted Data: " + jObject);
//            Log.d(TAG, "Posted URL: " + urlPost);
            httppost.setEntity(new ByteArrayEntity(jObject.toString().getBytes("UTF8")));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            return convertStreamToString(is);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "eof";
    }

    // This method returns the string for given inputstream
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("NetworkManager", sb.toString() + " response");
        return sb.toString();
    }

    public String getJSONFeedAsString(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        Log.d(TAG, URL);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {

            }
        } catch (Exception e) {
            Log.d("GettingJSON", e.getLocalizedMessage());
            return "";
        }

        return stringBuilder.toString();

    }

    // filename must be client generated id
    // url myst be sent
    // content type = image/jpg, audio/mpeg, video/mp4, text/vcard, application/octet-stream

    // binary data content of file

    @SuppressLint("NewApi") // supressed
    public int uploadFile(String sourceFileUri, String uploadFilePath) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //final String uploadFilePath = "/mnt/sdcard/";
        final String uploadFileName = "image.jpg";
        int serverResponseCode = 0;

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024; // 10 mb buffer size

        File sourceFile = new File(uploadFilePath);

        if (!sourceFile.isFile()) {
            Log.d("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);
            return 0; // server status 0 meaning file not existing
        } else // if file exists
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                // URL url = new URL(upLoadServerUri);
                URL url = convertToUrl(API.REGISTER_URL);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                //conn.setRequestProperty("CA-Auth-Token", token);
                //conn.setRequestProperty("CA-Auth-Id", id);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                // put the file name here
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"stupid_file_name\"" + lineEnd);
                dos.writeBytes("Content-Type: image/jpeg" + lineEnd);


                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                //

                Log.d("NM: Uploads", twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                InputStream inputStream = (InputStream) conn.getContent();

                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder jsonString = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    jsonString.append(line);
                }
                Log.i("uploadFile", "HTTP Response is : "
                        + jsonString + ": " + serverResponseCode);

                //TODO deal with this response
                if (serverResponseCode == 200) {
                    //try {
                    //JSONObject jsonObject
                    //}
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                //dialog.dismiss();
                ex.printStackTrace();
                Log.d("UploadingFile", "MalformedURLException");

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                //dialog.dismiss();
                e.printStackTrace();
                Log.d("UploadingFile", "Got Exception : see logcat ");
                Log.e("Upload file to server", "Exception : "
                        + e.getMessage(), e);
            }
            //dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    private URL convertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
