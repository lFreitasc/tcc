package com.example.gerficode.Database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gerficode.DAO.NotaFiscalDAO;
import com.example.gerficode.DAO.ProdutoDAO;
import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Entity.Produtos;

@androidx.room.Database(entities = {NotaFiscal.class, Produtos.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract NotaFiscalDAO notaFiscalDAO();
    public abstract ProdutoDAO produtoDAO();

    private static Database instance;

    public static Database getDatabase(final Context context){

        if(instance == null){
            synchronized (Database.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context, Database.class, "database.db").allowMainThreadQueries().build();
                }
            }

        }

        return instance;

    }
}
