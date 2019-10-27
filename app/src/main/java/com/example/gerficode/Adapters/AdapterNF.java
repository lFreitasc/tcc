package com.example.gerficode.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.R;

import java.util.List;

public class AdapterNF extends RecyclerView.Adapter<AdapterNF.MyViewHolder> {

    private List<NotaFiscal> lista;

    public AdapterNF(List<NotaFiscal> lista){
        this.lista = lista;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View notaFiscal = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_model, parent, false);

        return new MyViewHolder(notaFiscal);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.estabelecimento.setText(lista.get(position).getEstabelecimento());

        holder.valor.setText("Total: R$ "+lista.get(position).getValorTotal().toString()); //valor nulo ?
        holder.data.setText(lista.get(position).getData());

    }

    @Override
    public int getItemCount() {

        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView estabelecimento;
        TextView valor;
        TextView data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            estabelecimento = itemView.findViewById(R.id.textViewStore);
            valor = itemView.findViewById(R.id.textViewValue);
            data = itemView.findViewById(R.id.textViewDate);
        }
    }

}
