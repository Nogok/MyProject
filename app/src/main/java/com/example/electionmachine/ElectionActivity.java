package com.example.electionmachine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ElectionActivity extends AppCompatActivity {
    /**
     * Активность работы с голосованием. Получение с сервера,
     * создание удобного интерфейся дл пользователя в выборе голоса,
     * отправка данных. Сейчас всё на кнопках, позже TODO переделать на автоматические запросы.
     * */

    private static final String baseUrl = "https://secure-beyond-82089.herokuapp.com"; // Ссылка на сервер. Пока одна, позже TODO List<String>
    ElectionService service; //Сервис запросов
    RadioGroup radioGroup; // Радиокнопки для голосования
    TextView textView; //Текстовое поле для отслеживания запросов
    Response<List<Initiative>> response; //Ответ от сервера -- список инициатив
    int VoteFromUser; //Голос от пользователя, номер варианта ответа
    List<Initiative> list; //Список инициатив
    DigitalSign ds; //Класс создания цифровой подписи
    KeyPair kp; // Пара ключей "Публичный" + "Приватный"
    Initiative initiative; //Инициатива, с которой идёт работа
    Vote vote; // Голос, с которым идёт работа

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);
        textView = (TextView) findViewById(R.id.tv);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ElectionActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        try {
             ds = new DigitalSign();
            //Создание пары
             kp = DigitalSign.DSA.generateKeyPair((long) 17);
            Toast.makeText(this, "Pair Created", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(this, "Pair is not created!", Toast.LENGTH_SHORT).show();
            Log.e("KEY_ERROR", e.toString());
        }

    }

    public void request(View view) {
        new MyAsyncTask().execute("");

    }
    //Создание инициативы и вывод её на экран
    public void createElection(View view) {
        list = response.body();
        initiative = list.get(list.size()-1);
        for(int i = 0; i < initiative.variants.length; i++) {
            RadioButton r = new RadioButton(this);
            r.setText(initiative.variants[i]);
            radioGroup.addView(r);
        }

    }
    // Голосование. Создание голоса, отправка его на сервер
    public void Votation(View view) {
        //Получение голоса от пользователя
        int n = radioGroup.getCheckedRadioButtonId();
        RadioButton r = (RadioButton)findViewById(n);
        String variant = r.getText().toString();
        for(int i = 0; i < initiative.variants.length; i++){
            if (variant.equals(initiative.variants[i])){
                //Берём номер голоса
                VoteFromUser = i+1;
            }
        }
        try {
            //Создание подписи
            byte[] buff = kp.getPublic().getEncoded();
            vote = new Vote(initiative,VoteFromUser, Base64.encodeToString(buff,Base64.DEFAULT));
            Gson gson = new Gson();
            String s = gson.toJson(vote);
            buff = DigitalSign.DSA.signData(s.getBytes(), kp.getPrivate());
            //Прикрепление подписи к голосу
            vote.dsaSign = Base64.encodeToString(buff,Base64.DEFAULT).toString();
            Call<Void> call = service.createVote(vote);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    Toast.makeText(ElectionActivity.this,"DONE!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(ElectionActivity.this,"FAIL!", Toast.LENGTH_SHORT).show();
                }});
        }
        catch (Exception e){
            Toast.makeText(this, "Exception cought!", Toast.LENGTH_SHORT).show();
        }
    }


    //Асинхронный запрос к серверу. Получение инициативы
     class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Call<List<Initiative>> call = service.getAllInitives();
                response = call.execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText("Данные получены!");
        }
    }
}