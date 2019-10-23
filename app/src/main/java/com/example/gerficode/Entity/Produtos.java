package com.example.gerficode.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (foreignKeys = @ForeignKey(entity = NotaFiscal.class, parentColumns = "id", childColumns = "idNotaFiscal"))
public class Produtos {
    /*
    * Id
    * Nome
    * Quantidade
    * Preço
    * */

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(index = true)
    private Long idNotaFiscal;

    private String nome;

    private Float quantidade;

    private Float preco;


    public Produtos(Long idNotaFiscal) {
        this.idNotaFiscal = idNotaFiscal;
    }

    /*
    * Getters and Setters
    * */
    public Long getIdNotaFiscal() {
        return idNotaFiscal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }
}
