package com.example.volleydemo.singleton;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton
{
    private static MySingleton intance;
    private static Context ctx;
    private RequestQueue requestQueue;

    public MySingleton(Context context)
    {
        ctx=context;
        requestQueue=getRequestQueue();
    }
    public static synchronized MySingleton getIntance(Context context)
    {
        if(intance==null)
        {
            intance=new MySingleton(context);
        }
        return intance;
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley. newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    //Create template method to add request
    public <T> void addToRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }

}
