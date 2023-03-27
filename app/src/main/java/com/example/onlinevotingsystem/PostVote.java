package com.example.onlinevotingsystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PostVote extends AppCompatActivity {
    TextView title;
    TextView description;
    TextView like_count;
    TextView dislike_count, voted_already, option_one_text, option_two_text;
    String post_id;

    PieChart pieChart;

    Button back_button_vote, like_button, dislike_button;

    int like_C, dislike_C;

    int likes, dislikes;


    FirebaseAuth auth;
    FirebaseUser currUser;
    FirebaseDatabase database;
    DatabaseReference myRef, voterRef;
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
        voted_already = findViewById(R.id.voted_already);
        option_one_text =findViewById(R.id.option_one_text);
        option_two_text = findViewById(R.id.option_two_text);

        post_id = intent.getStringExtra("post_id");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts").child(post_id);
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser();

        voterRef = myRef.child("voters").child("voter_list");

        voterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> voterList = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()){
                    String voter_id = ds.getValue(String.class);
                    voterList.add(voter_id);
                    if(Objects.equals(voter_id, currUser.getUid())){
                        like_button.setVisibility(View.GONE);
                        dislike_button.setVisibility(View.GONE);
                        voted_already.setVisibility(View.VISIBLE);
                        option_one_text.setVisibility(View.VISIBLE);
                        option_two_text.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                title.setText(post.title);
                description.setText(post.description);
                like_count.setText(String.valueOf(post.choice_one_counter));
                dislike_count.setText(String.valueOf(post.choice_two_counter));
                like_button.setText(String.valueOf(post.choice_one));
                dislike_button.setText(String.valueOf(post.choice_two));

                String op1= String.valueOf(post.choice_one);
                String op2= String.valueOf(post.choice_two);

                option_one_text.setText(op1);
                option_two_text.setText(op2);

                int like_C = Integer.parseInt(like_count.getText().toString());
                int dislike_C = Integer.parseInt(dislike_count.getText().toString());

                //just added a pie chart to make it look cool
                pieChart= findViewById(R.id.pieChart);

                ArrayList<PieEntry> figure= new ArrayList<>();
                figure.add(new PieEntry(like_C,op1));
                figure.add(new PieEntry(dislike_C,op2));

                PieDataSet pieDataSet= new PieDataSet(figure, "votes");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData= new PieData(pieDataSet);

                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Votes");
                pieChart.animateY(2000);
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
        DatabaseReference voterRef = myRef.child("voters").child("voter_list");
        voterRef.push().setValue(currUser.getUid());

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



