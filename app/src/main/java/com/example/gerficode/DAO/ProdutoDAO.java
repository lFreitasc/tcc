package com.example.gerficode.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gerficode.Entity.Produtos;

import java.util.List;


@Dao
public interface ProdutoDAO {

    @Insert
    void create(Produtos produtos);

    @Delete
    void delete(Produtos produtos);

    @Update
    void update(Produtos produtos);

    @Query("SELECT * FROM produtos")
    List<Produtos> getAll();

    @Query("SELECT * FROM produtos WHERE idNotaFiscal = :id")
    List<Produtos> getProductsList(Long id);

    @Query("DELETE FROM produtos WHERE idNotaFiscal = :id")
    void deleteByNF(Long id);

}
