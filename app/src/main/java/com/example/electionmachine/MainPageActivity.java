package com.example.electionmachine;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainPageActivity extends AppCompatActivity {
    /**
     * Активность для перехода в другие активности
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //Intent i = new Intent(this,BlockGenerationService.class);
        //startService(i);
    }

    //Открытие активности голосования
    public void openElectionActivity(View view) {
        Intent i = new Intent(this,ListofInitiativesActivity.class);
        //Отправка переменной в активность для определения того, какая активность будет следующей
        i.putExtra("Activity", "Elect");
        startActivity(i);
    }

    //Открытие активности создания голосования
    public void createElection(View view) {
        Intent i = new Intent(this,InitiativeCreationActivity.class);
        startActivity(i);
    }

    //Открытие активности настроек приложения
    public void openConfigs(View view) {
        startActivity(new Intent(this,ConfigActivity.class));
    }

    //Открытие активности статистики
    public void openDiagramActivity(View view) {
        Intent i = new Intent(this,ListofInitiativesActivity.class);
        //Отправка переменной в активность для определения того, какая активность будет следующей
        i.putExtra("Activity", "Diagram");
        startActivity(i);
    }
}
