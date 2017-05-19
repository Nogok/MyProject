package com.example.electionmachine;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ElectionService {

    @GET("/addvote/{candidate}/{userSign}")
    Call<Vote> createVote(@Path("candidate") String candidate,
                          @Path("userSign") String userSign);
    @GET("/election")
    Call<Election> getElection();
}
