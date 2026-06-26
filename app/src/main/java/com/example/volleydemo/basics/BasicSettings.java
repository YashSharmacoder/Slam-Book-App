package com.example.volleydemo.basics;

import android.util.Log;
import android.widget.Toast;


import com.example.volleydemo.models.User;

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

public class BasicSettings {

    public static String getUrl()
    {

        return "http://192.168.1.2/project/";
    }

    public String block_status(int id)
    {
        String f="NO";
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", ""+id));
            //nameValuePairs.add(new BasicNameValuePair("text", text));


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(getUrl()+"block_status.php");
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
            String result = sb.toString();
            if(result!=null)
            {
                result=result.trim();
                f=result;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return f;
    }

    public String block_post(int id)
    {
        String f="NO";
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", ""+id));
            //nameValuePairs.add(new BasicNameValuePair("text", text));


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(getUrl()+"block_post.php");
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
            String result = sb.toString();
            if(result!=null)
            {
                result=result.trim();
                f=result;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return f;
    }
    public String unblock_post(int id)
    {
        String f="NO";
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", ""+id));
            //nameValuePairs.add(new BasicNameValuePair("text", text));


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(getUrl()+"unblock_post.php");
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
            String result = sb.toString();
            if(result!=null)
            {
                result=result.trim();
                f=result;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return f;
    }
    public String getContact(String email)
    {
        String contact="NO";

        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", email));
            //nameValuePairs.add(new BasicNameValuePair("text", text));


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(getUrl()+"find_contact.php");
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
            String result = sb.toString();
            if(result!=null)
            {
                result=result.trim();
                contact=result;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return contact;
    }
    public int getOTP(String email)
    {
        int otp=0;

        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", email));
            //nameValuePairs.add(new BasicNameValuePair("text", text));


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(getUrl()+"find_otp.php");
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
            String result = sb.toString();
            if(result!=null)
            {
                result=result.trim();
                otp= Integer.parseInt(result);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 1;
    }
    public String update_otp(String email)
    {
        String r="NO";

        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", email));
            //nameValuePairs.add(new BasicNameValuePair("text", text));


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(getUrl()+"update_otp.php");
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
            String result = sb.toString();
            if(result!=null)
            {
                result=result.trim();
                r=result;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }

    public int get_post_id(String email,String img)
    {
        int id=0;

        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("img", img));
            //nameValuePairs.add(new BasicNameValuePair("text", text));


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(getUrl()+"get_post_id.php");
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
            String result = sb.toString();
            if(result!=null)
            {
                result=result.trim();
                id= Integer.parseInt(result);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }

    public void delete_post(User user)
    {
        try
        {
            ArrayList<NameValuePair> params =new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("t",""+user.getT()) );


            //10.0.2.2 and 192.168.1.105
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(BasicSettings.getUrl()+"delete_post.php");
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
            String result=sb.toString();
            Log.d("Result : ",result);
            //Toast.makeText(getActivity().getApplicationContext(),result, Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //Toast.makeText(getActivity().getApplicationContext(),""+e, Toast.LENGTH_LONG).show();
        }
    }
}
