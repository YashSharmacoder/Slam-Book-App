package com.example.volleydemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.volleydemo.basics.BasicSettings;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView b2;
    private TextInputEditText t1,t2;
    private String data;
    private String result;
    private TextView forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);

        StrictMode.enableDefaults();
        t1= (TextInputEditText) findViewById(R.id.username);
        t2=(TextInputEditText) findViewById(R.id.password);

        b2=(TextView) findViewById(R.id.tv);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,signUp.class);
                startActivity(intent);
            }
        });

        forgot_password=findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
                    View view=inflater.inflate(R.layout.recover_password,null);

                    final EditText et_mail,et_mobile;
                    et_mail=view.findViewById(R.id.recover_password_et1);
                    et_mobile=view.findViewById(R.id.recover_password_et2);

                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Recover Password");
                    builder.setCancelable(true);
                    builder.setView(view);
                    builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String rmail, rmobile;
                                rmail = et_mail.getText().toString();
                                rmobile = et_mobile.getText().toString();

                                if(rmail==null || rmail.trim().equals("")||rmobile==null||rmobile.trim().equals(""))
                                {
                                    Toast.makeText(MainActivity.this, "Enter both email and mobile number.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("email", rmail));
                                    params.add(new BasicNameValuePair("contact", rmobile));
                                    //10.0.2.2 and 192.168.1.105
                                    HttpClient httpClient = new DefaultHttpClient();
                                    HttpPost httpPost = new HttpPost(BasicSettings.getUrl() + "recover_password.php");
                                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                                    HttpResponse response = httpClient.execute(httpPost);


                                    HttpEntity entity = response.getEntity();
                                    InputStream in = entity.getContent();

                                    BufferedReader br = new BufferedReader(new InputStreamReader(in, "ISO_8859_1"), 8);
                                    StringBuilder sb = new StringBuilder();
                                    String line = null;
                                    while ((line = br.readLine()) != null) {
                                        sb.append(line + "\n");

                                    }
                                    br.close();
                                    result = sb.toString();
                                    if (result == null || result.trim().equals("")) {
                                        Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        result = result.trim();
                                        if (result.equalsIgnoreCase("YES")) {
                                            Toast.makeText(MainActivity.this, "Email and Password sent to registered mobile number.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Invalid email or mobile number.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            catch (Exception ee) {
                                Log.d("error1", "" + ee);
                                Toast.makeText(MainActivity.this, ""+ee, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog ad=builder.create();
                    ad.show();



            }
        });

        button= (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String username,password;
                    username=t1.getText().toString();
                    password=t2.getText().toString();
                    ArrayList<NameValuePair> params =new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username",username) );
                    params.add(new BasicNameValuePair("password", password));
                    //10.0.2.2 and 192.168.1.105
                    HttpClient httpClient=new DefaultHttpClient();
                    HttpPost httpPost=new HttpPost(BasicSettings.getUrl()+"b2blogincheck.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse response=httpClient.execute(httpPost);


                    HttpEntity entity=response.getEntity();
                    InputStream in=entity.getContent();

                    BufferedReader br=new BufferedReader(new InputStreamReader(in,"ISO_8859_1"),8);
                    StringBuilder sb=new StringBuilder();
                    String line=null;
                    while ((line=br.readLine())!=null){
                        sb.append(line+"\n");

                    }
                    br.close();
                    result=sb.toString();
                    if(result==null || result.trim().equals("")){
                        Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "User Data : "+result, Toast.LENGTH_SHORT)
                                .show();
                        try
                        {
                            JSONArray jarray=new JSONArray(result);
                            int count=jarray.length();

                            if(count>0)
                            {
                                //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                                JSONObject obj=jarray.getJSONObject(0);
                                String city,usertype;
                                city=obj.getString("city");
                                usertype=obj.getString("usertype");
                                if(result.equalsIgnoreCase("no")){
                                    Toast.makeText(MainActivity.this, "Invalid User. Try Again", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    //Toast.makeText(MainActivity.this, usertype, Toast.LENGTH_SHORT).show();
                                    int otp=new BasicSettings().getOTP(username);
                                    if(otp==1) {
                                        SharedPreferences sp = getSharedPreferences("mypref", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.clear();
                                        editor.putString("user", username); // first is parameter and second is value
                                        editor.putString("usertype", usertype);
                                        editor.putString("city", city);
                                        editor.commit();

                                        t1.setText("");
                                        t2.setText("");
                                        Toast.makeText(MainActivity.this, "Usertype : "+usertype, Toast.LENGTH_SHORT).show();
                                        if (usertype.trim().equalsIgnoreCase("admin")) {
                                            Intent intent = new Intent(MainActivity.this, admin_home.class);
                                            Toast.makeText(MainActivity.this, "Opening Admin HOme : ", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        } else if (usertype.equalsIgnoreCase("user")) {
                                            Intent intent = new Intent(MainActivity.this, user_home.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MainActivity.this, "No Usertype Selected", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        process_otp(otp,username,usertype,city);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "JSON : No Record Found", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch(Exception e)
                        {
                            Log.e("error2", "" + e);
                            Toast.makeText(MainActivity.this, "Error2 JSON : "+e, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch(Exception e){
                    Log.d("error3", "" + e);
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });


    }
    private void process_otp(final int otp, final String username, final String usertype, final String city)
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View prompt = inflater.inflate(R.layout.get_otp,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(prompt);
        builder.setMessage("Enter OTP");
        builder.setTitle("OTP Service");

        final EditText otp_t1=(EditText) prompt.findViewById(R.id.getotp_et1);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String str=otp_t1.getText().toString();
                if(str!=null && str.trim().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter OTP"+str, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int new_otp= Integer.parseInt(str.trim());
                    if(new_otp==otp)
                    {
                        String r=new BasicSettings().update_otp(username);
                        if(r.trim().equalsIgnoreCase("NO"))
                        {
                            Toast.makeText(MainActivity.this,"Server not responding. Try again", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SharedPreferences sp = getSharedPreferences("mypref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.putString("user", username); // first is parameter and second is value
                            editor.putString("usertype", usertype);
                            editor.putString("city", city);
                            editor.commit();

                            t1.setText("");
                            t2.setText("");

                            if (usertype.equalsIgnoreCase("admin")) {
                                Intent intent = new Intent(MainActivity.this, admin_home.class);
                                startActivity(intent);
                            } else if (usertype.equalsIgnoreCase("user")) {
                                Intent intent = new Intent(MainActivity.this, user_home.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "No Usertype Selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Invalid OTP Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog ad =builder.create();
        ad.show();
    }
}
