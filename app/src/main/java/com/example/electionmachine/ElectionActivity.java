package com.example.electionmachine;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.security.KeyPair;
import java.security.PrivateKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ElectionActivity extends AppCompatActivity {
    /*
     * Активность работы с голосованием. Получение с сервера,
     * создание удобного интерфейся для пользователя в выборе голоса,
     * отправка данных.
     * */

    private static final String baseUrl = "https://cryptic-everglades-30040.herokuapp.com"; // Ссылка на сервер. Пока одна, позже TODO List<String>
    ElectionService service; //Сервис запросов
    RadioGroup radioGroup; // Радиокнопки для голосования
    TextView textView,descriptionTextView; //Текстовое поле для отслеживания запросов
    int VoteFromUser; //Голос от пользователя, номер варианта ответа
    KeyPair kp; // Пара ключей "Публичный" + "Приватный"
    Initiative initiative; //Инициатива, с которой идёт работа
    Vote vote; // Голос, с которым идёт работа
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText editTextForPass; // Поле EditText для получения пароля от пользователя
    Gson gson = new Gson();
    TripleDes tripleDes; // Оболочка для шифрования
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);
        textView = (TextView)findViewById(R.id.textView3);
        editTextForPass = (EditText) findViewById(R.id.password);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        descriptionTextView = (TextView)findViewById(R.id.descriptionOfInit);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ElectionActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        sharedPreferences = getSharedPreferences("Keys",MODE_PRIVATE);
        // Получение инициативы из предыдущей активности (ListofInitiativesActivity)
        initiative = gson.fromJson(getIntent().getStringExtra("Initiative"),Initiative.class);
        textView.setText(initiative.name); // Установка названия
        // Установка вариантов голосования
        for(int i = 0; i < initiative.variants.length; i++) {
            RadioButton r = new RadioButton(this);
            r.setText(initiative.variants[i]);
            radioGroup.addView(r);
        }
        // Установка описания
        descriptionTextView.setText(initiative.description);
    }


    // Голосование. Создание голоса, отправка его на сервер
    public void Votation(View view){
        editor = sharedPreferences.edit();
        String password = editTextForPass.getText().toString();
        //Проверка на наличие ключей. Если нет -- создаются
        if (!(sharedPreferences.contains("Private_Key") || sharedPreferences.contains("Public_Key"))) {
            try {
                //Создание пары
                kp = DigitalSign.generateKeyPair((long) 17);
                tripleDes = new TripleDes(password);
                editor.putString("Private_Key",tripleDes.encrypt(Base64.encodeToString(kp.getPrivate().getEncoded(),Base64.DEFAULT)));
                editor.putString("Public_Key",tripleDes.encrypt(Base64.encodeToString(kp.getPublic().getEncoded(),Base64.DEFAULT)));
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
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

            //Создание подписи и голоса
            tripleDes = new TripleDes(password);
            vote = new Vote();
            vote = new Vote(initiative,VoteFromUser, tripleDes.decrypt(sharedPreferences.getString("Public_Key", null)));
            String s = gson.toJson(vote);
            String sp = tripleDes.decrypt(sharedPreferences.getString("Private_Key", null));
            byte[] privateArr = Base64.decode(sp,Base64.DEFAULT);
            PrivateKey privateKey =  DigitalSign.convertPrivateKey(privateArr);
            byte[] buff = DigitalSign.signData(s.getBytes(), privateKey);
            //Прикрепление подписи к голосу
            vote.dsaSign = Base64.encodeToString(buff,Base64.DEFAULT);
            Call<Void> call = service.createVote(vote);
            call.enqueue(new Callback<Void>() {
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
            e.printStackTrace();
        }
    }



}