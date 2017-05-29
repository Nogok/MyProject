package com.example.electionmachine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Вячеслав on 29.05.2017.
 */

public class BlockGenerationService extends Service {

    String goal = "00007ffaff3939fbca4eb074249dc7d39b1d1ee4fed2da3f87430703cac5d250a";
    ElectionService service;
    private boolean b = true;
    List<Vote> l = new ArrayList<>();
    Call<List<Vote>> call;

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

                while(b) {

                    try {
                        call = service.getVotes();
                        Response<List<Vote>> response = call.execute();
                        l = response.body();
                        Log.e("BROADCAST", "WE HAVE VOTES!!");

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (!l.isEmpty()) {
                        Block block = new Block(l.get(l.size()-1));
                        Log.e("BROADCAST BLOCK", block.toString());
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
                        b = false;
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
    }
}
