package com.csoft.muon.repository;

/**
 * Repository exception for error handling in Repository interface operations
 * 
 * @author Carlo Morelli
 *
 */
public class RepositoryException extends Exception {

    private static final long serialVersionUID = 1L;

    public RepositoryException() {
        super();
    }

    public RepositoryException(String string) {
        super(string);
    }

    public RepositoryException(String string, Throwable throwable) {
        super(string, throwable);
    }

}
