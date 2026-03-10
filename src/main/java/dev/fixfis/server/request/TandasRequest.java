package dev.fixfis.server.request;

import dev.fixfis.server.Metrics;

import java.util.UUID;

public class TandasRequest {
    private UUID uuid = Metrics.userUUID;
    private Long lugar;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getLugar() {
        return lugar;
    }

    public void setLugar(Long lugar) {
        this.lugar = lugar;
    }
}
