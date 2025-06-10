package com.jvconsult.rfidapp;

public class InventarioItem {
    public String loja;
    public String sqbem;
    public String codgrupo;
    public String codlocalizacao;
    public String nrobem;
    public String nroincorp;
    public String descresumida;
    public String descdetalhada;
    public String qtdbem;
    public String nroplaqueta;
    public String nroseriebem;
    public String modelobem;

    public static InventarioItem fromCsvLine(String line) {
        String[] parts = line.split(",", -1);  // -1 mantém campos vazios
        if (parts.length < 12) return null;

        InventarioItem item = new InventarioItem();
        item.loja = parts[0];
        item.sqbem = parts[1];
        item.codgrupo = parts[2];
        item.codlocalizacao = parts[3];
        item.nrobem = parts[4];
        item.nroincorp = parts[5];
        item.descresumida = parts[6];
        item.descdetalhada = parts[7];
        item.qtdbem = parts[8];
        item.nroplaqueta = parts[9];
        item.nroseriebem = parts[10];
        item.modelobem = parts[11];
        return item;
    }

}
