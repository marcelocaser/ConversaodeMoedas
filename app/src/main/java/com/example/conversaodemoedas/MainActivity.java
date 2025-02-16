package com.example.conversaodemoedas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.conversaodemoedas.entity.Usuario;
import com.example.conversaodemoedas.persistence.AppDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        resultado = findViewById(R.id.txtResultado);
        Button btnAdicionar = findViewById(R.id.btnConverter);

        btnAdicionar.setOnClickListener(v -> {
            Usuario usuario = new Usuario("João", "joao@email.com");
            db.usuarioDao().inserir(usuario);
            carregarUsuarios();
        });

        carregarUsuarios();
    }

    public void converter(View view) {
        TextView valorReal = findViewById(R.id.valorReal);
        TextView valorCotacao = findViewById(R.id.valorCotacao);

        double calcValorReal = Double.parseDouble(valorReal.getText().toString());
        double calcValorCotacao = Double.parseDouble(valorCotacao.getText().toString());
        String r = String.format("%s", calcValorReal * calcValorCotacao);
        resultado.setText(r);
    }

    private void carregarUsuarios() {
        List<Usuario> lista = db.usuarioDao().listarTodos();
        StringBuilder builder = new StringBuilder();
        for (Usuario u : lista) {
            builder.append(u.getNome()).append(" - ").append(u.getEmail()).append("\n");
        }
        resultado.setText(builder.toString());
    }
}