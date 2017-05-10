package com.example.electionmachine;



public class Vote {
    public final String candidate;
    private final String userSignature;
    public Vote(String candidate, String userSignature){
        this.candidate = candidate;
        this.userSignature = userSignature;
    }

    @Override
    public String toString() {
        return "Голос за "+candidate;
    }
}
