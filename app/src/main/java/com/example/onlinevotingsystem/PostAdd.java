package com.example.onlinevotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.onlinevotingsystem.Post;
import com.example.onlinevotingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostAdd extends AppCompatActivity{
    EditText editText;
    Button add_button;
    FirebaseDatabase database;
    DatabaseReference idRef;
    DatabaseReference myRef;

    FirebaseUser currUser;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_post_add);

        database = FirebaseDatabase.getInstance();
        idRef = database.getReference("posts_id");
        myRef =  database.getReference();

        add_button = findViewById(R.id.addPostButton);
        currUser = auth.getCurrentUser();

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = findViewById(R.id.editTextPostTitle);
                String title = editText.getText().toString();
                StoreNewPostData(currUser.getUid(), title, title);

            }
        });



    }

    public void StoreNewPostData(String creator_id, String title,String description){



        idRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int post_id = Integer.valueOf(snapshot.getValue().toString());
                Post post = new Post(post_id, creator_id, title, description);
                myRef = database.getReference().child("posts").child(String.valueOf(post_id));
                myRef.setValue(post);
                post_id++;
                idRef.setValue(post_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}