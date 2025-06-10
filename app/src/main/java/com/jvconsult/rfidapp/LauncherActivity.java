package com.jvconsult.rfidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    public void goToLeituraRFID(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void goToCsvTable(View view) {
        startActivity(new Intent(this, CsvTableActivity.class));
    }

    public void onImportarPlanilha(View view) {
        PlanilhaHelper.importarPlanilha(this);
    }

    public void goToInventario(View view) {
        startActivity(new Intent(this, SelecionarLojaActivity.class));
    }

}
