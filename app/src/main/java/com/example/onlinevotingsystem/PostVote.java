package com.example.onlinevotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostVote extends AppCompatActivity {
    TextView title;
    TextView description;
    ImageButton like_button;
    ImageButton dislike_button;
    TextView like_count;
    TextView dislike_count;
    String post_id;

    int likes, dislikes;


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
        like_button = findViewById(R.id.likeButtonPostVote);
        dislike_button = findViewById(R.id.dislikeButtonPostVote);

        post_id =  intent.getStringExtra("post_id");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts").child(post_id);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                title.setText(post.title);
                description.setText(post.description);
                like_count.setText(String.valueOf(post.positive_choice_counter));
                dislike_count.setText(String.valueOf(post.negative_choice_counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef.child("positive_choice_counter").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int likes = Integer.valueOf(snapshot.getValue().toString());
                        likes++;
                        myRef.child("positive_choice_counter").setValue(likes);
                    }@Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        dislike_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef.child("negative_choice_counter").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int dislikes = Integer.valueOf(snapshot.getValue().toString());
                        dislikes++;
                        myRef.child("negative_choice_counter").setValue(dislikes);
                    }@Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



    }
}