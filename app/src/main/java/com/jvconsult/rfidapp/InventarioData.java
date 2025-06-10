package com.jvconsult.rfidapp;

import java.util.ArrayList;
import java.util.List;

public class InventarioData {
    private static InventarioData instance;
    private List<InventarioItem> inventarioCompleto = new ArrayList<>();

    private InventarioData() {}

    public static InventarioData getInstance() {
        if (instance == null) {
            instance = new InventarioData();
        }
        return instance;
    }

    public void setInventario(List<InventarioItem> inventario) {
        this.inventarioCompleto = inventario;
    }

    public List<InventarioItem> getInventario() {
        return inventarioCompleto;
    }

    public List<String> getLojasUnicas() {
        List<String> lojas = new ArrayList<>();
        for (InventarioItem item : inventarioCompleto) {
            if (item.loja != null && !lojas.contains(item.loja)) {
                lojas.add(item.loja);
            }
        }
        return lojas;
    }
}
