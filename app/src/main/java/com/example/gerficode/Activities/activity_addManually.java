package com.example.gerficode.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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



public class activity_addManually extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterProdutos adapter;
    private List<Produtos> listaProdutos = new ArrayList<>();
    private Database database;
    private NotaFiscal notaFiscal;
    private Produtos ultimoProduto = null;
    private int ultimoProdutoIndex = 0;
    private boolean updateFromExisting = false;

    //Layout
    private TextView textEstab;
    private TextView textData;
    private TextView textNome;
    private TextView textValorUnit;
    private TextView textQuantidade;
    private Button btnText;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manually);

        textEstab = findViewById(R.id.editTextNfEstab);
        textValorUnit = findViewById(R.id.editTextProductValorUnit);
        textQuantidade = findViewById(R.id.editTextProductQuant);
        textData = findViewById(R.id.editTextNfData);
        textNome = findViewById(R.id.editTextProductName);
        btnText = findViewById(R.id.buttonAdd);
        btnDelete = findViewById(R.id.buttonApagar);

        Bundle bundle = getIntent().getExtras();
        database = Database.getDatabase(activity_addManually.this);


        if(bundle != null){
            notaFiscal = database.notaFiscalDAO().getByID(bundle.getLong("idNotaFiscal"));
            updateFromExisting = true;
            textData.setText(notaFiscal.getData());
            textEstab.setText(notaFiscal.getEstabelecimento());
            btnDelete.setVisibility(View.VISIBLE);

            listaProdutos = database.produtoDAO().getProductsList(notaFiscal.getId());
        }


        defineRecyclerView();
    }

    public void defineRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewProdutos);
        adapter = new AdapterProdutos(listaProdutos);

        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

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
                                ultimoProduto = database.produtoDAO().getAll().get(position);
                                ultimoProdutoIndex = position;
                                textNome.setText(ultimoProduto.getNome());
                                textQuantidade.setText(ultimoProduto.getQuantidade().toString());
                                textValorUnit.setText(ultimoProduto.getPreco().toString());
                                btnText.setText("Alterar");
                            }


                            @Override
                            public void onLongItemClick(View view, int position) {
                                Toast.makeText(getApplicationContext(),"Produto removido", Toast.LENGTH_LONG).show();
                                listaProdutos.remove(position);
                                adapter.notifyDataSetChanged();
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

        try {
            if (ultimoProduto != null) {
                listaProdutos.remove(ultimoProdutoIndex);
                ultimoProduto.setNome(textNome.getText().toString());
                ultimoProduto.setQuantidade(Float.parseFloat(textQuantidade.getText().toString()));
                ultimoProduto.setPreco(Float.parseFloat(textValorUnit.getText().toString()));
                btnText.setText("Adicionar");
            } else {
                ultimoProduto = new Produtos();
                ultimoProduto.setNome(textNome.getText().toString());
                ultimoProduto.setQuantidade(Float.parseFloat(textQuantidade.getText().toString()));
                ultimoProduto.setPreco(Float.parseFloat(textValorUnit.getText().toString()));
            }
            listaProdutos.add(ultimoProduto);


            //limpar referencia e campos para novos produtos
            ultimoProduto = null;
            textNome.setText("");
            textValorUnit.setText("");
            textQuantidade.setText("");

            adapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.e("Lucas", "Classe activity_addManually, linha 158: "+e.getMessage());
        }
    }


    //Ação click OK
    public void btnActionOK(View view) {
        if (textData.getText().toString().equals("") || textEstab.getText().toString().equals("") || listaProdutos.size() == 0) {
            Toast.makeText(getApplicationContext(), "Verifique os dados cadastrados", Toast.LENGTH_LONG).show();
        } else {
            float valorTotal = 0f;
            if (updateFromExisting) {
                //caso esteja alterando uma nota fiscal já existente
                database.produtoDAO().deleteByNF(notaFiscal.getId());
                for (Produtos p : listaProdutos) {
                    p.setIdNotaFiscal(notaFiscal.getId());
                    valorTotal += p.getPreco() * p.getQuantidade();
                    database.produtoDAO().create(p);
                }
                notaFiscal.setValorTotal(valorTotal);
                notaFiscal.setData(textData.getText().toString());
                notaFiscal.setEstabelecimento(textEstab.getText().toString());
                database.notaFiscalDAO().update(notaFiscal);


            } else {
                //Caso precise criar uma nova nota fiscal
                notaFiscal = new NotaFiscal();
                notaFiscal.setEstabelecimento(textEstab.getText().toString());
                notaFiscal.setData(textData.getText().toString());

                for (Produtos p : listaProdutos) {
                    valorTotal += p.getPreco() * p.getQuantidade();
                }
                notaFiscal.setValorTotal(valorTotal);


                new Database_Dealer(getApplicationContext(), notaFiscal, listaProdutos);


            }
            finish();
        }
    }

    public void btnDelte(View view){
        database.produtoDAO().deleteByNF(notaFiscal.getId());
        database.notaFiscalDAO().delete(notaFiscal);

        finish();
    }
}
