package com.example.gerficode.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Produtos {
    /*
    * Id
    * Nome
    * Quantidade
    * Preço
    * */

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long idNotaFiscalId;

    private String nome;

    private int quantidade;

    private Float preco;


    public Produtos(Long foreignNotaFiscalId) {
        this.idNotaFiscalId = foreignNotaFiscalId;
    }

    /*
    * Getters and Setters
    * */
    public Long getForeignNotaFiscalId() {
        return idNotaFiscalId;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }
}
