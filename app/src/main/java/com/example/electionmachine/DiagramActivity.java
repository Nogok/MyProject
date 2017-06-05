package com.example.electionmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DiagramActivity extends AppCompatActivity {

    /**
     * Активность создания диаграммы для отображения статистики
     * голосов за определённую инициативу.
     * В данный момент создаётся только круговая диаграмма
     * TODO добавить возможность создания столбчатой диаграммы
     * */

    ElectionService service; // Сервис для запросов к серверу
    Call<List<Vote>> call;
    List<Vote> voteList = new ArrayList<>(); //Список голосов для вычисления статистики
    String initiativeJSON; //JSON строка инициативы из активности со списком
    Initiative initiative; //Инициатива, по которой получаем голоса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);
        // Создание объекта retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InitiativeCreationActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        //Получение инициативы из прошлой активности
        initiativeJSON = getIntent().getStringExtra("Initiative");
        Gson gson = new Gson();
        initiative = gson.fromJson(initiativeJSON,Initiative.class);
        //Запрос на получение всех голосов за эту инициативу
        call = service.getListOfVotes(initiative);
        call.enqueue(new Callback<List<Vote>>() {
            @Override
            public void onResponse(Response<List<Vote>> response) {
                //Получение списка голосов за инициативу
                voteList = response.body();
                Log.e("VOTELIST", voteList.size()+"");
                int[] variants = new int[voteList.size()];
                for(int i = 0; i < voteList.size(); i++){
                    variants[i] = voteList.get(i).variant;
                }
                // Получение вариантов голосования (Из инициативы не так интересно :) )
                ArrayList<Integer> notRepeatedVariants = new ArrayList<>();
                for(int i = 0; i < variants.length; i++){
                    if(!notRepeatedVariants.contains(variants[i]))
                        notRepeatedVariants.add(variants[i]);
                }
                //Создание диаграммы
                PieChart pieChart = (PieChart) findViewById(R.id.chart);
                ArrayList<Entry> entries = new ArrayList<>();
                ArrayList<String> label = new ArrayList<>();
                for (int i = 0; i < notRepeatedVariants.size(); i++){
                    float a = 0;
                    for(int j = 0; j < variants.length; j ++){
                        if (notRepeatedVariants.get(i) == variants[j]) a++;
                    }
                    entries.add(new Entry(a,i));
                    label.add(notRepeatedVariants.get(i)+"");
                }
                Log.e("ENTRIES",""+entries.size());
                Log.e("LABLES",""+label.size());
                PieDataSet dataset = new PieDataSet(entries, "Votes");
                PieData data = new PieData(label, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                pieChart.setData(data);
                pieChart.setDescription(initiative.description);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });




    }


}
