package com.example.biourb;

import androidx.annotation.NonNull;

public class Arvore {
    private final long id;
    private final String nomeCientifico;
    private final String dataPlantio;
    private final String estadoSaude;
    private final String localizacao;

    // Construtor
    public Arvore(long id, @NonNull String nomeCientifico, @NonNull String dataPlantio, @NonNull String estadoSaude, @NonNull String localizacao) {
        this.id = id;
        this.nomeCientifico = nomeCientifico;
        this.dataPlantio = dataPlantio;
        this.estadoSaude = estadoSaude;
        this.localizacao = localizacao;
    }

    // Métodos getters
    public long getId() {
        return id;
    }

    @NonNull
    public String getNomeCientifico() {
        return nomeCientifico;
    }

    @NonNull
    public String getDataPlantio() {
        return dataPlantio;
    }

    @NonNull
    public String getEstadoSaude() {
        return estadoSaude;
    }

    @NonNull
    public String getLocalizacao() {
        return localizacao;
    }

    @Override
    @NonNull
    public String toString() {
        return "Nome Científico: " + nomeCientifico + "\n" +
                "Data de Plantio: " + dataPlantio + "\n" +
                "Estado de Saúde: " + estadoSaude + "\n" +
                "Localização: " + localizacao;
    }
}