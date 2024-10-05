package com.example.biourb;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CadastrarArvoreActivity extends AppCompatActivity {

    private EditText etNomeCientifico;
    private EditText etDataPlantio;
    private EditText etEstadoSaude;
    private EditText etLocalizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_arvore);

        etNomeCientifico = findViewById(R.id.etNomeCientifico);
        etDataPlantio = findViewById(R.id.etDataPlantio);
        etEstadoSaude = findViewById(R.id.etEstadoSaude);
        etLocalizacao = findViewById(R.id.etLocalizacao);
        Button btnCadastrar = findViewById(R.id.btnCadastrarArvore);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // Adiciona um TextWatcher para formatar a data de plantio
        etDataPlantio.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não faz nada
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não faz nada
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

        // Ação do botão Cadastrar
        btnCadastrar.setOnClickListener(v -> {
            String nomeCientifico = etNomeCientifico.getText().toString().trim();
            String dataPlantio = etDataPlantio.getText().toString().trim();
            String estadoSaude = etEstadoSaude.getText().toString().trim();
            String localizacao = etLocalizacao.getText().toString().trim();

            if (validarCampos(nomeCientifico, dataPlantio, estadoSaude, localizacao)) {
                try (DatabaseHelper db = new DatabaseHelper(this)) {
                    long result = db.cadastrarArvore(nomeCientifico, dataPlantio, estadoSaude, localizacao);
                    if (result > 0) {
                        Toast.makeText(CadastrarArvoreActivity.this, "Árvore cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();  // Fecha a tela após cadastro
                    } else {
                        Toast.makeText(CadastrarArvoreActivity.this, "Erro ao cadastrar árvore.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("CadastrarArvoreActivity", "Erro ao acessar o banco de dados", e);
                    Toast.makeText(this, "Erro ao acessar o banco de dados.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Ação do botão Voltar
        btnVoltar.setOnClickListener(v -> finish()); // Volta para a tela anterior
    }

    // Método para formatar a data de entrada
    private String formatarData(String input) {
        StringBuilder formatted = new StringBuilder();

        // Formata a entrada de acordo com o padrão DD/MM/AAAA
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
}