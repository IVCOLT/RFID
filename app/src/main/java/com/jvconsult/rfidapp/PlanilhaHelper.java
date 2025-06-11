package com.jvconsult.rfidapp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlanilhaHelper {

    public static void importarPlanilha(Context context) {
        // Caminho do arquivo na pasta pública Downloads
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "inventario.csv");

        if (!file.exists()) {
            Toast.makeText(context, "Planilha não encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Método estático para fazer o parse manual do CSV (na MainActivity)
            List<String[]> linhas = MainActivity.parseCSVManual(file);

            boolean primeira = true;
            List<InventarioItem> inventarioCompleto = new ArrayList<>();

            for (String[] campos : linhas) {
                if (primeira) {
                    primeira = false; // pula o cabeçalho
                    continue;
                }
                if (campos.length >= 12) {
                    // Cria o item do inventário a partir da linha CSV
                    InventarioItem item = InventarioItem.fromCsvLine(String.join(",", campos));
                    if (item != null) {
                        inventarioCompleto.add(item);
                    }
                }
            }

            // Salva a lista completa no singleton para uso no app
            InventarioData.getInstance().setInventario(inventarioCompleto);

            Toast.makeText(context, "Planilha importada: " + inventarioCompleto.size() + " itens.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("PlanilhaHelper", "Erro ao importar planilha: " + e.getMessage());
            Toast.makeText(context, "Erro ao importar a planilha.", Toast.LENGTH_SHORT).show();
        }
    }
}
