package com.example.volleydemo.user_pkg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class user_user_post extends Fragment {

    private RecyclerView rcc;
    private static final String url = BasicSettings.getUrl()+ "user_user_post.php";
    private Spinner spinner;
    private EditText et1;
    private Button btn;
    private String selected_item="Title";

    public user_user_post(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_user_post,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner=view.findViewById(R.id.user_user_spinner);
        et1=view.findViewById(R.id.user_user_search);
        btn=view.findViewById(R.id.user_user_search_btn);

        List<String> op = new ArrayList<String>();
        op.add("Title");
        op.add("Description");
        op.add("City");
        op.add("Name");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, op);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_item=spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String option=et1.getText().toString();
                if(option==null || option.trim().equals(""))
                {
                    Toast.makeText(getActivity(),"Enter text", Toast.LENGTH_SHORT).show();
                    et1.requestFocus();
                }
                else {
                    reloadMyFunc1(option);
                }
            }
        });

        rcc=view.findViewById(R.id.rcv1);
        rcc.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcc.setHasFixedSize(true);
        reloadMyFunc();
    }


    private void reloadMyFunc() {
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DATA: ",response);
                GsonBuilder gsonBuilder=new GsonBuilder();
                Gson gson=gsonBuilder.create();
                User[] users =gson.fromJson(response, User[].class);
                rcc.setAdapter(new UserAdapter(getActivity().getApplicationContext(), users, new OnItemClickListener() {
                    @Override
                    public void onItemClick(User user) {

                        String imgname = user.getImage();
                        String file_ext = imgname.substring(imgname.length() - 3);
                        //Toast.makeText(getActivity(),"File extension : "+file_ext,Toast.LENGTH_SHORT).show();
                        if (file_ext.equalsIgnoreCase("pdf")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imgname));
                            startActivity(browserIntent);
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
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
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



    private void reloadMyFunc1(final String text) {

        String result="";
        String addr=BasicSettings.getUrl()+ "search_user_post.php";
        try
        {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("option", selected_item));
            nameValuePairs.add(new BasicNameValuePair("text", text));



            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(addr);
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
            //Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            try
            {
                JSONArray jarray=new JSONArray(result);
                int count=jarray.length();

                if(count>0) {

                    User users[]=new User[count];
                    for(int i=0; i<count; i++)
                    {
                        JSONObject obj = jarray.getJSONObject(i);
                        String email,title,description,image,usertype,city;
                        long t;
                        email=obj.getString("email");
                        title=obj.getString("title");
                        description=obj.getString("description");
                        image=obj.getString("image");
                        usertype=obj.getString("usertype");
                        city=obj.getString("city");
                        t=obj.getLong("t");
                        User usr=new User(email,title,description,image,t,usertype,city);
                        users[i]=usr;
                        //Toast.makeText(getActivity(), "Email : "+email, Toast.LENGTH_LONG).show();
                    }


                    rcc.setAdapter(new UserAdapter(getActivity().getApplicationContext(), users, new OnItemClickListener() {
                        @Override
                        public void onItemClick(User user) {

                            String imgname = user.getImage();
                            String file_ext = imgname.substring(imgname.length() - 3);
                            //Toast.makeText(getActivity(),"File extension : "+file_ext,Toast.LENGTH_SHORT).show();
                            if (file_ext.equalsIgnoreCase("pdf")) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imgname));
                                startActivity(browserIntent);
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
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
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
                else
                {
                    Toast.makeText(getActivity(), "No Data Found ", Toast.LENGTH_LONG).show();
                }

            }
            catch (Exception y)
            {
                y.printStackTrace();
                Toast.makeText(getActivity(), "JSON Exception : "+y, Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Exception : "+e, Toast.LENGTH_LONG).show();
        }
    }

}
