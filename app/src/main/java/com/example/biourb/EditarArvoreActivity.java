package com.example.biourb;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

public class EditarArvoreActivity extends AppCompatActivity {

    private EditText etNomeCientifico;
    private EditText etDataPlantio;
    private EditText etEstadoSaude;
    private EditText etLocalizacao;
    private long arvoreId; // ID da árvore a ser editada
    private String current = ""; // Para armazenar o estado da formatação de data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_arvore);

        etNomeCientifico = findViewById(R.id.etNomeCientifico);
        etDataPlantio = findViewById(R.id.etDataPlantio);
        etEstadoSaude = findViewById(R.id.etEstadoSaude);
        etLocalizacao = findViewById(R.id.etLocalizacao);

        Button btnSalvar = findViewById(R.id.btnSalvar);
        Button btnVoltar = findViewById(R.id.btnVoltar);
        Button btnExcluir = findViewById(R.id.btnExcluir);

        // Recebe o ID da árvore passada pela Intent
        arvoreId = getIntent().getLongExtra("arvoreId", -1);

        // Carrega os dados da árvore a partir do banco de dados
        carregarDadosArvore();

        // Adiciona o TextWatcher para formatar a data de plantio conforme o usuário digita
        etDataPlantio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não faz nada aqui
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não faz nada aqui
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().replaceAll("[^0-9]", "");
                String formatted = formatarData(input);

                // Se a entrada foi alterada, atualiza o campo de texto
                if (!formatted.equals(current)) {
                    current = formatted;
                    etDataPlantio.setText(current);
                    etDataPlantio.setSelection(current.length()); // Move o cursor para o final
                }
            }
        });

        // Ação do botão Salvar
        btnSalvar.setOnClickListener(v -> salvarAlteracoes());

        // Ação do botão Voltar
        btnVoltar.setOnClickListener(v -> finish());

        // Ação do botão Excluir
        btnExcluir.setOnClickListener(v -> new AlertDialog.Builder(EditarArvoreActivity.this)
                .setTitle("Excluir Árvore")
                .setMessage("Tem certeza que deseja excluir esta árvore?")
                .setPositiveButton("Sim", (dialog, which) -> excluirArvore())
                .setNegativeButton("Não", null)
                .show());
    }

    private void carregarDadosArvore() {
        try (DatabaseHelper db = new DatabaseHelper(this)) {
            Arvore arvore = db.getArvoreById(arvoreId);
            if (arvore != null) {
                etNomeCientifico.setText(arvore.getNomeCientifico());
                etDataPlantio.setText(arvore.getDataPlantio());
                etEstadoSaude.setText(arvore.getEstadoSaude());
                etLocalizacao.setText(arvore.getLocalizacao());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar dados da árvore.", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarAlteracoes() {
        String nomeCientifico = etNomeCientifico.getText().toString().trim();
        String dataPlantio = etDataPlantio.getText().toString().trim();
        String estadoSaude = etEstadoSaude.getText().toString().trim();
        String localizacao = etLocalizacao.getText().toString().trim();

        if (validarCampos(nomeCientifico, dataPlantio, estadoSaude, localizacao)) {
            try (DatabaseHelper db = new DatabaseHelper(this)) {
                db.atualizarArvore(arvoreId, nomeCientifico, dataPlantio, estadoSaude, localizacao);
                Toast.makeText(this, "Árvore atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Log.e("EditarArvoreActivity", "Erro ao acessar o banco de dados", e);
                Toast.makeText(this, "Erro ao acessar o banco de dados.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void excluirArvore() {
        try (DatabaseHelper db = new DatabaseHelper(this)) {
            db.deletarArvore(arvoreId);
            Toast.makeText(this, "Árvore excluída com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e("EditarArvoreActivity", "Erro ao excluir a árvore", e);
            Toast.makeText(this, "Erro ao excluir a árvore.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos(String nomeCientifico, String dataPlantio, String estadoSaude, String localizacao) {
        if (nomeCientifico.isEmpty()) {
            etNomeCientifico.setError("Campo obrigatório");
            return false;
        }
        if (dataPlantio.isEmpty()) {
            etDataPlantio.setError("Campo obrigatório");
            return false;
        }
        if (estadoSaude.isEmpty()) {
            etEstadoSaude.setError("Campo obrigatório");
            return false;
        }
        if (localizacao.isEmpty()) {
            etLocalizacao.setError("Campo obrigatório");
            return false;
        }
        return true;
    }

    // Método para formatar a data de entrada conforme o padrão DD/MM/AAAA
    private String formatarData(String input) {
        StringBuilder formatted = new StringBuilder();

        if (input.length() > 2) {
            formatted.append(input.substring(0, 2)).append("/");
        }
        if (input.length() > 4) {
            formatted.append(input.substring(2, 4)).append("/");
            formatted.append(input.substring(4, Math.min(input.length(), 8)));
        } else if (input.length() > 2) {
            formatted.append(input.substring(2));
        }

        return formatted.toString();
    }
}