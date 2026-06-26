package com.example.volleydemo.admin_pkg;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.volleydemo.R;
import com.example.volleydemo.admin_home;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class new_admin extends AppCompatActivity {
    private TextInputEditText t1,t2,t3,t4,t5,t6,t7;
    private Button signButton;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_admin);

        t1=findViewById(R.id.txt_name);
        t2=findViewById(R.id.txt_address);
        t3=findViewById(R.id.txt_city);
        t4=findViewById(R.id.txt_contact);
        t5=findViewById(R.id.txt_email);
        t6=findViewById(R.id.txt_password);
        t7=findViewById(R.id.txt_confirmpassword);

        signButton=(Button)findViewById(R.id.butt_signup);


        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String name,address,city,contact,email,password,confirmpassword;
                    name=t1.getText().toString();
                    address=t2.getText().toString();
                    city=t3.getText().toString();
                    contact=t4.getText().toString();
                    email=t5.getText().toString();
                    password=t6.getText().toString();
                    confirmpassword=t7.getText().toString();

                    if(!password.equals(confirmpassword))
                    {
                        Toast.makeText(new_admin.this,"Password Don't Match", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        ArrayList<NameValuePair> params =new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("name",name) );
                        params.add(new BasicNameValuePair("address", address));
                        params.add(new BasicNameValuePair("city", city));
                        params.add(new BasicNameValuePair("contact", contact));
                        params.add(new BasicNameValuePair("email", email));
                        params.add(new BasicNameValuePair("password", password));
                        //10.0.2.2 and 192.168.1.105
                        HttpClient httpClient=new DefaultHttpClient();
                        HttpPost httpPost=new HttpPost(BasicSettings.getUrl()+"new_admin.php");
                        httpPost.setEntity(new UrlEncodedFormEntity(params));
                        HttpResponse response=httpClient.execute(httpPost);


                        HttpEntity entity=response.getEntity();
                        InputStream in=entity.getContent();

                        BufferedReader br=new BufferedReader(new InputStreamReader(in,"iso-8859-1"),8);
                        StringBuilder sb=new StringBuilder();
                        String line=null;
                        while ((line=br.readLine())!=null){
                            sb.append(line+"\n");

                        }
                        br.close();
                        result=sb.toString();
                        Toast.makeText(new_admin.this,result, Toast.LENGTH_LONG).show();
                        Intent i2=new Intent(new_admin.this, admin_home.class);
                        startActivity(i2);
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(new_admin.this, "Error "+e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
