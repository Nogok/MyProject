package com.example.electionmachine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {

    /**
     * Активность настроек.
     * */

    CheckBox checkBoxForBlockGeneration; //Выключение и включение генерации блоков
    TextView BlockGenerationStatus; //Для отображения статуса генерации блоков (идёт она или нет)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        checkBoxForBlockGeneration = (CheckBox)findViewById(R.id.checkBox);
        BlockGenerationStatus = (TextView)findViewById(R.id.blockGenerate);
    }


    public void changeBlockGenerationStatus(View view) {
        if (checkBoxForBlockGeneration.isChecked()){
            stopService(new Intent(this,BlockGenerationService.class));
            BlockGenerationStatus.setText("Генерация блоков отключена!");
        }
        else {
            startService(new Intent(this,BlockGenerationService.class));
            BlockGenerationStatus.setText("Генерация блоков включена!");
        }
    }
}
