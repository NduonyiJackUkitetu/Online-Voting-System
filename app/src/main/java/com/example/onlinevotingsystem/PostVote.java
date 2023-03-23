package com.example.onlinevotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostVote extends AppCompatActivity {
    TextView title;
    TextView description;

    TextView like_count;
    TextView dislike_count;
    String post_id;


    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_vote);
        Intent intent = getIntent();
        title = findViewById(R.id.postVoteTitle);
        description = findViewById(R.id.postVoteDesc);
        like_count = findViewById(R.id.postVoteLikeCount);
        dislike_count = findViewById(R.id.postVoteDislikeCount);

        post_id =  intent.getStringExtra("post_id");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts").child(post_id);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                title.setText(post.title);
                description.setText(post.description);
                like_count.setText(String.valueOf(post.choice_one_counter));
                dislike_count.setText(String.valueOf(post.choice_two_counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}