package com.example.gerficode.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.gerficode.Adapters.AdapterNF;
import com.example.gerficode.Database.Database;
import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Helpers.HTML_Dealer;
import com.example.gerficode.R;



import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    private RecyclerView recyclerView;
    private List<NotaFiscal> notaFiscalList = new ArrayList<>();
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Database.getDatabase(MainActivity.this);
        recyclerView = findViewById(R.id.recycleViewData);

        //Adapter
        notaFiscalList = database.notaFiscalDAO().getAll();
        AdapterNF adapter = new AdapterNF(notaFiscalList);

        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true); //Reverte a exibixão dos dados do RecyclerView, verificar funcionamento

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

    //Permite retornar ao Menu principal sempre que apertar o botão back, usado como rota de escape da camera.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            setContentView(R.layout.activity_main);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        String url = result.getText();


        new HTML_Dealer(getApplicationContext(), url);

//      permite proximas chamadas, quando sem, valor retornado erroneamente
        zXingScannerView.resumeCameraPreview(this);
        zXingScannerView.stopCamera();
        setContentView(R.layout.activity_main);
    }

}
