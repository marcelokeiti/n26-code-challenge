package com.n26.exception;

/**
 * This exception is thrown when trying to create an FinancialTransaction older
 * than 60 seconds.
 */
public class ExpiredTransactionException extends RuntimeException {

    private static final long serialVersionUID = -2195308821164141119L;

    public ExpiredTransactionException() {
	super();
    }

    public ExpiredTransactionException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExpiredTransactionException(String message, Throwable cause) {
	super(message, cause);
    }

    public ExpiredTransactionException(String message) {
	super(message);
    }

    public ExpiredTransactionException(Throwable cause) {
	super(cause);
    }

}
