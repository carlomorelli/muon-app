package com.csoft.muon.dao;

public class DaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public DaoException() {
        super();
    }
    public DaoException(String string) {
        super(string);
    }
    public DaoException(String string, Throwable throwable) {
        super(string, throwable);
    }

}
