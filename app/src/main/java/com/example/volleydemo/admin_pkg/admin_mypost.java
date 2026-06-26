package com.example.volleydemo.admin_pkg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.example.volleydemo.R;
import com.example.volleydemo.adapters.UserAdapter;
import com.example.volleydemo.basics.BasicSettings;
import com.example.volleydemo.listeners.OnItemClickListener;
import com.example.volleydemo.models.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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


public class admin_mypost extends Fragment {
    private RecyclerView rcc;
    private String username="",usertype="";

    public admin_mypost(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_mypost,container,false);
    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("mypref", getActivity().MODE_PRIVATE);
        username = prefs.getString("user", "no"); // parameter, default value
        usertype = prefs.getString("usertype", "no");

        rcc=view.findViewById(R.id.rcv1);
        rcc.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcc.setHasFixedSize(true);
        reloadMyFunc();
    }


    private void reloadMyFunc() {
        final String url = BasicSettings.getUrl()+ "show_my_post_admin.php?username="+username;
        //Toast.makeText(getActivity().getApplicationContext(),"URL "+url,Toast.LENGTH_LONG).show();
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DATA: ",response);
                GsonBuilder gsonBuilder=new GsonBuilder();
                Gson gson=gsonBuilder.create();
                final User[] users =gson.fromJson(response, User[].class);
                rcc.setAdapter(new UserAdapter(getActivity().getApplicationContext(), users, new OnItemClickListener() {
                    @Override
                    public void onItemClick(final User user) {
                        final String imgname = user.getImage();
                        String file_ext = imgname.substring(imgname.length() - 3);
                        //Toast.makeText(getActivity(),"File extension : "+file_ext,Toast.LENGTH_SHORT).show();
                        if (file_ext.equalsIgnoreCase("pdf")) {

                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            View prompt = inflater.inflate(R.layout.edit_post, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(prompt);

                            final TextView tv1, tv2, tv3, tv4;
                            final ImageView img1;

                            tv1 = (TextView) prompt.findViewById(R.id.editpost_tv1);
                            tv2 = (TextView) prompt.findViewById(R.id.editpost_tv2);

                            img1 = (ImageView) prompt.findViewById(R.id.editpost_img);


                            Glide.with(img1.getContext()).load(BasicSettings.getUrl()+"/uploads/pdf.png").into(img1);

                            tv1.setText(user.getTitle());
                            //tv1.setTextColor(color);
                            tv2.setText(user.getDescription());
                            //tv2.setTextColor(color);

                            tv3 = (TextView) prompt.findViewById(R.id.editpost_tv3);
                            tv4 = (TextView) prompt.findViewById(R.id.editpost_tv4);
                            tv3.setText(new BasicSettings().getContact(user.getEmail()) + " [" + user.getCity() + "]");
                            tv4.setText("Email: " + user.getEmail());



                            builder.setTitle("Manage Post");
                            builder.setCancelable(true);

                            builder.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imgname));
                                    startActivity(browserIntent);

                                }
                            });
                            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new BasicSettings().delete_post(user);
                                    reloadMyFunc();
                                }
                            });

                            AlertDialog ad = builder.create();
                            ad.show();




                        } else {
                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            View prompt = inflater.inflate(R.layout.edit_post, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(prompt);

                            final TextView tv1, tv2;
                            final ImageView img1;

                            tv1 = (TextView) prompt.findViewById(R.id.editpost_tv1);
                            tv2 = (TextView) prompt.findViewById(R.id.editpost_tv2);
                            img1 = (ImageView) prompt.findViewById(R.id.editpost_img);

                            tv1.setText(user.getTitle());
                            tv2.setText(user.getDescription());
                            Glide.with(img1.getContext()).load(user.getImage()).into(img1);

                            final TextView tv3, tv4;
                            tv3 = (TextView) prompt.findViewById(R.id.editpost_tv3);
                            tv4 = (TextView) prompt.findViewById(R.id.editpost_tv4);
                            tv3.setText(new BasicSettings().getContact(user.getEmail()) + " [" + user.getCity() + "]");
                            tv4.setText("Email: " + user.getEmail());

                            builder.setCancelable(false);
                            builder.setTitle("Manage Post");
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new BasicSettings().delete_post(user);
                                    reloadMyFunc();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog ad = builder.create();
                            ad.show();
                        }
                    }
                }));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"ERROR", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
