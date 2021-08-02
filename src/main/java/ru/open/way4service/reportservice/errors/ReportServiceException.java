package ru.open.way4service.reportservice.errors;

public class ReportServiceException extends Exception {   
    private static final long serialVersionUID = 1L;

    public ReportServiceException() {
        super();
    }
    
    public ReportServiceException(String message) {
        super(message);
    }
    
    public ReportServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
