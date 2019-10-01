package com.example.gerficode.Helpers;

import android.content.Context;
import android.widget.Toast;



import com.example.gerficode.Database.Database;
import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Entity.Produtos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class HTML_Dealer {

    private String url;
    private Context context;
    static Database db;

    public HTML_Dealer(Context context, String url){
        this.context = context;
        this.url = url;

        db = Database.getDatabase(this.context);
        initMethod();
    }

    public void initMethod(){
        if(url.contains("fazenda.pr.gov.br/nfce/qrcode")){
            try{
                URL oracle = new URL(url);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream())); //Erro durante essa chamada

                String html = "";
                while (in.readLine() != null)
                    html += in.readLine();
                in.close();


                getDataFromHtml(html);
                Toast.makeText(context,"ASD",Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(context, "Erro durante a leitura do QR-Code", Toast.LENGTH_SHORT).show();


            }
        }else{
            Toast.makeText(context, "Erro de leitura da Nota Fiscal, endereço não condizente", Toast.LENGTH_LONG).show();
        }
    }


    private void putDataIntoDatabase(NotaFiscal notaFiscal, ArrayList<Produtos> produtos){
        db.notaFiscalDAO().create(notaFiscal);
        for (Produtos p: produtos) {
            db.produtoDAO().create(p);
        }

    }

    private void getDataFromHtml(String html){

        int index;
        NotaFiscal notaFiscal = new NotaFiscal();
        ArrayList<String> lidos = new ArrayList<>();
        ArrayList<Produtos> listaProdutos = new ArrayList<>();


        Produtos produtos;

        // Estabelecimento
        String resultado = "";
        char retorno = '.';

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
        index = html.indexOf("Emissão") + 18;
        notaFiscal.setData(html.substring(index, index+10));


        //TOTAL
        index = html.indexOf("Valor total");
        resultado = "";
        if(index != -1){
            index += 48;
            while ((retorno = html.charAt(index)) != '<'){
                resultado += retorno;
                index++;
            }

            notaFiscal.setValorTotal(Float.parseFloat(resultado));
        }

        //Produtos -> while, enquanto existir produtos a serem cadastrados
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
                    int quantidade = 1;
                    //Quantidade
                    index = html.indexOf("Qtde.");
                    if(index != -1){
                        index += 15;
                        while((retorno = html.charAt(index)) != '<') {
                            resultado += retorno;
                            index++;
                        }

                        quantidade = Integer.parseInt(resultado);
                    }

                    listaProdutos.get(pos).setQuantidade(listaProdutos.get(pos).getQuantidade() + quantidade);


                }else{

                    produtos = new Produtos(notaFiscal.getId());
                    produtos.setNome(resultado);
                    lidos.add(resultado);

                    //Quantidade
                    index = html.indexOf("Qtde.");
                    if(index != -1){
                        index += 15;
                        while((retorno = html.charAt(index)) != '<') {
                            resultado += retorno;
                            index++;
                        }
                        produtos.setQuantidade(Integer.parseInt(resultado));
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
                        produtos.setPreco(Float.parseFloat(resultado));
                    }

                }
                html = html.substring(index);
            }
        }
    }



}
