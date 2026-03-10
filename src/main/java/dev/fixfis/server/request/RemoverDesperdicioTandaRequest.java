package dev.fixfis.server.request;

public class RemoverDesperdicioTandaRequest {
    private Long idTanda;
    private Long idDesperdicio;

    public Long getIdTanda() {
        return idTanda;
    }

    public void setIdTanda(Long idTanda) {
        this.idTanda = idTanda;
    }

    public Long getIdDesperdicio() {
        return idDesperdicio;
    }

    public void setIdDesperdicio(Long idDesperdicio) {
        this.idDesperdicio = idDesperdicio;
    }
}
