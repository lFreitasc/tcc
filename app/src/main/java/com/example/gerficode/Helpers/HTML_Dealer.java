package com.example.gerficode.Helpers;

import android.content.Context;
import android.widget.Toast;


import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Entity.Produtos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class HTML_Dealer {

    private String url;
    private Context context;

    public HTML_Dealer(Context context, String url){
        this.context = context;
        this.url = url;


        initMethod();
    }

    public void initMethod(){
        if(url.contains("fazenda.pr.gov.br/nfce/qrcode")){
            try{
                URL oracle = new URL(url);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));

                String html = "";
                while (in.readLine() != null)
                    html += in.readLine();
                in.close();


                getDataFromHtml(html);

            }catch (Exception e){
                Toast.makeText(context, "Erro durante a leitura do QR-Code", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Erro de leitura da Nota Fiscal, endereço não condizente", Toast.LENGTH_LONG).show();
        }
    }


    private void getDataFromHtml(String html){

        int index;
        NotaFiscal notaFiscal = new NotaFiscal();
        ArrayList<String> lidos = new ArrayList<>();
        Produtos produtos;

        // Estabelecimento
        String resultado = "";
        char retorno = '.';

        index = html.indexOf("txtTopo");
        if(index != -1){
            index += 9;
            //Colocar uma string recebendo o valor dentro do while para diminuir processamento <----
            while((retorno = html.charAt(index)) != '<') {
                resultado += retorno;
                index++;
            }

            notaFiscal.setEstabelecimento(resultado);

        }
        html = html.substring(index);

        //Produtos, Quantidades e Valores
        while(index != -1){

            //Produto
            index = html.indexOf("txtTit2");
            if(index != -1){
                index += 9;
                while((retorno = html.charAt(index)) != '<') {
                    resultado += retorno;
                    index++;
                }

                if(lidos.contains(resultado)){
                    int pos;
                    pos = lidos.indexOf(resultado);
                    //#############################
                    /*
                    *
                    * Preciso verificar se já foi cadastrado um mesmo produto durante essa nota fiscal
                    * caso sim, verificar no banco pelo objeto e fazer um incremento (DAO), ou,
                    * armazenar todos os objetos em memoria para verificar tal fato e no final
                    * adicionar ao banco
                    *
                    * - pros da criação da lista: comum ter mais de um mesmo item na nf, vel. acesso ao
                    * banco, facilidade de execução
                    *
                    * */
                }else{
                    lidos.add(resultado);
                }

                resultado = "";

                //Quantidade
                index = html.indexOf("Qtde.");
                if(index != -1){
                    index += 15;
                    while((retorno = html.charAt(index)) != '<') {
                        resultado += retorno;
                        index++;
                    }

                }

                //Valor
                index = html.indexOf("valor");
                resultado = "";
                if(index != -1){
                    index += 7;
                    while((retorno = html.charAt(index)) != '<') {
                        resultado += retorno;
                        index++;
                    }

                }
                html = html.substring(index);
            }
        }


        //Total
    }


}
