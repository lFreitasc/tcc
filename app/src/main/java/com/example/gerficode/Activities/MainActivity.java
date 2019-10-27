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
import android.view.KeyEvent;
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
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<NotaFiscal> notaFiscalList = new ArrayList<>();
    private Database database;
    static final int CAMERA_INTENT_CODE = 13;
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
    protected void onRestart() {
        super.onRestart();
        defineRecyclerView();
    }

    public void defineRecyclerView(){
        recyclerView = findViewById(R.id.recycleViewData);
        adapter = new AdapterNF(notaFiscalList);

        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        //((LinearLayoutManager) layoutManager).setStackFromEnd(true); //Reverte a exibixão dos dados do RecyclerView, verificar funcionamento

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
                                Long idNotaFiscal = database.notaFiscalDAO().getAll().get(position).getId();
                                intent.putExtra("idNotaFiscal",idNotaFiscal);
                                startActivity(intent);
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
        startActivity(new Intent(this, activity_addManually.class));
    }

    public void scanQR(View view){

        Intent intencao = new Intent(this, activity_camera.class);
        startActivityForResult(intencao, CAMERA_INTENT_CODE);




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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_INTENT_CODE && data != null){
            String url = (String) data.getExtras().getSerializable("URL");
            new HTML_Dealer(getApplicationContext(), url);
            adapter.notifyDataSetChanged();
        }

    }
}
