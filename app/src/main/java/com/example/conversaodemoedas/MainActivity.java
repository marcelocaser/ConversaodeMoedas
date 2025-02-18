package com.example.conversaodemoedas;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conversaodemoedas.business.RetrofitClient;
import com.example.conversaodemoedas.business.interfaces.ApiService;
import com.example.conversaodemoedas.domain.CurrencyQuoteDO;
import com.example.conversaodemoedas.entity.CotacoesTO;
import com.example.conversaodemoedas.entity.UsuariosTO;
import com.example.conversaodemoedas.model.DataModel;
import com.example.conversaodemoedas.model.adpter.DataAdapter;
import com.example.conversaodemoedas.persistence.AppDatabase;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextView valorCotacao;
    private RecyclerView recyclerView;
    private final List<DataModel> dataCotacoes = new ArrayList<>();
    private final String[] columnNames = {"R$ (Real)", "$ (Dolar Americano)"};

    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private TextView txtBarcode;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        valorCotacao = findViewById(R.id.valorCotacao);
        //Button btnAdicionar = findViewById(R.id.btnConverter);
        recyclerView = findViewById(R.id.tblCotacoes);
        txtBarcode = findViewById(R.id.txtBarcodeValue);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*btnAdicionar.setOnClickListener(v -> {
            Usuario usuario = new Usuario("João", "joao@email.com");
            db.usuarioDao().inserir(usuario);
            carregarUsuarios();
        });*/

        buscarCotacaoDolarEmReal();
        carregarBandeiraPais();
        carregarCotacoesRealizadas();
        //carregarUsuarios();

        previewView = findViewById(R.id.previewView);
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Solicitar permissão da câmera se necessário
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }
    }

    private void buscarCotacaoDolarEmReal() {

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<CurrencyQuoteDO> call = apiService.getCotacaoDolarEuroParaReal();

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

    public void buscarCotacaoEuroEmReal() {

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<CurrencyQuoteDO> call = apiService.getCotacaoDolarEuroParaReal();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyQuoteDO> call, @NonNull Response<CurrencyQuoteDO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrencyQuoteDO currencyQuoteDO = response.body();
                    //valorCotacao.setText(currencyQuoteDO.getUsdbrl().getHigh());
                    valorCotacao.setText(currencyQuoteDO.getEurbrl().getHigh());
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
        List<UsuariosTO> lista = db.usuarioDao().listarTodos();
        StringBuilder builder = new StringBuilder();
        for (UsuariosTO u : lista) {
            builder.append(u.getNome()).append(" - ").append(u.getEmail()).append("\n");
        }
        //resultado.setText(builder.toString());
    }

    private void carregarBandeiraPais() {
        ImageView imageViewDolar = findViewById(R.id.imgBandeiraDolar);
        ImageView imageViewReal = findViewById(R.id.imgBandeiraReal);
        String imageUrlBr = "https://flagsapi.com/BR/shiny/64.png";  // URL da imagem
        String imageUrlUs = "https://flagsapi.com/US/shiny/64.png";
        String imageUrlIt = "https://flagsapi.com/IT/shiny/64.png";

        Picasso.get()
                .load(imageUrlBr)
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.error)
                .into(imageViewReal);

        Picasso.get()
                .load(imageUrlIt)
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.error)
                .into(imageViewDolar);
    }

    private void carregarCotacoesRealizadas() {
        List<CotacoesTO> cotacoes = db.cotacaoDao().listarTodos();
        for (CotacoesTO cotacoesTO : cotacoes) {
            DataModel dataModel = new DataModel(cotacoesTO.getValorReal(), cotacoesTO.getValorDolar());
            dataCotacoes.add(dataModel);
            DataAdapter adapter = new DataAdapter(dataCotacoes, columnNames);
            recyclerView.setAdapter(adapter);
        }
    }

    public void converter(View view) {
        TextView valorReal = findViewById(R.id.valorReal);

        Double calcValorReal = Double.parseDouble(valorReal.getText().toString());
        Double calcValorCotacao = Double.parseDouble(valorCotacao.getText().toString());
        Double conversaoRealDolar = calcValorReal / calcValorCotacao;
        Log.d("API", "Resultado conversao: " + conversaoRealDolar);

        DataModel dataModel = new DataModel(calcValorReal, conversaoRealDolar);

        dataCotacoes.add(dataModel);

        DataAdapter adapter = new DataAdapter(dataCotacoes, columnNames);
        recyclerView.setAdapter(adapter);
        //resultado.setText(r);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, image -> {
                    analyzeImage(image);
                });

                Camera camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalysis);

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public void salvarCotacoes(View view) {
        dataCotacoes.stream().forEach(cotacao -> {
            CotacoesTO cotacoesTO = db.cotacaoDao().consultarValorEmReal(cotacao.getValorReal());
            if (cotacoesTO == null) {
                cotacoesTO = new CotacoesTO();
                cotacoesTO.setValorDolar(cotacao.getValorDolar());
                cotacoesTO.setValorReal(cotacao.getValorReal());
                db.cotacaoDao().inserir(cotacoesTO);
                Toast.makeText(this, "Cotação salva com sucesso", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processImage(ImageProxy image) {
        // Aqui você pode adicionar código para leitura de código de barras
    }

    private void analyzeImage(ImageProxy imageProxy) {
        @SuppressWarnings("UnsafeOptInUsageError")
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            BarcodeScanner scanner = BarcodeScanning.getClient();
            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode : barcodes) {
                            String valor = barcode.getRawValue();
                            if (valor != null) {
                                runOnUiThread(() -> {
                                    txtBarcode.setText(valor);
                                    //Toast.makeText(this, "Código: " + valor, Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    })
                    .addOnCompleteListener(task -> imageProxy.close());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
        }
    }
}