package com.example.conversaodemoedas.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.conversaodemoedas.entity.UsuariosTO;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    void inserir(UsuariosTO usuariosTO);

    @Query("SELECT * FROM Usuarios")
    List<UsuariosTO> listarTodos();
}
