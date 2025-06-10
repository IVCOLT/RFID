package com.jvconsult.rfidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.ArrayList;


public class SelecionarLojaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_loja);

        List<String> lojas = InventarioData.getInstance().getLojasUnicas();

        ListView listView = findViewById(R.id.listViewLojas);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lojas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String lojaSelecionada = lojas.get(position);

            // Filtra todos os nroplaqueta dessa loja
            List<InventarioItem> todosItens = InventarioData.getInstance().getInventario();
            ArrayList<String> plaquetasDaLoja = new ArrayList<>();

            for (InventarioItem item : todosItens) {
                if (item.loja != null && item.loja.equals(lojaSelecionada)) {
                    if (item.nroplaqueta != null && !item.nroplaqueta.isEmpty()) {
                        plaquetasDaLoja.add(item.nroplaqueta);
                    }
                }
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("LOJA_SELECIONADA", lojaSelecionada);
            intent.putStringArrayListExtra("PLAQUETAS_LOJA", plaquetasDaLoja);
            startActivity(intent);
        });
    }
}

