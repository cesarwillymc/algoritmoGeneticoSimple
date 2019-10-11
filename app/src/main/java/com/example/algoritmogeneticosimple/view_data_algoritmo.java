package com.example.algoritmogeneticosimple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class view_data_algoritmo extends AppCompatActivity {
    private ArrayList<String> valor;
    private ArrayList<String> newValorStringNumber;
    @BindView(R.id.list_item)
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_algoritmo);
        ButterKnife.bind(this);
        newValorStringNumber=new ArrayList<>();
         valor = getIntent().getExtras().getStringArrayList("data");
         for(int i=0;i<valor.size();i++){
            newValorStringNumber.add( valor.get(i)+" : "+Integer.parseInt(valor.get(i),2));
         }
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, newValorStringNumber);
        listView.setAdapter(arrayAdapter);

    }
}
