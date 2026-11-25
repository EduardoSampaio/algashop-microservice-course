package com.algaworks.algashop.ordering.exception;

public class CustomerArchivedException extends  DomainException {
    public CustomerArchivedException(String message) {
        super(message);
    }

    CustomerArchivedException(String message, Throwable cause) {
        super(message, cause);
    }
}
