package ru.open.way4service.reportservice.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.repositories.reports.ReportsRepository;
import ru.open.way4service.reportservice.repositories.service.ServiceRepository;
import ru.open.way4service.reportservice.services.ReportExecutorService;

@RestController
@RequestMapping("/report-service")
@Tag(name = "Report controller", description = "Provides an interface for running a report on the server")
public class ReportController {

    @Autowired
    ReportExecutorService reportExecutor;
    
    @Autowired
    ServiceRepository serviceRepository;
    
    @Autowired
    ReportsRepository reportsRepository;

    @GetMapping(value = "/report/execute", produces = "application/json")
    @Operation(summary = "Run report for execute", description = "Provides execute report. Report run in thread pool")
    public void executeReport(/*Map<String, String> properties*/) throws ReportServiceException {
        
        serviceRepository.test();
        reportsRepository.test();
        //reportExecutor.executeReport(properties);
    }
}
