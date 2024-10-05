package com.example.biourb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome do banco de dados e tabela
    private static final String DATABASE_NAME = "biourb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ARVORES = "arvores";

    // Colunas da tabela de árvores
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME_CIENTIFICO = "nome_cientifico";
    private static final String COLUMN_DATA_PLANTIO = "data_plantio";
    private static final String COLUMN_ESTADO_SAUDE = "estado_saude";
    private static final String COLUMN_LOCALIZACAO = "localizacao";

    // Construtor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método para criar a tabela quando o banco de dados for criado
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ARVORES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOME_CIENTIFICO + " TEXT,"
                + COLUMN_DATA_PLANTIO + " TEXT,"
                + COLUMN_ESTADO_SAUDE + " TEXT,"
                + COLUMN_LOCALIZACAO + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Método chamado quando o banco de dados for atualizado
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARVORES);
        onCreate(db);
    }

    // Método para adicionar uma árvore
    public long cadastrarArvore(String nomeCientifico, String dataPlantio, String estadoSaude, String localizacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_CIENTIFICO, nomeCientifico);
        values.put(COLUMN_DATA_PLANTIO, dataPlantio);
        values.put(COLUMN_ESTADO_SAUDE, estadoSaude);
        values.put(COLUMN_LOCALIZACAO, localizacao);

        long result = db.insert(TABLE_ARVORES, null, values);
        db.close();
        return result;
    }

    // Método para recuperar todas as árvores cadastradas
    public ArrayList<Arvore> getArvoresCadastradas() {
        ArrayList<Arvore> arvoresList = new ArrayList<>();

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ARVORES, null)) {

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                    String nomeCientifico = cursor.getString(cursor.getColumnIndex(COLUMN_NOME_CIENTIFICO));
                    String dataPlantio = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_PLANTIO));
                    String estadoSaude = cursor.getString(cursor.getColumnIndex(COLUMN_ESTADO_SAUDE));
                    String localizacao = cursor.getString(cursor.getColumnIndex(COLUMN_LOCALIZACAO));

                    // Verifique se os índices são válidos
                    if (id != -1 && nomeCientifico != null && dataPlantio != null &&
                            estadoSaude != null && localizacao != null) {
                        Arvore arvore = new Arvore(id, nomeCientifico, dataPlantio, estadoSaude, localizacao);
                        arvoresList.add(arvore);
                    } else {
                        Log.w("DatabaseHelper", "Um ou mais dados da árvore são inválidos.");
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao recuperar árvores", e);
        }
        return arvoresList; // Este método deve retornar ArrayList<Arvore>
    }

    // Método para recuperar uma árvore pelo ID
    public Arvore getArvoreById(long id) {
        Arvore arvore = null;
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_ARVORES, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                long arvoreId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                String nomeCientifico = cursor.getString(cursor.getColumnIndex(COLUMN_NOME_CIENTIFICO));
                String dataPlantio = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_PLANTIO));
                String estadoSaude = cursor.getString(cursor.getColumnIndex(COLUMN_ESTADO_SAUDE));
                String localizacao = cursor.getString(cursor.getColumnIndex(COLUMN_LOCALIZACAO));

                arvore = new Arvore(arvoreId, nomeCientifico, dataPlantio, estadoSaude, localizacao);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao obter a árvore pelo ID", e);
        }
        return arvore;
    }


    // Método para atualizar uma árvore
    public void atualizarArvore(long id, String nomeCientifico, String dataPlantio, String estadoSaude, String localizacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_CIENTIFICO, nomeCientifico);
        values.put(COLUMN_DATA_PLANTIO, dataPlantio);
        values.put(COLUMN_ESTADO_SAUDE, estadoSaude);
        values.put(COLUMN_LOCALIZACAO, localizacao);

        db.update(TABLE_ARVORES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para excluir uma árvore
    public void deletarArvore(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("arvores", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}
