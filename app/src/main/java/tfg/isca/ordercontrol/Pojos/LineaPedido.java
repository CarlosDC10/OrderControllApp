package tfg.isca.ordercontrol.Pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LineaPedido implements Serializable {
    private int id;
    private int cantidad;
    private String TipoPaquete;
    private boolean compleatada;
    private String unidad;
    private List<Integer> lineasPreparadas = new ArrayList<>();
    private int cantidadActual;

    public LineaPedido(){}
    public LineaPedido(int id, int cantidad, String tipoPaquete, boolean compleatada, String unidad, List<Integer> lineasPreapradas, int cantidadActual) {
        this.id = id;
        this.cantidad = cantidad;
        TipoPaquete = tipoPaquete;
        this.compleatada = compleatada;
        this.unidad = unidad;
        this.lineasPreparadas = lineasPreapradas;
        this.cantidadActual = cantidadActual;
    }

    public boolean isCompleatada() {
        return compleatada;
    }

    public void setCompleatada(boolean compleatada) {
        this.compleatada = compleatada;
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

    public List<Integer> getLineasPreparadas() {
        return lineasPreparadas;
    }

    public void setLineasPreparadas(List<Integer> lineasPreparadas) {
        this.lineasPreparadas = lineasPreparadas;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }
}
