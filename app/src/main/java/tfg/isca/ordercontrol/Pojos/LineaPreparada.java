package tfg.isca.ordercontrol.Pojos;

import java.io.Serializable;

public class LineaPreparada implements Serializable {
    private int cantidad;
    private int lote;
    private String TipoPaquete;
    private String unidad;

    public LineaPreparada(){}

    public LineaPreparada(int cantidad, int lote, String tipoPaquete,String unidad) {
        this.cantidad = cantidad;
        this.lote = lote;
        this.TipoPaquete = tipoPaquete;
        this.unidad = unidad;
    }

    public String getTipoPaquete() {
        return TipoPaquete;
    }

    public void setTipoPaquete(String tipoPaquete) {
        TipoPaquete = tipoPaquete;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getLote() {
        return lote;
    }

    public void setLote(int lote) {
        this.lote = lote;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
