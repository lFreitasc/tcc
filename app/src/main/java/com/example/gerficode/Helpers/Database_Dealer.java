package com.example.gerficode.Helpers;

import android.content.Context;

import com.example.gerficode.Database.Database;
import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Entity.Produtos;

import java.util.List;

public class Database_Dealer {
    private Database db;

    public Database_Dealer(Context context, NotaFiscal notaFiscal, List<Produtos> listaProdutos) {
        db = Database.getDatabase(context);

        Long idNF = db.notaFiscalDAO().create(notaFiscal);

        for (Produtos p: listaProdutos) {
            p.setIdNotaFiscal(idNF);
            db.produtoDAO().create(p);
        }
    }
}
