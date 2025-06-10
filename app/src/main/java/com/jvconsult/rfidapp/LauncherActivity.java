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

    // Vai para a MainActivity
    public void goToLeituraRFID(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    // Vai para a CsvTableActivity
    public void goToCsvTable(View view) {
        startActivity(new Intent(this, CsvTableActivity.class));
    }
}
