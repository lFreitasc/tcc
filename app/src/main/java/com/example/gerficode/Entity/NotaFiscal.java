package com.example.gerficode.Entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(indices = @Index(value = {"id"}, unique = true))
public class NotaFiscal {
    /*
    * ID
    * Data
    * Valor Total
    * Local
    * */
    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String data;

    private Float valorTotal;

    private String estabelecimento;


    /*
    * Getters and Setters
    * */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {this.estabelecimento = estabelecimento;}
}
