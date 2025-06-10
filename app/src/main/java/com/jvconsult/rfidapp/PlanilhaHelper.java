package com.jvconsult.rfidapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;



public class PlanilhaHelper {

    public static void importarPlanilha(Context context) {
        File file = new File(context.getExternalFilesDir(null), "BaseNovo.csv");

        if (!file.exists()) {
            Toast.makeText(context, "Planilha não encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            List<String[]> linhas = MainActivity.parseCSVManual(file);
            boolean primeira = true;
            List<InventarioItem> inventarioCompleto = new ArrayList<>();

            for (String[] campos : linhas) {
                if (primeira) {
                    primeira = false;
                    continue;
                }
                if (campos.length >= 12) {
                    InventarioItem item = InventarioItem.fromCsvLine(String.join(",", campos));
                    if (item != null) {
                        inventarioCompleto.add(item);
                    }
                }
            }

            // Salva no Singleton
            InventarioData.getInstance().setInventario(inventarioCompleto);

            Toast.makeText(context, "Planilha importada: " + inventarioCompleto.size() + " itens.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("PlanilhaHelper", "Erro ao importar planilha: " + e.getMessage());
            Toast.makeText(context, "Erro ao importar a planilha.", Toast.LENGTH_SHORT).show();
        }
    }
}

