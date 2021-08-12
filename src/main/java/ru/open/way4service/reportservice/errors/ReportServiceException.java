package ru.open.way4service.reportservice.errors;

public class ReportServiceException extends RuntimeException {   
    private static final long serialVersionUID = 1L;

    public ReportServiceException() {
        super();
    }
    
    public ReportServiceException(String message) {
        super(message);
    }
    
    public ReportServiceException(Throwable cause) {
        super("See nested exception", cause);
    }
    
    public ReportServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
