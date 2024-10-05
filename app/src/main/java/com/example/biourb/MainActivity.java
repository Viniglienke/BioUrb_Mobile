package com.example.biourb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cadastrarArvore = findViewById(R.id.btnCadastrarArvore);
        Button arvoresCadastradas = findViewById(R.id.btnArvoresCadastradas);

        cadastrarArvore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CadastrarArvoreActivity.class);
            startActivity(intent);
        });

        arvoresCadastradas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ArvoresCadastradasActivity.class);
            startActivity(intent);
        });
    }
}
