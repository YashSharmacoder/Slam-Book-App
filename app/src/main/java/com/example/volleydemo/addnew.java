package com.example.volleydemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static com.example.volleydemo.admin_pkg.admin_upload_pic.hasPermissions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;


public class addnew extends AppCompatActivity {
    TextInputEditText fname,lname,mobile,email;
    Button b;
    private String fnamet,lnamet,mobilet,emailt;
    Bitmap bitmap,bitmap2;
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        StrictMode.enableDefaults();
        b=(Button)findViewById(R.id.savebutton);
        imageButton=findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openCamera();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                fname=(TextInputEditText)findViewById(R.id.fname);
                lname=(TextInputEditText)findViewById(R.id.lname);
                mobile=(TextInputEditText)findViewById(R.id.mobile);
                email=(TextInputEditText)findViewById(R.id.email);
                
                fnamet=fname.getText().toString();
                lnamet=lname.getText().toString();
                mobilet=mobile.getText().toString();
                emailt=email.getText().toString();
                if(fnamet==null || fnamet.trim().equalsIgnoreCase("")){
                    fname.setError("Enter First Name");
                }else if(lnamet==null || lnamet.trim().equalsIgnoreCase("")){
                    lname.setError("Enter Last Name");
                }else if(mobilet==null || mobilet.trim().equalsIgnoreCase("") || mobilet.length()!= 10 || mobilet.length()<0 || mobilet.length()>=11){
                    mobile.setError("Enter Valid Contact Number : 10 Digits");
                }else if(emailt==null || emailt.trim().equalsIgnoreCase("")){
                    email.setError("Enter Valid Email Address");
                }
                else{
                    Intent intent = new Intent(addnew.this, admin_home.class);
                    startActivity(intent);
                }

            }
        });
       
    }

}
