package com.example.electionmachine;

public class Vote {

    public Initiative initiative;
    public int variant;
    public String publicKey;
    public String dsaSign;

    public Vote(){

    }
    public Vote(Initiative initiative,int variant,String publicKey){
        this.initiative=initiative;
        this.variant=variant;
        this.publicKey=publicKey;
    }
}
