package uk.ac.ucl.cege.cegeg077.zceshel.mapsexample;

// Code in this App is partially adapted from this source: http://www.xyzws.com/javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
// Accessed 27th December 2015 by Claire Ellul
// Adapted from Claire Ellul by Haitham El Mengad 2/05/2017
// This activity prompts the user to enter his/her name and
// to select an answer choice for the question (view lay-out)
// It then sends the information to the UCL server: http://node01.geospatial.ucl.ac.uk/teaching/user42/process_form.php
// The name is used instead of phone ID to track the user input

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SendCheckboxToServer extends AppCompatActivity implements  View.OnClickListener{
   
    //Get correct answer from GPSSensorActivity
    
    Intent intent = getIntent();
    String correct_ans = intent.getStringExtra("correct_ans");
    
    private TextView tv;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_checkbox_to_server);
        
        // define the submission button
        ((Button)findViewById(R.id.button1)).setOnClickListener(this);
        tv = (TextView) findViewById(R.id.webResponse);
    }
    
    // The method "submitDataPost" is launched when the user clicks on the
    // button (see layout file)
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button1:
                submitDataPost();
                break;
        }
    }
    
    // The following method converts the user input into an exploitable array
    // of NameValuePair objects
    
    private void submitDataPost() {
        //NameValuePair nameValuePairs[] = getFormDetails();
        
        String data="name=Haithamsurname=ElMengad";
        
        // create an asynchronous operation that will take these values
        // and send them to the server
        SendHttpRequestTask sfd = new SendHttpRequestTask();
        try {
            // firstname
            EditText et = (EditText)findViewById(R.id.firstname);
            String theFirstName = (String) et.getText().toString();
            // surname
            EditText et2 = (EditText)findViewById(R.id.surname);
            String theSurname = (String) et2.getText().toString();
            
            
            String urlParameters =
            "firstname=" + URLEncoder.encode(theFirstName, "UTF-8") +
            "&surname=" + URLEncoder.encode(theSurname, "UTF-8");
            // also add the checkboxes - are they checked or not
            urlParameters += "&"+getCheckBoxes();
            Log.i("result",urlParameters);
            
            sfd.execute(urlParameters);
        }
        catch             (UnsupportedEncodingException e){}
        
    }
    
    // Set the checkboxes titles
    // Set the value of each checkbox being checked in a string "result"
    private String getCheckBoxes() {
        
        String result;
        // Checkbox 1 for answer choice A
        CheckBox ACheckBox = (CheckBox) findViewById(R.id.A);
        result="A" + "="+ACheckBox.isChecked();
        
        // Checkbox 1 for answer choice B
        // Note that more than one checkbox can be checked and change the value of "result"
        // A sentence in the layout file asks the user to only select one answer
        CheckBox BCheckBox = (CheckBox) findViewById(R.id.B);
        result += "&" +"B" +"="+BCheckBox.isChecked();
        
        CheckBox C = (CheckBox) findViewById(R.id.C);
        result += "&" +"C" +"="+C.isChecked();
        
        CheckBox D = (CheckBox) findViewById(R.id.D);
        result += "&" +"D" +"="+D.isChecked();
        
        Log.i("result", result);
        return result;
        
    }
    // This class is defined to send the form filled by the user to the UCL server
    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {
        
        @Override
        protected void onPreExecute() {
            
        }
        
        @Override
        protected String doInBackground(String... params) {
            URL url;
            String urlParams = params[0];
            // String targetURL = "http://node01.geospatial.ucl.ac.uk/teaching/user42/process_form.php";
            String targetURL="http://node01.geospatial.ucl.ac.uk/teaching/user42/process_form.php";
            
            HttpURLConnection connection = null;
            try {
                // Create connection
                url = new URL(targetURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                                              "application/x-www-form-urlencoded");
                
                connection.setRequestProperty("Content-Length", "" +
                                              Integer.toString(urlParams.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                
                // Send request
                DataOutputStream wr = new DataOutputStream(
                                                           connection.getOutputStream());
                wr.writeBytes(urlParams);
                wr.flush();
                wr.close();
                
                // Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                connection.disconnect();
                return response.toString();
                
            } catch (Exception e) {
                
                e.printStackTrace();
                return null;
                
            } finally {
                
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        
        // Outputs the correct answer on the screen when the user clicks on the
        // Post button
        @Override
        protected void onPostExecute(String response) {
            tv.setText(correct_ans);
        }
    }

