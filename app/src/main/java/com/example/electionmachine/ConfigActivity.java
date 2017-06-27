package com.example.electionmachine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {

    /**
     * Активность настроек.
     * В данный момент есть только одна настройка(основная): включение и отключение автоматической генерации
     * блоков. TODO HPS
     * */

    Button buttonForService; //Выключение и включение генерации блоков
    TextView BlockGenerationStatus; //Для отображения статуса генерации блоков (идёт она или нет)
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String blockGenerationEnabled = "Генерация блоков включена!";
    String blockGenerationDisabled = "Генерация блоков отключена!";
    String enableBlockGeneration = "Включить генерацию блоков";
    String disableBlockGeneration = "Выключить генерацию блоков";
    String bool = "bool";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        buttonForService = (Button)findViewById(R.id.buttonForService);
        BlockGenerationStatus = (TextView)findViewById(R.id.blockGenerate);
        sharedPreferences = getSharedPreferences("boolForGenerating", MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean(bool,true);
        buttonForService.setText(b ? disableBlockGeneration: enableBlockGeneration);
        BlockGenerationStatus.setText(b ? blockGenerationEnabled:blockGenerationDisabled);
        editor = sharedPreferences.edit();
    }


    // Функция включения/выключения ресивера
    public void changeBlockGenerationStatus(View view) {
        // Получение нынешнего статуса ресивера (включен или выключен)
       String status = BlockGenerationStatus.getText().toString();
        // Если включён -- выключить и изменить текст на кнопке и TextView
        if (status.equals(blockGenerationEnabled)){
            BlockGenerationStatus.setText(blockGenerationDisabled);
            buttonForService.setText(enableBlockGeneration);
            stopService(new Intent(getApplicationContext(),BlockGenerationService.class));
            editor.putBoolean(bool,false);
            editor.commit();
        }
        // Если выключен -- включить и изменить текст на кнопке и TextView
        else if (status.equals(blockGenerationDisabled)){
            BlockGenerationStatus.setText(blockGenerationEnabled);
            buttonForService.setText(disableBlockGeneration);
            startService(new Intent(getApplicationContext(),BlockGenerationService.class));
            editor.putBoolean(bool,true);
            editor.commit();
        }


    }
}
