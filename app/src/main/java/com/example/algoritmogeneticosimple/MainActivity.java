package com.example.algoritmogeneticosimple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int contador_individuos=0;
    int max;
    private String datos="";
    @BindView(R.id.cromosomas_button)
    Button cromosomas_button;
    @BindView(R.id.cromosomas_edit_text)
    EditText cromosomas_edit;
    @BindView(R.id.generacion_button)
    Button generacion_button;
    @BindView(R.id.generacion_edit_text)
    EditText generacion_edit;
    @BindView(R.id.indice_button)
    Button indice_button;
    @BindView(R.id.indice_edit_text)
    EditText indice_edit;
    @BindView(R.id.individuos_button)
    Button individuos_button;
    @BindView(R.id.individuos_edit_text)
    EditText individuos_edit;
    @BindView(R.id.poblacion_button)
    Button poblacion_button;
    @BindView(R.id.poblacion_edit_text)
    EditText poblacion_edit;
    @BindView(R.id.Progress_bar)
    ProgressBar progressBar;
    private int poblacion;
    private int generacion;
    private int cromosomas;
    private ArrayList<String> individuos;
    ArrayList<String> nexgeneration;
    private int indice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        iniciar_variables();
        poblacion_button.setOnClickListener(this);
        cromosomas_button.setOnClickListener(this);
        generacion_button.setOnClickListener(this);
        indice_button.setOnClickListener(this);
        individuos_button.setOnClickListener(this);
    }

    private void iniciar_variables() {
        poblacion_edit.setEnabled(true);
        poblacion_button.setEnabled(true);
        //
        cromosomas_button.setEnabled(false);
        cromosomas_edit.setEnabled(false);
        //
        generacion_button.setEnabled(false);
        generacion_edit.setEnabled(false);
        //
        indice_button.setEnabled(false);
        indice_edit.setEnabled(false);
        //
        individuos_button.setEnabled(false);
        individuos_edit.setEnabled(false);
        //
    }

    private String decimalToBinary(int n){
        if(n==0||n==1){
            return n+"";
        }else{
            return decimalToBinary(n/2)+(n%2);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.poblacion_button:
                poblacion= Integer.parseInt(poblacion_edit.getText().toString()) ;
                generacion_edit.setEnabled(true);
                generacion_button.setEnabled(true);
                poblacion_button.setVisibility(View.GONE);
                poblacion_edit.setEnabled(false);
                generacion_edit.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 500)});
                generacion_edit.setHint("Defina cantidad de Generaciones<500");
                break;
            case R.id.generacion_button:
                generacion= Integer.parseInt(generacion_edit.getText().toString()) ;
                cromosomas_edit.setEnabled(true);
                cromosomas_button.setEnabled(true);
                generacion_button.setVisibility(View.GONE);
                generacion_edit.setEnabled(false);
                break;
            case R.id.cromosomas_button:
                cromosomas= Integer.parseInt(cromosomas_edit.getText().toString()) ;
                individuos_edit.setEnabled(true);
                individuos_button.setEnabled(true);
                cromosomas_button.setVisibility(View.GONE);
                cromosomas_edit.setEnabled(false);
                max=(dos_elevadoal(cromosomas)-1);
                individuos_edit.setHint("Min: "+0+" "+"Max: "+max);
                individuos_edit.setFilters(new InputFilter[]{ new InputFilterMinMax(0, max)});
                individuos= new ArrayList<>();
                individuos_button.setText("Nº "+ (contador_individuos+1));
                contador_individuos++;
                break;
            case R.id.individuos_button:
                if(contador_individuos<poblacion){
                    individuos.add(completarbinarios(decimalToBinary(Integer.parseInt(individuos_edit.getText().toString()))));

                    individuos_button.setText("Nº "+ (contador_individuos+1));
                    contador_individuos++;
                    datos=datos+"+" +individuos_edit.getText().toString();
                    individuos_edit.setText("");
                }else{
                    individuos.add(completarbinarios(decimalToBinary(Integer.parseInt(individuos_edit.getText().toString()))));
                    individuos_button.setVisibility(View.GONE);
                    individuos_edit.setEnabled(false);
                    indice_button.setEnabled(true);
                    indice_edit.setEnabled(true);
                    indice_edit.setFilters(new InputFilter[]{ new InputFilterMinMax(0, cromosomas-1)});
                    indice_edit.setHint("Max: "+cromosomas);
                }
                break;
            case R.id.indice_button:
                indice=Integer.parseInt(indice_edit.getText().toString()) ;
                nexgeneration=new ArrayList<>();
                progressBar.setVisibility(View.VISIBLE);
                realizarMutacion();
                break;
        }

    }

    private void realizarMutacion() {

        for (int i=0;i<generacion;i++){
            seleccion_cruze();
            ArrayList<String> maximosgenes=insertar();
            individuos=nuevos_genes(maximosgenes);
            nexgeneration.clear();
        }
        progressBar.setVisibility(View.GONE);
        Intent intent= new Intent(this, view_data_algoritmo.class );
        intent.putExtra("data",individuos);
        startActivity(intent);

    }

    private ArrayList<String> nuevos_genes(ArrayList<String> maximosgenes) {
        if (maximosgenes.size()==poblacion){
            return maximosgenes;
        }else if(maximosgenes.size()==poblacion-1){
            maximosgenes.add(maximosgenes.get(0));
            return maximosgenes;
        }else{
//            if(maximosgenes.size()%2==0){
                int cantidad_ini=0;
                if (maximosgenes.size()!=0){
                    cantidad_ini= maximosgenes.size();
                }

                for (int i=maximosgenes.size()-1,a=0;i<poblacion-1;i++,a++){
                    if(cantidad_ini>a){
                        maximosgenes.add(maximosgenes.get(a));
                    }else{
                        a=0;
                        maximosgenes.add(maximosgenes.get(a));
                    }
                }
//            }
//              else{  for (int i=maximosgenes.size()-1, a=0,b=maximosgenes.size()-1;i<poblacion-1;a++,b--,i+=2){
//
//                    if(((maximosgenes.size()-1)/2)==a){
//                        maximosgenes.add(maximosgenes.get(a));
//                        a=0;
//                        b=maximosgenes.size()-1;
//                    }else{
//                        maximosgenes.add(maximosgenes.get(a));
//                        maximosgenes.add(maximosgenes.get(b));
//                    }
//                }
//            }
        }


        individuos.clear();
        return maximosgenes;
    }
    double porcentaje=0.0;
    double maxsetentaPorCiento;
    private ArrayList<String> insertar() {
        ArrayList<Integer> enteros=new ArrayList<>();
        Integer sumatotal=0;
        if(porcentaje<0.9){
            porcentaje+=0.1;
        }
        maxsetentaPorCiento=max*porcentaje;
        ArrayList<String> nuevosMaximos=new ArrayList<>();
        for(int i=0;i<poblacion;i++){
            Integer a=Integer.parseInt(nexgeneration.get(i),2);
            Log.e("cruze",nexgeneration.get(i));
            Log.e("decimal",a+"");
            enteros.add(a);
            sumatotal=sumatotal+a;
        }
        for(int i=0;i<poblacion;i++){
            if(maxsetentaPorCiento<enteros.get(i)){
                nuevosMaximos.add(nexgeneration.get(i));
            }
        }
        if (nuevosMaximos.size()==0){
            porcentaje=-0.1;
            insertar();
        }
        return nuevosMaximos;

    }


    private void seleccion_cruze() {

        for(int i=0,a=poblacion-1;i<poblacion/2;i++,a--){
            cruze(individuos.get(i).toCharArray(),individuos.get(a).toCharArray());
        }
    }


    private void cruze(char array1[], char array2[]) {
        char temp;

        for(int i=0,a=(cromosomas)-indice;i<indice;i++,a++){
            temp=array1[i];
            array1[i]=array2[a];
            array2[a]=temp;
        }

        nexgeneration.add(String.copyValueOf(array1));
        nexgeneration.add(String.copyValueOf(array2));
    }


    public String completarbinarios(String binary){
        int cantidaddigitos1=binary.length() ;
        String arr1=binary;
        if(cantidaddigitos1<cromosomas){
            for(int i=cantidaddigitos1;i<cromosomas ;i++){
                arr1="0"+arr1;
            }
        }
        return arr1;
    }
    private int dos_elevadoal(int maximo){
        int a=1;
        for(int i=0;i<maximo;i++){
            a=a*2;
        }
        return a;
    }
}
