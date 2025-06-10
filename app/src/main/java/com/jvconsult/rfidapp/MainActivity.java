package com.jvconsult.rfidapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.pda.rfid.EPCModel;
import com.pda.rfid.IAsynchronousMessage;
import com.pda.rfid.uhf.UHFReader;
import com.port.Adapt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MainActivity extends AppCompatActivity implements IAsynchronousMessage {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSIONS = 100;

    private ListView list;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> listData = new ArrayList<>();
    private HashMap<String, EPCModel> hmList = new HashMap<>();
    private final Object lock = new Object();

    private boolean isOpened = false;
    private boolean isReading = false;

    // Dados da planilha importada (por nroplaqueta)
    private Set<String> planilhaTags = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.ltEPCs);

        simpleAdapter = new SimpleAdapter(
                this,
                listData,
                R.layout.item_tag,
                new String[]{"EPC", "Status"},
                new int[]{R.id.tvEpc, R.id.tvCount}
        );
        list.setAdapter(simpleAdapter);

        checkPermissions();
    }

    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_PHONE_STATE);
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), REQUEST_PERMISSIONS);
        } else {
            initUHF();
            importarPlanilha();
        }
    }

    private void initUHF() {
        Adapt.init(this);
        isOpened = UHFReader.getUHFInstance().OpenConnect(this);
        if (!isOpened) {
            Log.d(TAG, "Falha ao abrir conexão UHF");
        }
        UHFReader._Config.SetEPCBaseBandParam(255, 0, 1, 0);
        UHFReader._Config.SetANTPowerParam(1, 20);
    }

    private void importarPlanilha() {
        File file = new File("/storage/emulated/0/pda/BASE GERAL PARA INVENTARIO COM RFID - 08ABRIL2025 - 08ABRIEL202591).csv");
        if (!file.exists()) {
            Toast.makeText(this, "Planilha não encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            List<String[]> linhas = parseCSVManual(file);
            boolean primeira = true;

            for (String[] campos : linhas) {
                if (primeira) { // pular cabeçalho
                    primeira = false;
                    continue;
                }
                if (campos.length >= 10) {
                    String nroPlaqueta = campos[9].trim();
                    if (!nroPlaqueta.isEmpty()) {
                        planilhaTags.add(nroPlaqueta);
                    }
                }
            }
            Toast.makeText(this, "Planilha importada: " + planilhaTags.size() + " itens.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao ler CSV manual: " + e.getMessage());
            Toast.makeText(this, "Erro ao ler a planilha.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRead(View v) {
        if (!isOpened || isReading) return;
        isReading = UHFReader._Tag6C.GetEPC(1, 1) == 0;
    }

    public void onStop(View v) {
        if (!isOpened || !isReading) return;
        UHFReader.getUHFInstance().Stop();
        isReading = false;
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void OutPutEPC(EPCModel model) {
        try {
            synchronized (lock) {
                String epc = model._EPC.trim();

                if (!hmList.containsKey(epc)) {
                    model._TotalCount = 1;
                    hmList.put(epc, model);

                    Map<String, Object> map = new HashMap<>();
                    map.put("EPC", epc);

                    if (planilhaTags.contains(epc)) {
                        map.put("Status", "✔ Encontrado");
                    } else {
                        map.put("Status", "❌ Não encontrado");
                    }

                    listData.add(map);

                    runOnUiThread(() -> simpleAdapter.notifyDataSetChanged());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro no EPC: " + e.getMessage(), e);
        }
    }

    public void exportTagsToCsv(View v) {
        synchronized (lock) {
            if (hmList.isEmpty()) {
                Toast.makeText(this, "Nenhuma tag lida.", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> exportData = new ArrayList<>();
            exportData.add("RFID_TAG,Status");

            for (Map<String, Object> item : listData) {
                String epc = (String) item.get("EPC");
                String status = (String) item.get("Status");
                exportData.add(epc + "," + status);
            }

            CsvExporter.exportToCsv(exportData, "resultado_inventario.csv");
            startActivity(new Intent(this, CsvTableActivity.class));
            Toast.makeText(this, "Exportado com sucesso.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        UHFReader.getUHFInstance().CloseConnect();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 && !isReading) {
            onRead(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 139 && isReading) {
            onStop(null);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            checkPermissions();
        }
    }

    // Parser manual de CSV para considerar aspas e vírgulas internas
    public static List<String[]> parseCSVManual(File file) throws IOException {
        List<String[]> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(parseLine(line));
        }
        br.close();
        return lines;
    }

    private static String[] parseLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim());
        return result.toArray(new String[0]);
    }
}
