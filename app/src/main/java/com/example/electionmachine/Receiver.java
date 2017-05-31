package com.example.electionmachine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Вячеслав on 29.05.2017.
 */

public class Receiver extends BroadcastReceiver {

    /**
     * Собственно, сам класс Ресивера
     * */

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context,BlockGenerationService.class));

    }
}
