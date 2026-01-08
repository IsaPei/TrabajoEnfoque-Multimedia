package com.example.multimediaenfoque;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText eTxtIntroValor;
    private RadioGroup rGroupButton;
    private RadioButton rBtnLongitud, rBtnPeso, rBtnTemperatura;
    private Button btnCalcular, btnBorrar, btnGuardar, btnHistorial;
    private TextView txtViewResultado;
    private CheckBox checkGuardar;
    private ListView listado;
    private ArrayList<String> arrayListado;
    private ArrayAdapter<String> arrayAdapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Conectar id del xml con las variables.
        eTxtIntroValor = findViewById(R.id.eTxtIntroValor);
        rGroupButton = findViewById(R.id.rGroupButton);
        rBtnLongitud = findViewById(R.id.rBtnLongitud);
        rBtnPeso = findViewById(R.id.rBtnPeso);
        rBtnTemperatura = findViewById(R.id.rBtnTemperatura);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnHistorial = findViewById(R.id.btnHistorial);
        txtViewResultado = findViewById(R.id.txtViewResultado);
        checkGuardar = findViewById(R.id.checkGuardar);
        listado = findViewById(R.id.listado);

        //Botón para hacer el cálculo de las unidades
        btnCalcular.setOnClickListener(v -> {
            String valor = eTxtIntroValor.getText().toString();
            if (!valor.isEmpty()){
                double num = Double.parseDouble(valor);
                double resultado = 0;
                String unidad = "";
                if (rBtnLongitud.isChecked()){
                    resultado = num / 1000; //Para pasarlo de metros a Km
                    unidad = " km";
                } else if (rBtnPeso.isChecked()) {
                    resultado = num / 1000; //Para pasarlo de gramos a Kg
                    unidad = " kg";
                } else if (rBtnTemperatura.isChecked()) {
                    resultado = (num*1.8) +32; //Para pasarlo de Celsius a Fahrenheit
                    unidad = " F";
                }
                if (rBtnLongitud.isChecked()||rBtnPeso.isChecked()||rBtnTemperatura.isChecked()){
                    String resultFin = resultado + unidad;
                    txtViewResultado.setText(resultFin);
                    arrayListado.add(0, resultFin);
                    arrayAdapList.notifyDataSetChanged();
                } else {
                    txtViewResultado.setText(R.string.errSelect);
                }
            } else {
                txtViewResultado.setText(R.string.errValor);
            }
        });

        //Botón para borrar
        btnBorrar.setOnClickListener(v -> {
            eTxtIntroValor.setText("");
            txtViewResultado.setText("");
            rGroupButton.clearCheck();
        });

        arrayListado = new ArrayList<>();
        arrayAdapList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListado);
        listado.setAdapter(arrayAdapList);

        //Para guardar
        btnGuardar.setOnClickListener(v -> {
            if (!checkGuardar.isChecked()){
                Toast.makeText(MainActivity.this,"Para poder guardar los datos debes hacer click en el checkbox.", Toast.LENGTH_SHORT).show();
                return;
            }
            String calculo = txtViewResultado.getText().toString();
            if (!calculo.isEmpty()){
                SharedPreferences sp = getSharedPreferences("Valores guardados", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sp.edit();
                spEditor.putString("valorGuardado", calculo);
                spEditor.apply();
                Toast.makeText(MainActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });

        //Para mostrar el historial
        btnHistorial.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("Valores guardados", Context.MODE_PRIVATE);
            String guardar = sp.getString("valorGuardado", "No hay valores guardados");
            txtViewResultado.setText(guardar);
            Toast.makeText(MainActivity.this, "Datos mostrados", Toast.LENGTH_SHORT).show();
        });
    }
}