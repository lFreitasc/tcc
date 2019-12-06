package com.example.gerficode.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.gerficode.Adapters.AdapterNF;
import com.example.gerficode.Database.Database;
import com.example.gerficode.Entity.NotaFiscal;
import com.example.gerficode.Helpers.HTML_Dealer;
import com.example.gerficode.Helpers.RecyclerItemClickListener;
import com.example.gerficode.R;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<NotaFiscal> notaFiscalList = new ArrayList<>();
    private Database database;
    static final int CAMERA_INTENT_CODE = 13;
    static final int INTENT_CODE_UPDATE = 15;

    AdapterNF adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("Lucas","Inicio");
        database = Database.getDatabase(MainActivity.this);


        notaFiscalList = database.notaFiscalDAO().getAll();
        defineRecyclerView();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        notaFiscalList = database.notaFiscalDAO().getAll();
        adapter.notifyDataSetChanged();
    }


    public void defineRecyclerView(){
        Collections.reverse(notaFiscalList);
        recyclerView = findViewById(R.id.recycleViewData);
        adapter = new AdapterNF(notaFiscalList);

        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position)
                            {
                                Intent intent = new Intent(getApplicationContext(), activity_addManually.class);
                                int indexPos = notaFiscalList.size();
                                Long idNotaFiscal =  notaFiscalList.get(indexPos - position - 1).getId();
                                intent.putExtra("idNotaFiscal",idNotaFiscal);
                                startActivityForResult(intent,INTENT_CODE_UPDATE);
                            }


                            @Override
                            public void onLongItemClick(View view, int position) {
//                                Toast.makeText(getApplication().getBaseContext(), db.heroDAO().getHeroes().get(position),Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    public void openAdd(View view){
        startActivityForResult(new Intent(this, activity_addManually.class),INTENT_CODE_UPDATE);
    }

    public void scanQR(View view){

        Intent intencao = new Intent(this, activity_camera.class);
        startActivityForResult(intencao, CAMERA_INTENT_CODE);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_INTENT_CODE && data != null){
            String url = (String) data.getExtras().getSerializable("URL");
            new HTML_Dealer(getApplicationContext(), url);
        }
        notaFiscalList = database.notaFiscalDAO().getAll();
        defineRecyclerView();
    }
}
