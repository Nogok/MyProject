package com.example.electionmachine;

public class Vote {

    public String dsaSign;
    public Initiative initiative;
    public String publicKey;
    public int variant;

    public Vote(){

    }
    public Vote(Initiative initiative,int variant,String publicKey){
        this.initiative=initiative;
        this.variant=variant;
        this.publicKey=publicKey;
    }
}
