package com.example.onlinevotingsystem;

import java.io.Serializable;
import java.util.ArrayList;

public class Voters{
    public ArrayList<String> voter_list;

    public Voters(){
        //Default Constructor
    }
    public Voters(ArrayList<String> voter_list){
        this.voter_list = voter_list;
    }


}
