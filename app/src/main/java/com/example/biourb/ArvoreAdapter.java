package com.example.biourb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ArvoreAdapter extends ArrayAdapter<Arvore> {
    public ArvoreAdapter(@NonNull Context context, @NonNull ArrayList<Arvore> arvores) {
        super(context, 0, arvores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Obtém o objeto Arvore para a posição atual
        Arvore arvore = getItem(position);

        // Verifica se uma view existente está sendo reutilizada, caso contrário, infla uma nova view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_arvore, parent, false);
        }

        // Encontra as views do layout
        TextView tvNomeCientifico = convertView.findViewById(R.id.tvNomeCientifico);
        TextView tvDataPlantio = convertView.findViewById(R.id.tvDataPlantio);
        TextView tvEstadoSaude = convertView.findViewById(R.id.tvEstadoSaude);
        TextView tvLocalizacao = convertView.findViewById(R.id.tvLocalizacao);

        // Preenche as views com os dados da árvore
        if (arvore != null) {
            tvNomeCientifico.setText(arvore.getNomeCientifico());
            tvDataPlantio.setText(getContext().getString(R.string.data_plantio, arvore.getDataPlantio()));
            tvEstadoSaude.setText(getContext().getString(R.string.estado_saude, arvore.getEstadoSaude()));
            tvLocalizacao.setText(getContext().getString(R.string.localizacao, arvore.getLocalizacao()));
        }

        return convertView;
    }
}