package com.jvconsult.rfidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelecionarSetorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_setor);

        // Recebe a loja selecionada da tela anterior
        String lojaSelecionada = getIntent().getStringExtra("LOJA_SELECIONADA");
        List<InventarioItem> todosItens = InventarioData.getInstance().getInventario();

        // Pega os setores únicos daquela loja
        Set<String> setores = new HashSet<>();
        for (InventarioItem item : todosItens) {
            if (item.loja != null && item.loja.equals(lojaSelecionada)
                    && item.codlocalizacao != null && !item.codlocalizacao.isEmpty()) {
                setores.add(item.codlocalizacao);
            }
        }

        // Se só houver um setor, já pula pra MainActivity direto
        if (setores.size() == 1) {
            String setorUnico = setores.iterator().next();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("LOJA_SELECIONADA", lojaSelecionada);
            intent.putExtra("SETOR_SELECIONADO", setorUnico);
            startActivity(intent);
            finish();
            return;
        }

        // Lista todos os setores na tela
        List<String> listaSetores = new ArrayList<>(setores);
        ListView listView = findViewById(R.id.listViewSetores);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaSetores);
        listView.setAdapter(adapter);

        // Quando clicar em um setor, vai para MainActivity (leitura RFID)
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String setorSelecionado = listaSetores.get(position);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("LOJA_SELECIONADA", lojaSelecionada);
            intent.putExtra("SETOR_SELECIONADO", setorSelecionado);
            startActivity(intent);
            finish();
        });
    }
}
