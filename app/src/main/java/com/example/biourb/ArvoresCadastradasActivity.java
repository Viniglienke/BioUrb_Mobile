package com.example.biourb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ArvoresCadastradasActivity extends AppCompatActivity {
    private static final String TAG = "ArvoresCadastradasActivity";
    private ArrayList<Arvore> arvoresList = new ArrayList<>();
    private ArvoreAdapter adapter; // Mude de ArrayAdapter para ArvoreAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arvores_cadastradas);

        ListView lvArvores = findViewById(R.id.lvArvores);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        try (DatabaseHelper db = new DatabaseHelper(this)) {
            arvoresList = db.getArvoresCadastradas(); // Recuperar a lista de árvores

            // Use o ArvoreAdapter aqui
            adapter = new ArvoreAdapter(this, arvoresList);
            lvArvores.setAdapter(adapter);

            if (arvoresList.isEmpty()) {
                Toast.makeText(this, "Nenhuma árvore cadastrada.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao acessar o banco de dados.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Erro ao acessar o banco de dados", e);
        }

        lvArvores.setOnItemClickListener((parent, view, position, id) -> {
            Arvore arvoreSelecionada = arvoresList.get(position);
            Intent intent = new Intent(this, EditarArvoreActivity.class);
            intent.putExtra("arvoreId", arvoreSelecionada.getId()); // Passa o ID da árvore selecionada
            startActivity(intent);
        });

        btnVoltar.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza a lista de árvores quando a atividade é retomada
        try (DatabaseHelper db = new DatabaseHelper(this)) {
            arvoresList.clear();
            arvoresList.addAll(db.getArvoresCadastradas());
            adapter.notifyDataSetChanged(); // Notifica o adaptador sobre a mudança
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar lista de árvores", e);
        }
    }
}