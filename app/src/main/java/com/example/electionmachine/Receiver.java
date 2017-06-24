package com.example.electionmachine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class Receiver extends BroadcastReceiver {

    /**
     * Собственно, сам класс Ресивера
     * */

    @Override
    public void onReceive(Context context, Intent intent) {
        int a = context.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences("boolForGenerating", a);
        boolean isGenerating = sharedPreferences.getBoolean("bool",true);
        if(isGenerating) context.startService(new Intent(context,BlockGenerationService.class));

    }
}
