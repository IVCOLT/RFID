package com.jvconsult.rfidapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LauncherActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMPORT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    public void goToLeituraRFID(android.view.View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void goToCsvTable(android.view.View view) {
        startActivity(new Intent(this, CsvTableActivity.class));
    }

    public void onImportarPlanilha(android.view.View view) {
        // Abre o seletor de arquivos para CSV
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_IMPORT);
    }

    public void goToInventario(android.view.View view) {
        startActivity(new Intent(this, SelecionarLojaActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMPORT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();

                try {
                    // Copia o arquivo para a pasta interna para ser lido depois
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    File file = new File(getExternalFilesDir(null), "inventario.csv");
                    OutputStream outputStream = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.close();
                    inputStream.close();

                    // Agora importa usando sua classe PlanilhaHelper
                    PlanilhaHelper.importarPlanilha(this);

                    Toast.makeText(this, "Planilha importada com sucesso!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.e("LauncherActivity", "Erro ao importar planilha", e);
                    Toast.makeText(this, "Erro ao importar a planilha", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
