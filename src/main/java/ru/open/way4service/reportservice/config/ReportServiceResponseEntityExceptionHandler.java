package ru.open.way4service.reportservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.open.way4service.reportservice.errors.ReportServiceException;

@RestControllerAdvice
public class ReportServiceResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ReportServiceResponseEntityExceptionHandler.class);
    
    public ReportServiceResponseEntityExceptionHandler() {
        super();
    }
    
    @ExceptionHandler({ ReportServiceException.class })
    public ResponseEntity<Object> handleInternal(final Exception ex, final WebRequest request) {
        
        if(ex.getMessage() != null && ex.getMessage().equals("See nested exception")) {
            logger.error(ex.getClass().getName() + " " + ex.getCause().getMessage());
            if(ex.getCause() instanceof TaskRejectedException) {
                return handleExceptionInternal(ex, "", new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
            }
        } else {
            logger.error(ex.getClass().getName() + " " + ex.getMessage());
        }
        
        return handleExceptionInternal(ex, "", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
