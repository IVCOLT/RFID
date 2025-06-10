package com.jvconsult.rfidapp;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CsvTableActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> dataList = new ArrayList<>();
    private static final String FILE_NAME = "tags_lidas.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv_table);

        listView = findViewById(R.id.csvListView);

        loadCsvData();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dataList
        );

        listView.setAdapter(adapter);
    }

    private void loadCsvData() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
        if (!file.exists()) {
            dataList.add("Arquivo CSV não encontrado.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        } catch (Exception e) {
            Log.e("CSV Read", "Erro ao ler arquivo: " + e.getMessage());
            dataList.add("Erro ao ler o CSV.");
        }
    }
}
