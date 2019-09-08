package com.example.gerficode.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gerficode.Entity.NotaFiscal;

import java.util.List;

@Dao
public interface NotaFiscalDAO {
    @Insert
    void create(NotaFiscal notaFiscal);

    @Delete
    void delete(NotaFiscal notaFiscal);

    @Update
    void update(NotaFiscal notaFiscal);

    @Query("SELECT * FROM notafiscal")
    List<NotaFiscal> getAll();
}
