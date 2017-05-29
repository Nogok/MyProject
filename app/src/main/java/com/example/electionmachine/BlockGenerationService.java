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
    Call<List<Vote>> call; //Запрос для списка голосов
    Call<ResponseBody> callGoal; //Запрос цели
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
                        Response<List<Vote>> response = call.execute();
                        l = response.body();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Если какие-то голоса есть, то...
                    if (!l.isEmpty()) {
                        Log.e("BROADCAST", "WE HAVE VOTES!!");
                        Block block = new Block(l.get(l.size()-1));
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
                        Log.e("BROADCAST", "BLOCK IS READY!!!");


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
