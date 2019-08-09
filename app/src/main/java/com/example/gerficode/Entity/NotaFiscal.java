package com.example.gerficode.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class NotaFiscal {
    /*
    * ID
    * Data
    * Valor Total
    * Local
    * */
    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Date data;

    private Double valorTotal;

    private String local;


    /*
    * Getters and Setters
    * */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
