package com.example.gerficode.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gerficode.Entity.Produtos;

import java.util.List;

@Dao
public interface ProdutoDAO {

    @Insert
    void create(Produtos produtos);

    @Delete
    void delete(Produtos produtos);

    @Query("SELECT * FROM produtos")
    List<Produtos> getAll();
}
