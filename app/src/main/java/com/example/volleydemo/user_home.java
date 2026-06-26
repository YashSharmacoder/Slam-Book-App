package com.example.volleydemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.volleydemo.basics.BasicSettings;
import com.example.volleydemo.user_pkg.User_myphoto;
import com.example.volleydemo.user_pkg.upload_pic;
import com.example.volleydemo.user_pkg.user_admin_frag;
import com.example.volleydemo.user_pkg.user_upload_pdf;
import com.example.volleydemo.user_pkg.user_user_post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class user_home extends AppCompatActivity {

    private TextView mTextMessage;

    private String username="no",usertype="no";

    private String result;
    private TextInputEditText et1,et2,et3,et4,et5,et6,et7;
    private TextView tv1;

    private String n1,a1,c1,c2,e1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int id=item.getItemId();
                if(id==R.id.admin_phot)
                {
                    user_admin();
                    return true;
                }
                else if(id==R.id.user_photo)
                {
                    user_user_post f2=new user_user_post();
                    FragmentTransaction fit=getSupportFragmentManager().beginTransaction();
                    fit.replace(R.id.fragment_container,f2);
                    fit.addToBackStack(null);
                    fit.commit();
                    return true;
                }
                else if(id==R.id.upload_photo)
                {
                    upload_pic f3=new upload_pic();
                    FragmentTransaction fot=getSupportFragmentManager().beginTransaction();
                    fot.replace(R.id.fragment_container,f3);
                    fot.addToBackStack(null);
                    fot.commit();
                    return true;
                }
                else if(id==R.id.my_photo)
                {
                    User_myphoto f4=new User_myphoto();
                    FragmentTransaction foto=getSupportFragmentManager().beginTransaction();
                    foto.replace(R.id.fragment_container,f4);
                    foto.addToBackStack(null);
                    foto.commit();
                    return true;
                }
                else if(id==R.id.pdf_upload)
                {
                    user_upload_pdf f5=new user_upload_pdf();
                    FragmentTransaction foto5=getSupportFragmentManager().beginTransaction();
                    foto5.replace(R.id.fragment_container,f5);
                    foto5.addToBackStack(null);
                    foto5.commit();
                    return true;
                }

            return false;
        }
    };

    private void user_admin() {
        user_admin_frag f1=new user_admin_frag();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,f1);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);

        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        username = prefs.getString("user", "no"); // parameter, default value
        usertype = prefs.getString("usertype", "no");

        if(usertype.equalsIgnoreCase("no"))
        {
            Intent i1=new Intent(user_home.this,MainActivity.class);
            startActivity(i1);
        }

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        user_admin();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater awesome=getMenuInflater();
        awesome.inflate(R.menu.user_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        final int id=item.getItemId();
        if(id==R.id.user_profile)
        {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(BasicSettings.getUrl()+"fetchuser.php?username=" + username);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity=response.getEntity();

                InputStream in=entity.getContent();
                BufferedReader br=new BufferedReader(new InputStreamReader(in,"ISO_8859_1"),8);

                StringBuilder sb=new StringBuilder();
                String line=null;

                while ((line=br.readLine())!=null)
                {
                    sb.append(line+"\n");
                }
                br.close();
                result=sb.toString();
            }
            catch (Exception e)
            {
                Toast.makeText(user_home.this, "Reading:"+e, Toast.LENGTH_SHORT).show();
            }

            try
            {
                JSONArray jarray=new JSONArray(result);
                int count=jarray.length();

                if(count>0)
                {
                    JSONObject obj=jarray.getJSONObject(0);

                    n1=obj.getString("name");
                    a1=obj.getString("address");
                    c1=obj.getString("city");
                    c2=obj.getString("contact");
                    e1=obj.getString("email");


                    //Toast.makeText(context, n1+a1+c1+c2+e1, Toast.LENGTH_SHORT).show();

                    LayoutInflater li= LayoutInflater.from(user_home.this);
                    View view=li.inflate(R.layout.profile,null);

                    AlertDialog.Builder alertBuilder=new AlertDialog.Builder(user_home.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Profile");
                    alertBuilder.setView(view);
                    et1=view.findViewById(R.id.t_name);
                    et2=view.findViewById(R.id.t_address);
                    et3=view.findViewById(R.id.t_city);
                    et4=view.findViewById(R.id.t_contact);
                    tv1=view.findViewById(R.id.t_email);

                    et1.setText(n1);
                    et2.setText(a1);
                    et3.setText(c1);
                    et4.setText(c2);
                    tv1.setText(e1);

                    alertBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try
                            {
                                String name,address,city,contact,email;
                                name=et1.getText().toString().trim();
                                address=et2.getText().toString().trim();
                                city=et3.getText().toString().trim();
                                contact=et4.getText().toString().trim();
                                email=tv1.getText().toString().trim();

                                if (name.equalsIgnoreCase("") || address.equalsIgnoreCase("") || city.equalsIgnoreCase("") ||
                                        contact.equalsIgnoreCase("")){
                                    Toast.makeText(user_home.this, "Don't left any field empty.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {

                                    update(n1, a1, c1, c2, e1, name, address, city, contact, email);
                                }
                            }
                            catch (Exception e){}
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog=alertBuilder.create();
                    alertDialog.show();
                }
                else
                {
                    Toast.makeText(user_home.this, "No record found", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(user_home.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }




            return true;
        }
        else if(id==R.id.user_logout)
        {
            SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("user");
            editor.remove("usertype");
            editor.remove("city");
            editor.commit();

            Intent i1=new Intent(user_home.this,MainActivity.class);
            startActivity(i1);

            //Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(id==R.id.user_password)
        {
            LayoutInflater li = LayoutInflater.from(user_home.this);
            View view = li.inflate(R.layout.change_password, null);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(user_home.this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Change Password");
            alertBuilder.setView(view);

            et5=view.findViewById(R.id.old_password);
            et6=view.findViewById(R.id.new_password);
            et7=view.findViewById(R.id.confirm_new_password);

            alertBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        String oldPassword, newPassword, confirmPassword;
                        oldPassword = et5.getText().toString().trim();
                        newPassword = et6.getText().toString().trim();
                        confirmPassword = et7.getText().toString().trim();

                        if (!newPassword.equals(confirmPassword)) {
                            Toast.makeText(user_home.this, "Password Don't Match", Toast.LENGTH_LONG).show();
                        } else {
                            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("oldPassword", oldPassword));
                            nameValuePairs.add(new BasicNameValuePair("newPassword", newPassword));
                            nameValuePairs.add(new BasicNameValuePair("username", username));


                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost(BasicSettings.getUrl() + "change_password.php");
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                            Toast.makeText(user_home.this, result, Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
            return true;
        }

        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        username = prefs.getString("user", "no"); // parameter, default value
        usertype = prefs.getString("usertype", "no");
        if(usertype==null || usertype.equalsIgnoreCase("user")==false)
        {
            Intent i1=new Intent(user_home.this,MainActivity.class);
            startActivity(i1);
        }
    }


    private void update(String oldname, String oldaddress, String oldcity, String oldcontact,
                        String oldemail, String newname, String newaddress, String newcity, String newcontact,
                        String newemail){
        try {
            String on = oldname;
            String oad = oldaddress;
            String oc = oldcity;
            String ocon = oldcontact;
            String oem = oldemail;
            String nn = newname;
            String nad = newaddress;
            String nc = newcity;
            String ncon = newcontact;
            String nem = newemail;

            ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("on",on));
            nameValuePairs.add(new BasicNameValuePair("oad",oad));
            nameValuePairs.add(new BasicNameValuePair("oc",oc));
            nameValuePairs.add(new BasicNameValuePair("ocon",ocon));
            nameValuePairs.add(new BasicNameValuePair("oem",oem));
            nameValuePairs.add(new BasicNameValuePair("nn",nn));
            nameValuePairs.add(new BasicNameValuePair("nad",nad));
            nameValuePairs.add(new BasicNameValuePair("nc",nc));
            nameValuePairs.add(new BasicNameValuePair("ncon",ncon));
            nameValuePairs.add(new BasicNameValuePair("nem",nem));


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(BasicSettings.getUrl()+"update.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity=response.getEntity();

            InputStream in=entity.getContent();
            BufferedReader br=new BufferedReader(new InputStreamReader(in,"ISO_8859_1"),8);

            StringBuilder sb=new StringBuilder();
            String line=null;

            while ((line=br.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            br.close();
            result=sb.toString();
            Toast.makeText(user_home.this, result, Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(user_home.this, "Error:"+e, Toast.LENGTH_SHORT).show();
        }
    }

}
