package com.example.gerficode.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gerficode.R;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleViewData);

        //Adapter


        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter();

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(zXingScannerView != null){
            zXingScannerView.stopCamera();
        }

    }

    public void openAdd(View view){
        startActivity(new Intent(this, AddActivity.class));
    }

    public void scanQR(View view){
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }


    @Override
    public void handleResult(com.google.zxing.Result result) {
//        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();

        String url = result.getText();
        try{
            URL oracle = new URL("http://www.fazenda.pr.gov.br/nfce/qrcode/?p=41190311517841000278650390000286691391851000|2|1|2|27A225283A4A7321A02E72A95C416C05B6A33C94");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String html = ""; //<---- URL obtida pelo qr-code, verificar se contem link para a fazenda
            while (in.readLine() != null)
                html += in.readLine();
            in.close();


        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Erro durante a leitura do QR-Code", Toast.LENGTH_SHORT).show();
        }
//      permite proximas chamadas, caso contrario, camera trava no primeiro qr-code lido
        zXingScannerView.resumeCameraPreview(this);
    }

}
