package com.example.conversaodemoedas.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.conversaodemoedas.entity.CotacoesTO;

import java.util.List;
@Dao
public interface CotacaoDao {

    @Insert
    void inserir(CotacoesTO cotacoesTO);

    @Query("SELECT * FROM Cotacoes")
    List<CotacoesTO> listarTodos();

    @Query("SELECT * FROM Cotacoes WHERE valorReal = :valorReal")
    CotacoesTO consultarValorEmReal(Double valorReal);
}
