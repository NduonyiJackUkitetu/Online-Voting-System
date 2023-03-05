package com.example.onlinevotingsystem;

public class Post {

    public String creator_id, title, description, voting_choice;
    public String positive_choice_counter, negative_choice_counter;

    public Post()
    {
        //Default Constructor
    }
    
    public Post(String creator_id, String title,String description)
    {
        this.creator_id = creator_id;
        this.title = title;
        this.description = description;
    }

}
