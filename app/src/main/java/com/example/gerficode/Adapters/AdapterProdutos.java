package com.example.gerficode.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gerficode.Entity.Produtos;
import com.example.gerficode.R;

import java.util.List;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.MyViewHolder>{
    private List<Produtos> listaProdutos;

    public AdapterProdutos(List<Produtos> listaProdutos){
        this.listaProdutos = listaProdutos;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewNotaFiscal = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_notafiscal, parent, false);

        return new MyViewHolder(viewNotaFiscal);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.produto.setText(listaProdutos.get(position).getNome());
        holder.valorUnit.setText(listaProdutos.get(position).getPreco().toString());
        holder.quantidade.setText(listaProdutos.get(position).getQuantidade().toString());

    }

    @Override
    public int getItemCount() {

        return listaProdutos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView produto;
        TextView valorUnit;
        TextView quantidade;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            produto = itemView.findViewById(R.id.textViewProdutos);
            valorUnit = itemView.findViewById(R.id.textViewValor);
            quantidade = itemView.findViewById(R.id.textViewQuantidade);
        }
    }
}
