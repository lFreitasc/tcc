package com.example.gerficode.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.gerficode.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class activity_camera extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(zXingScannerView != null){
            zXingScannerView.stopCamera();
        }

    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        String url = result.getText();

//      permite proximas chamadas, quando sem, valor retornado erroneamente
        zXingScannerView.resumeCameraPreview(this);
        zXingScannerView.stopCamera();
        zXingScannerView.removeAllViews();
        //setContentView(R.layout.activity_main);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("URL", url);
        setResult(1, resultIntent);

        finish();
    }


}
