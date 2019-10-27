package com.example.gerficode.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerficode.Adapters.AdapterProdutos;
import com.example.gerficode.Database.Database;
import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Entity.Produtos;
import com.example.gerficode.Helpers.Database_Dealer;
import com.example.gerficode.Helpers.RecyclerItemClickListener;
import com.example.gerficode.R;

import java.util.ArrayList;
import java.util.List;

//Falta fazer uma consulta no banco de dados e verificar se existe produtos referentes a nota fiscal em tratamento
//Esse método é tanto para adicionar manualmente quanto para editar já existentes
//Vale refatoração para CEU-Activity ??

public class activity_addManually extends AppCompatActivity {
    private RecyclerView recyclerView;
    AdapterProdutos adapter;
    private List<Produtos> listaProdutos = new ArrayList<>();
    private Database database;
    private NotaFiscal notaFiscal;
    private Produtos ultimoProduto = null;
    private boolean updateFromExisting = false;
    private TextView textEstab;
    private TextView textData;
    private TextView textNome;
    private TextView textValorUnit;
    private TextView textQuantidade;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manually);

        textEstab = findViewById(R.id.editTextNfEstab);
        textValorUnit = findViewById(R.id.editTextProductValorUnit);
        textQuantidade = findViewById(R.id.editTextProductQuant);
        textData = findViewById(R.id.editTextNfData);
        textNome = findViewById(R.id.editTextProductName);


        Bundle bundle = getIntent().getExtras();
        database = Database.getDatabase(activity_addManually.this);


        if(bundle != null){
            notaFiscal = database.notaFiscalDAO().getByID(bundle.getLong("idNotaFiscal"));
            updateFromExisting = true;
            textData.setText(notaFiscal.getData());
            textEstab.setText(notaFiscal.getEstabelecimento());

        }


        defineRecyclerView();
    }

    public void defineRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewProdutos);
        adapter = new AdapterProdutos(listaProdutos);

        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        //((LinearLayoutManager) layoutManager).setStackFromEnd(true); //Reverte a exibixão dos dados do RecyclerView, verificar funcionamento

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position)
                            {
                                Produtos p = database.produtoDAO().getAll().get(position);
                                textNome.setText(p.getNome());
                                textQuantidade.setText(p.getQuantidade().toString());
                                textValorUnit.setText(p.getPreco().toString());
                            }


                            @Override
                            public void onLongItemClick(View view, int position) {
                                Toast.makeText(getApplicationContext(),"Toque longo", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    //Ação click adicionar
    public void btnActionAdd(View view){

        if(ultimoProduto != null){
            ultimoProduto.setNome(textNome.toString());
            ultimoProduto.setQuantidade(Float.parseFloat(textQuantidade.toString()));
            ultimoProduto.setPreco(Float.parseFloat(textValorUnit.toString()));

        }else{
            ultimoProduto = new Produtos(notaFiscal.getId());
            ultimoProduto.setNome(textNome.toString());
            ultimoProduto.setQuantidade(Float.parseFloat(textQuantidade.toString()));
            ultimoProduto.setPreco(Float.parseFloat(textValorUnit.toString()));
            listaProdutos.add(ultimoProduto);
        }

        //limpar referencia e campos para novos produtos
        ultimoProduto = null;
        textNome.setText("");
        textValorUnit.setText("");
        textQuantidade.setText("");
    }


    //Ação click OK
    public void btnActionOK(View view){


        if(updateFromExisting){
            database.notaFiscalDAO().update(notaFiscal);
            database.produtoDAO().deleteByNF(notaFiscal.getId());
            for(Produtos p : listaProdutos){
                database.produtoDAO().create(p);
            }

        }else{
            notaFiscal = new NotaFiscal();
            notaFiscal.setEstabelecimento(textEstab.toString());
            notaFiscal.setData(textData.toString());
            float valorTotal = 0f;
            for(Produtos p : listaProdutos){
                database.produtoDAO().create(p);
                valorTotal += p.getPreco() * p.getQuantidade();
            }
            notaFiscal.setValorTotal(valorTotal);

            new Database_Dealer(getApplicationContext(), notaFiscal, listaProdutos);
            database.notaFiscalDAO().create(notaFiscal);

        }

    }
}
