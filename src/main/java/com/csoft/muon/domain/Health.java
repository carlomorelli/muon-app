package com.csoft.muon.domain;

/**
 * Health component entity
 * 
 * @author Carlo Morelli
 *
 */
public final class Health {

    private String name;
    private String status;

    public Health() {
    }

    public Health(final String name, final String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

}