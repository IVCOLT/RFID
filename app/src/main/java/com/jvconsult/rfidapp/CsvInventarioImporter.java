package com.jvconsult.rfidapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvInventarioImporter {

    public static List<InventarioItem> loadFromFile(File file) {
        List<InventarioItem> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }  // Ignora cabeçalho
                InventarioItem item = InventarioItem.fromCsvLine(line);
                if (item != null) lista.add(item);
            }
        } catch (Exception e) {
            Log.e("CSV", "Erro lendo CSV: " + e.getMessage(), e);
        }

        return lista;
    }
}
