package com.example.chat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder{

    public com.google.android.material.imageview.ShapeableImageView profileImage;
    public TextView userName;
    public TextView message;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImage = itemView.findViewById(R.id.chat_img);
        userName = itemView.findViewById(R.id.chatName);
        message = itemView.findViewById(R.id.chatMessage);
    }
}
