package com.example.gerficode.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gerficode.R;

public class AdapterNF extends RecyclerView.Adapter<AdapterNF.MyViewHolder> {

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View notaFiscal = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_model, parent, false);

        return new MyViewHolder(notaFiscal);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
