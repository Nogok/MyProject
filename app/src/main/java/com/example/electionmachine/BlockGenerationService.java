package com.example.electionmachine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Вячеслав on 29.05.2017.
 */

public class BlockGenerationService extends Service {

    /**
    * Сервис Бродкаста, позхволяющий в фоновом режиме реализовать генерацию блока
    *
    * */
    String goal; // Переменная для условия генерации блока blockhash < goal. Получаем с сервера
    ElectionService service; //Сервис для запросов на сервер
    private boolean b = true; // Переменная для цикла генерации внутри дополнительного потока
    List<Vote> l = new ArrayList<>(); //Список голосов
    Call<ResponseBody> call; //Запрос для списка голосов
    Call<ResponseBody> callGoal; //Запрос цели
    Call<Block> blockCall;
    Block CreatingBlock = new Block();
    String voteHash = "";
    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InitiativeCreationActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //Получаем цель
                try {
                    callGoal = service.getGoal();
                    goal = callGoal.execute().body().string();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                //Цикл сбора данных для генерации
                while(b) {
                    //Получение списка голосов
                    try {
                        call = service.getVotes();
                        Log.e("BROADCAST", "VOTES ARE GOT");
                        blockCall = service.getBlock();
                        Log.e("BROADCAST", "Block are got");
                        Response<Block> blockResponse = blockCall.execute();
                        Log.e("BROADCAST","Block executed");
                        voteHash = call.execute().body().string();
                        Log.e("BROADCAST","VoteHash executed");
                        CreatingBlock = blockResponse.body();


                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("BROADCAST", "PROBLEM WITH VOTES");
                    }
                    //Если какие-то голоса есть, то генерируем из них блок
                    if (!voteHash.equals("")) {
                        Block block = new Block(voteHash,CreatingBlock);
                        //Цикл генерации самого блока
                        while (block.getHash().compareTo(goal) > 0) {
                            block.hashcode();
                            block.nonce++;
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Call<Void> blockCall = service.addBlock(block);
                        blockCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Response<Void> response) {
                                Log.e("BROADCAST", "BLOCK IS SENT!!");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.e("BROADCAST", "Something happend");
                            }
                        });
                        Log.e("BROADCAST", "BLOCK IS SENT");
                    }
                    else
                    {
                        /**
                         *Если же список голосов с сервера пуст
                         * (т.е. нет голосов, которые можно использовать для создания блока
                         * то 5 минут к серверу не обращаемся, ждём, вдруг там соберётся нужное количество голосов.
                         **/
                        Log.e("BROADCAST", "WE DON'T HAVE VOTES");
                        try {
                            Thread.sleep(300000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        });
        t.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Останавливаем цикл в дополнительном потоке.
        b = false;
    }
}
