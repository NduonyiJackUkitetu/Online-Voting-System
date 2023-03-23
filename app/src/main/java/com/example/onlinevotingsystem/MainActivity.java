package com.example.onlinevotingsystem;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference userRef;

    DatabaseReference postRef;
    Button logoutButton, addDataButton;
    TextView userName;
    FirebaseUser currUser;
    int numberOFTasks = 0;

    private List<Post> postList = new ArrayList<Post>();

    ArrayAdapter<Post> postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postAdapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList);
        ListView listView = (ListView) findViewById(R.id.ListViewPost);
        listView.setAdapter(postAdapter);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");


        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout_button);
        addDataButton = findViewById(R.id.write_data);
        userName = findViewById(R.id.user_name_text);
        currUser = auth.getCurrentUser();
        userRef = database.getReference("users").child(currUser.getUid());


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    userName.setText(("Hello " + user.fullName + "!"));
                }
                else
                {
                    Log.w("loadPost:onDataChange", "The data was not found");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w( "loadPost:onCancelled", error.toException());            }
        });


        if(currUser == null){
            //if no current user is found, then start the login activity
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            //userName.setText(currUser.getEmail());
            //userName.setText("Hello " + currUser.getEmail());
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        addDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PostAdd.class );
                startActivity(intent);
                finish();
                //EditText editText = findViewById(R.id.editTextPost);
                //String currentPostDescription = editText.getText().toString();
               // Post newPost = new Post(0,currUser.getUid(), currentPostDescription, currentPostDescription);
                //postAdapter.add(newPost);
               // StoreNewPostData(currUser.getUid(), currentPostDescription, currentPostDescription);
            }
        });



        postRef = database.getReference("posts");

        ArrayList<Post> postList = new ArrayList<>();

        ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList);

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postList.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });
        //postsRef.addValueEventListener(postsListener);

        listView.setAdapter(adapter);



    }


    //    public void addTask(View view) {
//
//        numberOFTasks++;
//        EditText editText = findViewById(R.id.editTextPost);
//        String currentPostDescription = editText.getText().toString();
//        Post newPost = new Post(0,currUser.getUid(), currentPostDescription, currentPostDescription);
//        postAdapter.add(newPost);
//
//        StoreNewPostData(currUser.getUid(), currentPostDescription, currentPostDescription);
//
//        userName.setText("kek");
//
//
//    }

}