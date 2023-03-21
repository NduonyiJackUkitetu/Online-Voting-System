package com.example.onlinevotingsystem;

public class Post {

    public String creator_id, title, description, voting_choice;
    public int positive_choice_counter;
    public int negative_choice_counter;
    public int post_id;
    public Post()
    {
        //Default Constructor
    }
    
    public Post(int post_id, String creator_id, String title,String description)
    {
        this.post_id = post_id;
        this.creator_id = creator_id;
        this.title = title;
        this.description = description;
        this.positive_choice_counter = 0;
        this.negative_choice_counter = 0;
    }



    public String GetTitle(){
        return title;
    }
    @Override public String toString()
    { return title ;
    }
}
