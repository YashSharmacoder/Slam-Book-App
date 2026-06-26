package com.example.volleydemo.adapters;


import com.example.volleydemo.basics.BasicSettings;

public class EndPoints {
    private static final String ROOT_URL = BasicSettings.getUrl()+ "Api.php?apicall=";
    public static final String UPLOAD_URL = ROOT_URL + "uploadpic";
    public static final String GET_PICS_URL = ROOT_URL + "getpics";
}
