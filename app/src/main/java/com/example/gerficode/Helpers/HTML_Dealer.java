package com.example.gerficode.Helpers;

import android.content.Context;
import android.util.Log;
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

    private void initMethod(){

        if(url.contains("fazenda.pr.gov.br/nfce/qrcode")){
            try{
                URL oracle = new URL(url);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));

                String html = "";
                String retorno;
                while ((retorno = in.readLine()) != null)
                {
                    html += retorno;
                }
                in.close();

                getDataFromHtml(html);
            }catch (Exception e){
                Log.e("Lucas",e.getMessage());
                Toast.makeText(context, "Erro durante a leitura do QR-Code", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, "Erro de leitura da Nota Fiscal, endereço não condizente ou Estado não suportado", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromHtml(String html){

        int index;
        NotaFiscal notaFiscal = new NotaFiscal();
        ArrayList<String> lidos = new ArrayList<>();
        ArrayList<Float> lidosQtd = new ArrayList<>();
        ArrayList<Produtos> listaProdutos = new ArrayList<>();
        Produtos produtos;
        float valorTotal = 0;

        // Estabelecimento
        String resultado = "";
        char retorno;

        index = html.indexOf("txtTopo");
        if(index != -1){
            index += 9;
            while((retorno = html.charAt(index)) != '<') {
                resultado += retorno;
                index++;
            }

            notaFiscal.setEstabelecimento(resultado);

        }

        // DATA
        index = html.indexOf("Emissão:") + 18;
        notaFiscal.setData(html.substring(index, index+10));


        /*//TOTAL
        index = html.indexOf("Valor total");
        resultado = "";
        if(index != -1){
            index += 51;
            while ((retorno = html.charAt(index)) != '<'){
                resultado += retorno;
                index++;
            }

            resultado = resultado.replaceAll(" ","");
            resultado = resultado.replaceAll(",",".");

            notaFiscal.setValorTotal(Float.parseFloat(resultado));
        }else{
            Log.e("Lucas","Valor não encontrado");
            notaFiscal.setValorTotal(0f);
        }*/

        //Produtos -> while, enquanto existir produtos a serem cadastrados
        index = 0;
        while(index != -1){

            resultado = "";

            //Produto
            index = html.indexOf("txtTit2");
            if(index != -1){
                index += 9;
                while((retorno = html.charAt(index)) != '<') {
                    resultado += retorno;
                    index++;
                }

                //Verificando se o produto lido já foi "cadastrado" (armazenado na lista para cadastro)
                if(lidos.contains(resultado)){
                    int pos = lidos.indexOf(resultado);
                    Float quantidade = lidosQtd.get(pos); //existe a possibilidade de erro ?

                    resultado = "";

                    //Quantidade
                    index = html.indexOf("Qtde.");
                    if(index != -1){
                        index += 15;
                        while((retorno = html.charAt(index)) != '<') {
                            resultado += retorno;
                            index++;
                        }

                        resultado = resultado.replaceAll(" ","");
                        resultado = resultado.replaceAll(",",".");
                        quantidade = Float.parseFloat(resultado);
                    }
                    valorTotal += listaProdutos.get(pos).getPreco() * quantidade;
                    listaProdutos.get(pos).setQuantidade(listaProdutos.get(pos).getQuantidade() + quantidade);
                    index = html.indexOf("Unit.:");
                    index+=16;


                }else{

                    produtos = new Produtos();
                    produtos.setNome(resultado);
                    lidos.add(resultado);
                    resultado = "";


                    //Quantidade
                    index = html.indexOf("Qtde.");
                    if(index != -1){
                        index += 15;
                        while((retorno = html.charAt(index)) != '<') {
                            resultado += retorno;
                            index++;
                        }
                        resultado = resultado.replaceAll(" ","");
                        resultado = resultado.replaceAll(",",".");
                        if((index = resultado.indexOf(".")) != -1){
                            resultado = resultado.substring(0,index+2);
                        }
                        produtos.setQuantidade(Float.parseFloat(resultado));
                        lidosQtd.add(produtos.getQuantidade());

                    }


                    //Valor
                    index = html.indexOf("Unit.:");
                    resultado = "";
                    if(index != -1){
                        index += 16;
                        while((retorno = html.charAt(index)) != '<') {
                            resultado += retorno;
                            index++;
                        }
                        resultado = resultado.replaceAll(" ","");
                        resultado = resultado.replaceAll(",",".");
                        produtos.setPreco(Float.parseFloat(resultado));
                        valorTotal += produtos.getPreco() * produtos.getQuantidade();
                    }

                    listaProdutos.add(produtos);
                }
                html = html.substring(index);
            }
        }
        if(lidos.size() == 0){
            Toast.makeText(context,"Erro durante leitura dos produtos",Toast.LENGTH_LONG).show();
        }else{
            //putDataIntoDatabase(notaFiscal, listaProdutos);
            notaFiscal.setValorTotal(valorTotal);
            new Database_Dealer(context, notaFiscal, listaProdutos);
        }


    }



}
