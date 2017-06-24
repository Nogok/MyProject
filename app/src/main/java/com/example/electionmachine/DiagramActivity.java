package com.example.electionmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
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
     * */

    ElectionService service; // Сервис для запросов к серверу
    Call<List<Vote>> call;
    List<Vote> voteList = new ArrayList<>(); //Список голосов для вычисления статистики
    String initiativeJSON; //JSON строка инициативы из активности со списком
    Initiative initiative; //Инициатива, по которой получаем голоса
    ArrayList<Integer> notRepeatedVariants = new ArrayList<>();
    int[] variants;
    PieChart pieChart;
    LinearLayout layoutForDiagram;
    LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);
        layoutForDiagram = (LinearLayout) findViewById(R.id.layoutForDiagram);
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
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }


    public void createCircle(View view) {
        layoutForDiagram.removeAllViews();
        pieChart = new PieChart(this);
        variants = new int[voteList.size()];
        for(int i = 0; i < voteList.size(); i++){
            variants[i] = voteList.get(i).variant;
        }
        // Получение вариантов голосования

        for(int i:variants){
            if(!notRepeatedVariants.contains(i))
                notRepeatedVariants.add(i);
        }

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i < notRepeatedVariants.size(); i++){
            float a = 0;
            for(int j :variants){
                if (notRepeatedVariants.get(i) == j) a++;
            }
            entries.add(new Entry(a,i));
           // label.add(notRepeatedVariants.get(i)+"");
        }
        Collections.addAll(label,initiative.variants);
        PieDataSet dataSet = new PieDataSet(entries, "Votes");
        PieData data = new PieData(label, dataSet);
        dataSet.setValueTextSize(20);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setLayoutParams(linLayoutParam);
        pieChart.setData(data);
        pieChart.setDescriptionTextSize(30);
        pieChart.setDescription(initiative.description);
        layoutForDiagram.addView(pieChart);
    }

    public void createRows(View view) {
        layoutForDiagram.removeAllViews();
        BarChart barChart = new BarChart(this);
        variants = new int[voteList.size()];
        for(int i = 0; i < voteList.size(); i++){
            variants[i] = voteList.get(i).variant;
        }
        // Получение вариантов голосования

        for(int i:variants){
            if(!notRepeatedVariants.contains(i))
                notRepeatedVariants.add(i);
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i < notRepeatedVariants.size(); i++){
            float a = 0;
            for(int j :variants){
                if (notRepeatedVariants.get(i) == j) a++;
            }
            entries.add(new BarEntry(a,i));
            label.add(notRepeatedVariants.get(i)+"");
        }
        BarDataSet dataSet = new BarDataSet(entries,"Votes");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(label,dataSet);
        barChart.setLayoutParams(linLayoutParam);
        barChart.setData(data);
        barChart.setDescription(initiative.description);
        layoutForDiagram.addView(barChart);


    }
}
