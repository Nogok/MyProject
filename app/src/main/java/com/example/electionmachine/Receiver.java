package com.example.electionmachine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

    /**
     * Собственно, сам класс Ресивера
     * */

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("boolForGenerating", context.MODE_PRIVATE);
        boolean isGenerating = sharedPreferences.getBoolean("bool",true);
        if(isGenerating) context.startService(new Intent(context,BlockGenerationService.class));

    }
}
