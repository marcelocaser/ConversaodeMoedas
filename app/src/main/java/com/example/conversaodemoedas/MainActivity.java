package com.example.conversaodemoedas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conversaodemoedas.business.RetrofitClient;
import com.example.conversaodemoedas.business.interfaces.ApiService;
import com.example.conversaodemoedas.domain.CurrencyQuoteDO;
import com.example.conversaodemoedas.entity.Usuario;
import com.example.conversaodemoedas.model.DataModel;
import com.example.conversaodemoedas.model.adpter.DataAdapter;
import com.example.conversaodemoedas.persistence.AppDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextView valorCotacao;
    private RecyclerView recyclerView;
    private final List<DataModel> dataCotacoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        valorCotacao = findViewById(R.id.valorCotacao);
        //Button btnAdicionar = findViewById(R.id.btnConverter);
        recyclerView = findViewById(R.id.tblCotacoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*btnAdicionar.setOnClickListener(v -> {
            Usuario usuario = new Usuario("João", "joao@email.com");
            db.usuarioDao().inserir(usuario);
            carregarUsuarios();
        });*/

        buscarCotacaoDolarEmReal();
        carregarBandeiraPais();
        //carregarUsuarios();
    }

    public void buscarCotacaoDolarEmReal() {

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<CurrencyQuoteDO> call = apiService.getCotacaoDolarParaReal();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyQuoteDO> call, @NonNull Response<CurrencyQuoteDO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrencyQuoteDO currencyQuoteDO = response.body();
                    valorCotacao.setText(currencyQuoteDO.getUsdbrl().getHigh());
                    Log.d("API", "Moeda: " + currencyQuoteDO.getUsdbrl().getName());
                } else {
                    Log.e("API", "Erro na resposta: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CurrencyQuoteDO> call, @NonNull Throwable t) {
                Log.e("API", "Erro na chamada", t);
            }
        });

    }

    private void carregarUsuarios() {
        List<Usuario> lista = db.usuarioDao().listarTodos();
        StringBuilder builder = new StringBuilder();
        for (Usuario u : lista) {
            builder.append(u.getNome()).append(" - ").append(u.getEmail()).append("\n");
        }
        //resultado.setText(builder.toString());
    }

    private void carregarBandeiraPais() {
        ImageView imageViewDolar = findViewById(R.id.imgBandeiraDolar);
        ImageView imageViewReal = findViewById(R.id.imgBandeiraReal);
        String imageUrlBr = "https://flagsapi.com/BR/shiny/64.png";  // URL da imagem
        String imageUrlUs = "https://flagsapi.com/US/shiny/64.png";

        Picasso.get()
                .load(imageUrlBr)
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.error)
                .into(imageViewReal);

        Picasso.get()
                .load(imageUrlUs)
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.error)
                .into(imageViewDolar);
    }

    public void converter(View view) {
        TextView valorReal = findViewById(R.id.valorReal);

        Double calcValorReal = Double.parseDouble(valorReal.getText().toString());
        Double calcValorCotacao = Double.parseDouble(valorCotacao.getText().toString());
        Double conversaoRealDolar = calcValorReal / calcValorCotacao;
        Log.d("API", "Resultado conversao: " + conversaoRealDolar);

        DataModel dataModel = new DataModel(calcValorReal, conversaoRealDolar);

        dataCotacoes.add(dataModel);

        // Nomes das colunas
        String[] columnNames = {"R$ (Real)", "$ (Dolar Americano)"};

        DataAdapter adapter = new DataAdapter(dataCotacoes, columnNames);
        recyclerView.setAdapter(adapter);
        //resultado.setText(r);
    }
}