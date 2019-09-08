package com.example.gerficode.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.gerficode.Adapters.AdapterNF;
import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Helpers.HTML_Dealer;
import com.example.gerficode.R;



import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    private RecyclerView recyclerView;
    private List<NotaFiscal> notaFiscalDTOList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleViewData);

        //Adapter
        AdapterNF adapter = new AdapterNF(notaFiscalDTOList);

        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true); //Reverte a exibixão dos dados do RecyclerView, verificar funcionamento

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

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
        String url = result.getText();

        new HTML_Dealer(getApplicationContext(), url);

//      permite proximas chamadas, caso contrario, camera trava após primeira solicitação
        zXingScannerView.resumeCameraPreview(this);
    }

}
