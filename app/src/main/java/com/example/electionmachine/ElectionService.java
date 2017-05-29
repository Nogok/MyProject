package com.example.electionmachine;


import java.math.BigInteger;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ElectionService {

    @POST("/addinitiative")

    Call<Void> addNewInitive(@Body Initiative initiative);
    @POST("/getinitiatives")
    Call<List<Initiative>> getAllInitives();

    @POST("/addvote")
    Call<Void> createVote(@Body Vote vote);
    @GET("/election")
    Call<Election> getElection();

//    @GET("/publickey/{publickey}/{s}/{r}/{q}/{p}/{g}")
//    Call<MyDSA> sendDSA(@Path("publickey") BigInteger y,
//                        @Path("s") BigInteger s,
//                        @Path("r") BigInteger r,
//                        @Path("q") BigInteger q,
//                        @Path("p") BigInteger p,
//                        @Path("g") BigInteger g);

    @GET("/getBlock")
    Call<Block> getBlock();

    @GET("/getvotes")
    Call<List<Vote>> getVotes();
}
