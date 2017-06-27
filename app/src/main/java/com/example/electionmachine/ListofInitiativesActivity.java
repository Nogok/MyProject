package com.example.electionmachine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListofInitiativesActivity extends AppCompatActivity {

    ElectionService service;
    String[] descriptionsOfInitiatives; // массив описания инициатив
    Call<List<Initiative>> listCall; // Запрос на получение инициатив
    List<Initiative> initiatives = new ArrayList<>(); //масств инициатив
    Initiative initiativeForDiagram; // Инициатива для передачи в интент
    Intent i; // Интент для перехода на новую активность
    String activity; // Название активности, куда будет отправлять интент
    ListView listView; //Список голосов (интерфейс)
    TextView textView; // Текствью для статуса
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getIntent().getStringExtra("Activity");
        // Настройка интента
        if (activity.equals("Diagram")) {
            i = new Intent(this, DiagramActivity.class);
        }
        else if (activity.equals("Elect")){
            i = new Intent(this,ElectionActivity.class);
        }
        setContentView(R.layout.activity_listof_initiatives);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InitiativeCreationActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        listView = (ListView)findViewById(R.id.listOfInitiative);
        // Запрос на получение инициатив
        listCall = service.getAllInitives();
        listCall.enqueue(new Callback<List<Initiative>>() {
            @Override
            public void onResponse(Response<List<Initiative>> response) {
                // Получили инициативы
                initiatives = response.body();
                Log.e("INITIATIVES", initiatives.size()+"");
                if (initiatives.size() == 0){
                    textView = (TextView)findViewById(R.id.statusOfInit);
                    textView.setText("Нет доступных голосований.");
                }
                descriptionsOfInitiatives = new String[initiatives.size()];
                // Заполнение массива описаний
                for (int i = 0; i < initiatives.size(); i++){
                    descriptionsOfInitiatives[i] = initiatives.get(i).name;
                }
                // Создание адаптера для элемента списка
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ListofInitiativesActivity.this,android.R.layout.simple_list_item_1, descriptionsOfInitiatives);
                listView.setAdapter(arrayAdapter);
                // Обработка нажатия на элемент списка
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Получение инициативы для отправки
                            String result = ((TextView) view).getText().toString();
                            for (int i = 0; i < initiatives.size(); i++) {
                                if (initiatives.get(i).name.equals(result)) {
                                    initiativeForDiagram = initiatives.get(i);
                                    break;
                                }
                            }
                            // Присваивание в интент строки инициативы
                            Gson gson = new Gson();
                            String initiativeJSON = gson.toJson(initiativeForDiagram);
                            i.putExtra("Initiative", initiativeJSON);
                            startActivity(i);
                        }

                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });




    }

}
