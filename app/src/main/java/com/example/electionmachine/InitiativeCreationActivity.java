package com.example.electionmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
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
    Integer[] amountOfCandidates = {2,3,4,5,6,7,8}; //Массив для выпадающего списка (количество кандидатов, которое выбирает пользователь
    public static final String baseUrl = "https://cryptic-everglades-30040.herokuapp.com";
    EditText EdDescription, EdName; //Эдиттекст для описания.
    Initiative initiative; // Будущая инициатива
    ElectionService service; // Сервис запросов к серверу
    LinearLayout layoutForCandidates; // Layout для edittext'ов для получения информации о кандидатах.

    ArrayList<EditText> editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inition_creation);
        EdDescription = (EditText)findViewById(R.id.InDescription);
        EdName = (EditText)findViewById(R.id.nameET);
        layoutForCandidates = (LinearLayout) findViewById(R.id.layoutForCandidates);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amountOfCandidates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InitiativeCreationActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        Listener listener = new Listener();
        spinner.setOnItemSelectedListener(listener);
    }

    //Отправка инициативы на сервер
    public void sendInitiative(View view) {
        String name = EdName.getText().toString();
        String description = EdDescription.getText().toString();
        String[] variants = new String[editTexts.size()];
        for (int i = 0; i < editTexts.size(); i++){
            variants[i] = editTexts.get(i).getText().toString();
        }
        initiative = new Initiative(name,description,variants);
        Call<Void> call = service.addNewInitive(initiative);
        call.enqueue(new Callback<Void>() {
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

    private class Listener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            createViewForCandidates(position+2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void createViewForCandidates(int amount){
        layoutForCandidates.removeAllViews();
        editTexts = new ArrayList<>();
        for (int i = 0; i < amount; i++ ){
            TextView textViewForCandidates = new TextView(this);
            String s = "Кандидат №"+(i+1);
            textViewForCandidates.setText(s);
            EditText ed = new EditText(this);
            editTexts.add(ed);
            layoutForCandidates.addView(textViewForCandidates);
            layoutForCandidates.addView(ed);
        }

    }

}
