package com.example.onlinevotingsystem;

public class Post {

    public String creator_id, title, description, choice_one, choice_two;
    public int choice_one_counter;
    public int choice_two_counter;
    public int post_id;
    public Post()
    {
        //Default Constructor
    }
    
    public Post(int post_id, String creator_id, String title,String description, String choice_one, String choice_two)
    {
        this.post_id = post_id;
        this.creator_id = creator_id;
        this.title = title;
        this.description = description;
        this.choice_one = choice_one;
        this.choice_two = choice_two;
        this.choice_one_counter = 0;
        this.choice_two_counter = 0;
    }



    public String GetTitle(){
        return title;
    }
    @Override public String toString()
    { return title ;
    }
}
