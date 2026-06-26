package com.example.volleydemo.user_pkg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.volleydemo.R;
import com.example.volleydemo.adapters.EndPoints;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class upload_pic extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int IMG_REQUEST=777;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final static int MY_CAMERA_REQUEST_CODE=200;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int PERMISSION_CODE=11;

    private String mediaPath;
    private String postPath;


    public upload_pic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
        else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_FROM_GALLERY);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_pic, container, false);
    }

    private Button button;
    private EditText title,desc;
    private ImageView imgView;
    private Button camara,gallary;
    Bitmap bitmap,bitmap2;
    private String tit=null,dsc=null;
    private String city="kota";
    private String username=null,usertype=null;
    private int flag=0;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button=view.findViewById(R.id.upload);
        desc=view.findViewById(R.id.descpImage);
        title=view.findViewById(R.id.titleImage);
        camara=view.findViewById(R.id.butt_camara);
        gallary=view.findViewById(R.id.butt_gallary);
        imgView=view.findViewById(R.id.img_view);

        SharedPreferences prefs =getActivity(). getSharedPreferences("mypref",getActivity(). MODE_PRIVATE);
        username = prefs.getString("user", "no"); // parameter, default value
        usertype = prefs.getString("usertype", "no");
        city = prefs.getString("city", "no");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tit=title.getText().toString();
                dsc=desc.getText().toString();

                if(tit==null || tit.equals("") || dsc==null || dsc.equals(""))
                {
                    Toast.makeText(getActivity(),"Enter both title and description",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(bitmap==null)
                    {
                        Toast.makeText(getActivity(),"No image selected",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        uploadBitmap(bitmap);


                        FragmentTransaction t = getFragmentManager().beginTransaction();
                        Fragment mFrag = new user_user_post();
                        t.replace(R.id.fragment_container, mFrag);
                        t.commit();
                    }
                }
            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void openCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
    private void selectImage()
    {
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,101);

//        Intent intent=new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,101);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getActivity(),"Hello "+requestCode,Toast.LENGTH_SHORT).show();
        if(requestCode==0 && resultCode==RESULT_OK && data!=null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(bitmap);
            flag=1;
        }
        else if (resultCode==RESULT_OK && requestCode==101){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath = cursor.getString(columnIndex);
            // Set the Image in ImageView for Previewing the Media
            bitmap=BitmapFactory.decodeFile(mediaPath);
            imgView.setImageBitmap(bitmap);
            cursor.close();
            //flag=2;
            if(mediaPath==null)
                Toast.makeText(getActivity(),"Select the image from photos or gallery",Toast.LENGTH_SHORT).show();
            //else
                //Toast.makeText(getActivity(),"Selected image : "+mediaPath,Toast.LENGTH_SHORT).show();
        }

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try
        {
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        }
        catch (NullPointerException rr)
        {
            rr.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("title", tit);
                params.put("description", dsc);
                params.put("usertype",usertype);
                params.put("city",city);
                params.put("email",username);
                return params;
            }

            @Override
            public Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(volleyMultipartRequest);
    }


    public static boolean hasPermissions(Context context, String... permissions){
        if(context!=null && permissions!=null){
            for(String permission : permissions){
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();

            }
        }
        else if (requestCode == PERMISSION_CODE) {

            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "gallery permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), "gallery permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}
