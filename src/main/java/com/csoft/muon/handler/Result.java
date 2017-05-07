package com.csoft.muon.handler;

public class Result {

    private int status;
    private String body;
    
    public Result(int status, String body) {
        super();
        this.status = status;
        this.body = body;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getBody() {
        return body;
    }

    
}
