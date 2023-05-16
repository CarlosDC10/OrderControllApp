package tfg.isca.ordercontrol.Pojos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private String cliente;
    private String fechaEntrega;
    private String muelle;
    private String estado;
    private String unidad;

    private List<Integer> lineasPedido = new ArrayList<>();

    public Pedido(int id, String cliente, String fechaEntrega, String muelle, String estado, String unidad, List<Integer> lineas){
        this.id = id;
        this.cliente = cliente;
        this.fechaEntrega = fechaEntrega;
        this.muelle = muelle;
        this.estado = estado;
        this.unidad = unidad;
        this.lineasPedido = lineas;
    }

    public String getMuelle() {
        return muelle;
    }

    public void setMuelle(String muelle){
        this.muelle = muelle;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Integer> getLineasPedido() {
        return lineasPedido;
    }

    public void setLineasPedido(List<Integer> lineasPedido) {
        this.lineasPedido = lineasPedido;
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
}
