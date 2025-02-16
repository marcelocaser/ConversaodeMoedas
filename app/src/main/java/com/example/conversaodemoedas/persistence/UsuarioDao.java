package com.example.conversaodemoedas.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.conversaodemoedas.entity.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    void inserir(Usuario usuario);

    @Query("SELECT * FROM usuarios")
    List<Usuario> listarTodos();
}
