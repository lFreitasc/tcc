package com.example.gerficode.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerficode.DAO.ProdutoDAO;
import com.example.gerficode.Database.Database;
import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Entity.Produtos;
import com.example.gerficode.R;

import java.util.List;

public class activity_search extends AppCompatActivity {

    TextView result_fiel;
    TextView search_text;
    Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        result_fiel = findViewById(R.id.search_result);
        search_text = findViewById(R.id.search_text);

        database = Database.getDatabase(activity_search.this);


    }

    public void search_method(View view){
        String busca = search_text.getText().toString();
        List<Produtos> todos_produtos = database.produtoDAO().getAll();
        List<NotaFiscal> todas_notas = database.notaFiscalDAO().getAll();
        String retorno = "";
        int count = 0;


        for(Produtos p : todos_produtos){
            if(count <= 10){
                if(editDistance(busca,p.getNome()) <= 13){
                    count++;
                    retorno += p.getNome() + " - R$" + p.getPreco() + " - Data: ";
                    for(NotaFiscal nf : todas_notas){
                        if(nf.getId() == p.getIdNotaFiscal()){
                            retorno += nf.getData() + "\n";
                        }
                    }
                }
            }

        }



        result_fiel.setText(retorno);


    }

    //Algoritmo para buscar string semelhantes
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
