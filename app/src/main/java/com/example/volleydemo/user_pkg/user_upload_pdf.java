package com.example.volleydemo.user_pkg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.volleydemo.R;
import com.example.volleydemo.adapters.EndPoints;
import com.example.volleydemo.basics.BasicSettings;
import com.example.volleydemo.basics.FilePath;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class user_upload_pdf extends Fragment {


    private static final int PICK_FILE_REQUEST = 1;
    //private static final String TAG = .getSimpleName();
    private String selectedFilePath=null;
    private String SERVER_URL ="";
    private ImageView ivAttachment;
    private Button bUpload;
    private TextView tvFileName;
    private ProgressDialog dialog;
    private PowerManager.WakeLock wakeLock;
    private int PICK_FROM_GALLERY=101;
    private static final String TAG="Upload PDF";
    private String username=null,usertype=null,city=null,tit="A",des="B";
    private EditText tv_title,tv_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_upload_pdf, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SERVER_URL=BasicSettings.getUrl()+"upload_pdf.php";
        ivAttachment = (ImageView) view.findViewById(R.id.ivAttachment);
        bUpload = (Button) view.findViewById(R.id.b_upload);
        tvFileName = (TextView) view.findViewById(R.id.tv_file_name);
        tv_title=view.findViewById(R.id.user_pdf_title);
        tv_description=view.findViewById(R.id.user_pdf_des);

        SharedPreferences prefs =getActivity(). getSharedPreferences("mypref",getActivity(). MODE_PRIVATE);
        username = prefs.getString("user", "no"); // parameter, default value
        usertype = prefs.getString("usertype", "no");
        city = prefs.getString("city", "no");



        ivAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tit=tv_title.getText().toString();
                des=tv_description.getText().toString();

                if(tit==null || tit.trim().equals("") || des==null || des.trim().equals(""))
                {
                    Toast.makeText(getActivity(), "Enter Title and Descrition", Toast.LENGTH_SHORT).show();

                }

                if (selectedFilePath != null) {
                    uploadFile(selectedFilePath);
                } else {
                    Toast.makeText(getActivity(), "Please choose a File First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        check_permissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void check_permissions()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_FROM_GALLERY);
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_FROM_GALLERY) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "Access Storage Granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), "Access storage denied", Toast.LENGTH_LONG).show();

            }

        }
    }

    private void showFileChooser() {


        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra("return-data",true);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

               //PowerManager powerManager = (PowerManager) getActivity(). getSystemService(getActivity().POWER_SERVICE);
               //wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
                //wakeLock.acquire();

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(getActivity(), selectedFileUri);
                //Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    tvFileName.setText(selectedFilePath);
                } else {
                    Toast.makeText(getActivity(), "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFile(String filepath) {
        byte[] byteArray = null;
        try {
            File file = new File(filepath);
            byteArray = new byte[(int) file.length()];
            byteArray = FileUtils.readFileToByteArray(file);


        } catch (Exception e) {
            e.printStackTrace();

        }

        if (byteArray != null) {
            final byte[] finalByteArray = byteArray;
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, SERVER_URL,
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
                    params.put("description", des);
                    params.put("usertype", usertype);
                    params.put("city", city);
                    params.put("email", username);
                    return params;
                }

                @Override
                public Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("pic", new DataPart(imagename + ".pdf", finalByteArray));
                    return params;
                }
            };

            //adding the request to volley
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(volleyMultipartRequest);
        }
        else
        {
            Toast.makeText(getActivity(),"Cannot upload file",Toast.LENGTH_SHORT).show();
        }
    }

}
