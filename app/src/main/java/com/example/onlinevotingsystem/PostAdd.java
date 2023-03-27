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
import android.widget.ProgressBar;
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
import java.util.Objects;

public class PostAdd extends AppCompatActivity{
    TextInputEditText editTextTitle, editTextDesc, editTextOp1, editTextOp2;
    Button add_button, back_button;

    ProgressBar progressBar;
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
        back_button = findViewById(R.id.backPostButton);
        progressBar = findViewById(R.id.progressBar);

        editTextTitle = findViewById(R.id.editTextPostTitle);
        editTextDesc = findViewById(R.id.editTextPostDesc);
        editTextOp1 = findViewById(R.id.button_1_description);
        editTextOp2 = findViewById(R.id.button_2_description);


        currUser = auth.getCurrentUser();

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String title = Objects.requireNonNull(editTextTitle.getText()).toString();
                String description = Objects.requireNonNull(editTextDesc.getText()).toString();
                String option_one = Objects.requireNonNull(editTextOp1.getText()).toString();
                String option_two = Objects.requireNonNull(editTextOp2.getText()).toString();


                StoreNewPostData(currUser.getUid(), title, description, option_one, option_two);

            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void StoreNewPostData(String creator_id, String title,String description, String option_one, String option_two){



        idRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                int post_id = Integer.valueOf(snapshot.getValue().toString());
                ArrayList<String> voterList = new ArrayList<String>();

                Post post = new Post(post_id, creator_id, title, description, option_one, option_two);

                myRef = database.getReference().child("posts").child(String.valueOf(post_id));
                myRef.setValue(post);

                Voters voters = new Voters(voterList);
                myRef.child("voters").setValue(voters);


                post_id++;
                idRef.setValue(post_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

}