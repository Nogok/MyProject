package com.example.electionmachine;



import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ElectionService {

    /**
     * Интерфейс-сервис запросов к серверу
     * */

    //Отправка инициативы на сервер
    @POST("/addinitiative")
    Call<Void> addNewInitive(@Body Initiative initiative);


    //Получение списка иниициатив с сервера
    @POST("/getinitiatives")
    Call<List<Initiative>> getAllInitives();

    //Отправка голоса
    @POST("/addvote")
    Call<Void> createVote(@Body Vote vote);

    //Получение блока
    @GET("/getBlock")
    Call<Block> getBlock();

    //Получение списка голосов
    @GET("/getvotes")
    Call<List<Vote>> getVotes();


    //Получение числа для проверки blockhash < goal
    @GET("/getgoal")
    Call<ResponseBody> getGoal();

    @POST("/addblock")
    Call<Void> addBlock(@Body Block block);

    @POST("/getlistofvotes")
    Call<List<Vote>> getListOfVotes(@Body Initiative initiative);

    @GET("/getinitiativebydescriprion/{description}")
    Call<Initiative> getInitiativeByDescription(@Path("description") String description);


}
