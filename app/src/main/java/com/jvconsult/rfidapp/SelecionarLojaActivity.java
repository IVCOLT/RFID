package com.jvconsult.rfidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SelecionarLojaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_loja);

        // Busca as lojas únicas do inventário importado
        List<String> lojas = InventarioData.getInstance().getLojasUnicas();

        // Configura o ListView para exibir as lojas
        ListView listView = findViewById(R.id.listViewLojas);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lojas);
        listView.setAdapter(adapter);

        // Quando clicar em uma loja, vai para SelecionarSetorActivity
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String lojaSelecionada = lojas.get(position);

            Intent intent = new Intent(this, SelecionarSetorActivity.class);
            intent.putExtra("LOJA_SELECIONADA", lojaSelecionada);
            startActivity(intent);
        });
    }
}

