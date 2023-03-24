package com.example.onlinevotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    Button back_button_vote;

    int likes, dislikes;


    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference Ref;


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
        back_button_vote = findViewById(R.id.backPostButtonT);

        post_id = intent.getStringExtra("post_id");
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

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ButtonPress(1);

            }
        });
        dislike_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            ButtonPress(2);

            }
        });

        back_button_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    public void ButtonPress(int choice)
    {
        String text = "";
        if (choice == 1)
        {
            text = "choice_one_counter";
        }
        else if (choice == 2)
        {
            text = "choice_two_counter";
        }
        String finalText = text;
        Ref = myRef.child(text);
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int likes = Integer.valueOf(snapshot.getValue().toString());
                likes++;
                Ref.setValue(likes);
            }@Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}