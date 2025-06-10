package com.jvconsult.rfidapp;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    public static void exportToCsv(List<String> dataList, String fileName) {
        try {
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, fileName);

            // Verifica se arquivo existe e se está vazio para decidir se escreve cabeçalho
            boolean writeHeader = !file.exists() || file.length() == 0;

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));  // modo append

            if (writeHeader) {
                // Assume que a primeira linha da lista é o cabeçalho, escreve só uma vez
                if (!dataList.isEmpty()) {
                    bw.write(dataList.get(0));
                    bw.newLine();
                }
            }

            // Escreve as linhas de dados pulando o cabeçalho (índice 0)
            for (int i = 1; i < dataList.size(); i++) {
                bw.write(dataList.get(i));
                bw.newLine();
            }

            bw.flush();
            bw.close();

            Log.d("CSV Export", "Arquivo exportado para: " + file.getAbsolutePath());

        } catch (IOException e) {
            Log.e("CSV Export", "Erro ao exportar CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
