package ru.open.way4service.reportservice.services;

import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.open.way4service.reportservice.errors.ReportServiceException;

@Service
public class ReportExecutorServiceImpl implements ReportExecutorService {
    
    @Async("taskExecutor")
    public void executeReport(Map<String, String> poroperties) throws ReportServiceException {
        
    }
}
