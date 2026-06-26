package com.example.volleydemo.adapters;


import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.volleydemo.R;
import com.example.volleydemo.basics.BasicSettings;
import com.example.volleydemo.listeners.OnItemClickListener;
import com.example.volleydemo.models.User;


/**
 * Created by mrohi on 24-03-2019.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ShowEmployeeHolder> {

    Context context;
    User[] users;
    public OnItemClickListener listener;
    public UserAdapter(Context context, User[] users, OnItemClickListener listener)
        {
        this.context=context;
        this.users = users;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ShowEmployeeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.row,viewGroup,false);

        return new ShowEmployeeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowEmployeeHolder showEmployeeHolder, final int i) {
        final User user = users[i];

        String title,desc;
        title=user.getTitle();
        desc=user.getDescription();
        if(title.length()>24)
        {
            title=title.substring(0,24);
        }
        if(desc.length()>36)
        {
            desc=desc.substring(0,36);
            desc=desc+" ...";
        }

        if(user.getImage().trim().equalsIgnoreCase("no")==false) {
            String imgname = user.getImage();
            String file_ext = imgname.substring(imgname.length() - 3);
            if (file_ext.equalsIgnoreCase("pdf")) {
                imgname = BasicSettings.getUrl() + "/uploads/pdf.png";
                Glide.with(showEmployeeHolder.img.getContext()).load(imgname).into(showEmployeeHolder.img);
            } else {
                Glide.with(showEmployeeHolder.img.getContext()).load(user.getImage()).into(showEmployeeHolder.img);
            }
        }
        //Change color according to block status
        final int postid=new BasicSettings().get_post_id(user.getEmail(),user.getImage());
        String b_status=new BasicSettings().block_status(postid);

        int color= Color.parseColor("#000000");
        if(b_status.equalsIgnoreCase("YES"))
        {

            color=Color.parseColor("#ff0000");
        }

        showEmployeeHolder.tv.setTextColor(color);
        showEmployeeHolder.utype.setTextColor(color);
        showEmployeeHolder.tv1.setTextColor(color);

        showEmployeeHolder.tv.setText(title);
        showEmployeeHolder.utype.setText(desc);
        showEmployeeHolder.tv1.setText(new BasicSettings().getContact(user.getEmail())+" ["+user.getCity()+"]");

        showEmployeeHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Clicked"+user.getTitle(), Toast.LENGTH_SHORT).show();
                listener.onItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.length;
    }

    public static class ShowEmployeeHolder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView tv;
        TextView utype;
        TextView tv1;
        public ShowEmployeeHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.profile);
            tv=itemView.findViewById(R.id.Name);
            tv1=itemView.findViewById(R.id.row_tv1);
            utype=itemView.findViewById(R.id.usertype4);
        }
    }
}
