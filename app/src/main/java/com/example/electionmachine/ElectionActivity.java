package com.example.electionmachine;

import android.content.SharedPreferences;
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
import java.security.PrivateKey;
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
     * создание удобного интерфейся для пользователя в выборе голоса,
     * отправка данных. TODO переделать интерфейс
     * */

    private static final String baseUrl = "https://cryptic-everglades-30040.herokuapp.com"; // Ссылка на сервер. Пока одна, позже TODO List<String>
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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);
        textView = (TextView)findViewById(R.id.textView3);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ElectionActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        sharedPreferences = getSharedPreferences("Keys",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initiative = gson.fromJson(getIntent().getStringExtra("Initiative"),Initiative.class);
        textView.setText(initiative.description);
        for(int i = 0; i < initiative.variants.length; i++) {
            RadioButton r = new RadioButton(this);
            r.setText(initiative.variants[i]);
            radioGroup.addView(r);
        }


    }


    // Голосование. Создание голоса, отправка его на сервер
    public void Votation(View view) {
        if (!(sharedPreferences.contains("Private_key") || sharedPreferences.contains("Public_key"))) {
            try {
                //Создание пары
                kp = DigitalSign.generateKeyPair((long) 17);
                editor.putString("Private_key",Base64.encodeToString(kp.getPrivate().getEncoded(),Base64.DEFAULT));
                editor.putString("Public_key",Base64.encodeToString(kp.getPublic().getEncoded(),Base64.DEFAULT));
                editor.commit();
                Toast.makeText(this, "Pair Created", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Pair is not created!", Toast.LENGTH_SHORT).show();
                Log.e("KEY_ERROR", e.toString());
            }
        }

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

            vote = new Vote(initiative,VoteFromUser, sharedPreferences.getString("Public_key", null));
            Gson gson = new Gson();
            String s = gson.toJson(vote);
            Log.e("Vote", s);
            String sp = sharedPreferences.getString("Private_key", null);
            Log.e("Private key is ", sp);
            byte[] priv=Base64.decode(sp,Base64.DEFAULT);
            Log.e("PrivateKey: ", "is got");
            PrivateKey privkey =  DigitalSign.convertPrivateKey(priv);
            Log.e("PrivateKey: ", "is converted");
            byte[] buff = DigitalSign.signData(s.getBytes(), privkey);
            //Прикрепление подписи к голосу
            vote.dsaSign = Base64.encodeToString(buff,Base64.DEFAULT).toString();
            Log.e("Signature", " is created");
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
            Log.e("Vote ", "is sent");
        }
        catch (Exception e){
            Toast.makeText(this, "Exception caught!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



}