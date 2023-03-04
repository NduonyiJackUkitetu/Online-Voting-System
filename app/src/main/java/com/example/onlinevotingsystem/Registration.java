package com.example.onlinevotingsystem;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.onlinevotingsystem.databinding.ActivityRegistrationBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRegistrationBinding binding;

    TextInputEditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName;
    TextView goToLogin;
    Button registerButton;
    RadioButton userRB, managerRB;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    String userRole, fullName;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextFirstName = findViewById(R.id.first_name);
        editTextLastName = findViewById(R.id.last_name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        userRB = findViewById(R.id.user_radio_button);
        managerRB = findViewById(R.id.manager_radio_button);
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progressBar);
        goToLogin = findViewById(R.id.goToLogin);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userRole = "Voter";


        userRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRole = "Voter";
            }
        });

        managerRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRole = "Manager";
            }
        });


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                fullName = editTextFirstName.getText() + " " + editTextLastName.getText();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Registration.this, "Email field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Registration.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    assert user != null;
                                    storeNewUserData(user.getUid(), fullName, email, userRole);
                                    Toast.makeText(Registration.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Registration.this, "Account creation failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void storeNewUserData(String userId, String fullName, String email, String userRole) {
            User user = new User(email, fullName, userRole);
            DatabaseReference myRef = mDatabase.getReference("users");
            myRef.child(userId).setValue(user);
        }
}