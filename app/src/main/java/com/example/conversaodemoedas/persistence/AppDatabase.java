package com.example.conversaodemoedas.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.conversaodemoedas.entity.CotacoesTO;
import com.example.conversaodemoedas.entity.UsuariosTO;

@Database(entities = {UsuariosTO.class, CotacoesTO.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UsuarioDao usuarioDao();
    public abstract CotacaoDao cotacaoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Apenas para testes, não recomendado em produção!
                    .build();
        }
        return instance;
    }
}
