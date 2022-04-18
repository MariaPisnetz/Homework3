package com.example.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<ViewHolder>{

    private List<Message> messages = new ArrayList<>();

    public Adapter() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            messages = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                try {
                                    Message m = new Message(
                                            document.get("userPhoto").toString(),
                                            document.get("userName").toString(),
                                            document.get("message").toString()
                                    );
                                    messages.add(m);
                                } catch (Exception e) {
                                }
                            }
                            notifyDataSetChanged();
                        }
                    }
                });


        db.collection("chat").addSnapshotListener(new  EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                messages = new ArrayList<>();
                for (DocumentSnapshot document : value.getDocuments())
                {
                    try {
                        Message objectMessage = new Message(
                                document.get("userPhoto").toString(),
                                document.get("userName").toString(),
                                document.get("message").toString()
                        );
                        messages.add(objectMessage);
                    } catch (Exception e) {
                    }
                }
                notifyDataSetChanged();
            }
        });

    }

    public void addMessage(Message m) {
        Map<String, Object> message = new HashMap<>();
        message.put("userPhoto", m.photo);
        message.put("userName", m.userName);
        message.put("message",m.message);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chat").document(String.valueOf(System.currentTimeMillis()))
                .set(message);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.message.setText(message.message);
        holder.userName.setText(message.userName);
        Glide.with(holder.profileImage.getContext()).load(message.photo).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
