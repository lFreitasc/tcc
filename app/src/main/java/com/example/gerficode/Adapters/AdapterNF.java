package com.example.gerficode.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gerficode.Model.NotaFiscalDTO;
import com.example.gerficode.R;

import java.util.List;

public class AdapterNF extends RecyclerView.Adapter<AdapterNF.MyViewHolder> {

    private List<NotaFiscalDTO> lista;

    public AdapterNF(List<NotaFiscalDTO> lista){
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
        holder.valor.setText(lista.get(position).getValor().toString());
        holder.data.setText(lista.get(position).getData().toString());

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
