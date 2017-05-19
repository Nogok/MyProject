package com.example.electionmachine;


import java.util.List;
import java.util.ArrayList;


public class Election{

    List<Elector> candidates;
    Vote vote;

    public Election(){
        candidates = new ArrayList<Elector>();
    }



    public List<Elector> getCandidates() {
        return candidates;
    }



    @Override
    public String toString() {
        String str="";
        for (Elector e:candidates)
            str+=e+"\n";
        return str;
    }

}
