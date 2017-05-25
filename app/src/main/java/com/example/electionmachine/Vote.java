package com.example.electionmachine;

public class Vote {
    public final String candidate;

    public Vote(String candidate ){
        this.candidate = candidate;

    }

    @Override
    public String toString() {
        return "Голос за "+candidate;
    }
}
