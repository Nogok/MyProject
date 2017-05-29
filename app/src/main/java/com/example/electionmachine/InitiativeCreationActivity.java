package com.example.electionmachine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InitiativeCreationActivity extends AppCompatActivity {

    /**
     * Активность создания инициативы
     * Интерфейс для пользователя
     * */
    public static final String baseUrl = "https://secure-beyond-82089.herokuapp.com";
    EditText EdDescription, EdVariants; //Эдиттексты для Описания и вариантов. TODO Варианты добавлять по-другому, вдруг людям в названии пригодятся пробелы
    Initiative initiative; // Будущая инициатива
    ElectionService service; // Сервис запросов к серверу

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inition_creation);
        EdDescription = (EditText)findViewById(R.id.InDescription);
        EdVariants = (EditText)findViewById(R.id.InVariants);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InitiativeCreationActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
    }


    public void sendInitiative(View view) {
        String description = EdDescription.getText().toString();
        String[] variants = EdVariants.getText().toString().trim().split("\\s+");
        initiative = new Initiative(description,variants);
        Call<Void> call = service.addNewInitive(initiative);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response) {
                Toast.makeText(InitiativeCreationActivity.this, "Initiative send success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(InitiativeCreationActivity.this, "Fail send initiative", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
