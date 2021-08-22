package ru.open.way4service.reportservice.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ReportConfig;
import ru.open.way4service.reportservice.services.ReportExecutorService;
import ru.open.way4service.reportservice.services.ReportLoaderService;

@RestController
@RequestMapping("/report-service")
@Tag(name = "Report controller", description = "Provides an interface for running a report on the server")
public class ReportController {
    private Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    ReportExecutorService reportExecutor;

    @Autowired
    ReportLoaderService reportLoaderService;

    @PostMapping(value = "/report/execute/{reportId}", produces = "application/json")
    @Operation(summary = "Provides the ability to run a report", description = "Provides execute report. Report run in thread pool. Set reportId in service path and put in request body map of report properties. If thread pool is overflowing returned status 503")
    public void executeReport(
            @Parameter(name = "reportId", description = "Report id in service configuration DB", example = "123") @PathVariable("reportId") long id,
            @Parameter(name = "properties", description = "Properties map for running the report") @RequestBody Map<String, Object> properties) {
        try {
            long requestNumber = System.currentTimeMillis();
            logger.info(String.format("Start execute report by id [%s], request number [%s]", id, requestNumber));
            ReportConfig reportConfig = reportLoaderService.getReportConfig(id);
            reportExecutor.executeReport(requestNumber, reportConfig, properties).get();
        } catch (Exception ex) {
            throw new ReportServiceException(ex);
        }
    }

    @PostMapping(value = "/report/execute-async/{reportId}", produces = "application/json")
    @Operation(summary = "Provides the ability to run a report async", description = "Provides execute report async. Report run in thread pool. Set reportId in service path and put in request body map of report properties. If thread pool is overflowing returned status 503")
    public void executeReportAsync(
            @Parameter(name = "reportId", description = "Report id in service configuration DB", example = "123") @PathVariable("reportId") long id,
            @Parameter(name = "properties", description = "Properties map for running the report") @RequestBody Map<String, Object> properties) {
        try {
            long requestNumber = System.currentTimeMillis();
            logger.info(String.format("Report id [%s] with request number [%s]. Start report export", id, requestNumber));
            ReportConfig reportConfig = reportLoaderService.getReportConfig(id);
            logger.trace(String.format("Report id [%s] with request number [%s]. Report config is loaded", id, requestNumber));
            reportExecutor.executeReport(requestNumber, reportConfig, properties);
        } catch (Exception ex) {
            throw new ReportServiceException(ex);
        }
    }
}
