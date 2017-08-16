package com.csoft.muon.domain;

public final class Version {

    private String name;
    private String version;
    
    public Version(final String name, final String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

}