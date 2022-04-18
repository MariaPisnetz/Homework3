package com.example.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        TextView welcome = findViewById(R.id.welcomeTV);
        welcome.setText("Welcome, " + firebaseUser.getDisplayName());

        adapter = new Adapter();
        RecyclerView recycler = findViewById(R.id.chatRV);
        recycler.setHasFixedSize(false);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),1);
        recycler.setLayoutManager(manager);

        ImageView userImage = findViewById(R.id.mainUserImage);
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(userImage);

        recycler.setAdapter(adapter);

        FloatingActionButton btn = findViewById(R.id.addMessageBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                EditText text = findViewById(R.id.writeMessage);
                Message m = new Message(firebaseUser.getPhotoUrl().toString(), firebaseUser.getDisplayName(), text.getText().toString());
                adapter.addMessage(m);
                text.setText("");
            }
        });

        ImageButton exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(MainPage.this, MainActivity.class));
            }
        });
    }
}