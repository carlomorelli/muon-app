package com.csoft.muon.domain;

import java.util.List;

/**
 * Health collection entity
 * 
 * @author Carlo Morelli
 *
 */
public class HealthsDto {

    private List<Health> components;

    public HealthsDto() {
    }

    public HealthsDto(List<Health> components) {
        this.components = components;
    }
    
    public List<Health> getComponents() {
        return components;
    }
}
