package com.example.onlinevotingsystem;

public class User {

    public String email, id, fullName, role;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String email, String fullName, String role){
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

}


//creatorID
//Title
//Description
//VotingChoice1 and VotingChoice2
//Choice1Counter and Choice2Counter
// List of IDS that have voted