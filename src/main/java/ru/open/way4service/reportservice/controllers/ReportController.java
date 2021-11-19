package ru.open.way4service.reportservice.controllers;

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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ReportConfig;
import ru.open.way4service.reportservice.models.ReportRequest;
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

    @PostMapping(value = "/report/execute/{reportId}", produces = "application/json; charset=UTF-8")
    @Operation(summary = "Provides the ability to run a report",
            responses = {@ApiResponse(responseCode = "200", description = "Report strat to execute"), @ApiResponse(responseCode = "503", description = "Thread pool is overflowing"), @ApiResponse(responseCode = "500", description = "Internal server error")},
            description = "Provides execute report. Report run in thread pool. Set reportId in service path and put in request body map of report properties")
    public void executeReport(
            @Parameter(name = "reportId", description = "Report id in service configuration DB", example="123") @PathVariable("reportId") long id,
            @Parameter(name = "settings", description = "Settings that include the path and name of the unloaded file and a map of parameters") @RequestBody ReportRequest settings) {
        try {
            long requestNumber = System.currentTimeMillis();
            logger.info(String.format("Start execute report by id [%s], request number [%s]", id, requestNumber));;
            logger.info(String.format("Received report properties [%s]", settings.toString()));
            
            ReportConfig reportConfig = reportLoaderService.getReportConfig(id);
            
            if(settings.getExportFilePath() != null && !settings.getExportFilePath().trim().equals("")) {
                reportConfig.setExportFilePath(settings.getExportFilePath());
            }
            
            reportExecutor.executeReport(requestNumber, reportConfig, settings.getProperties()).get();
            
            logger.info(String.format("Send response for report by id [%s], request number [%s]", id, requestNumber));
        } catch (Exception ex) {
            throw new ReportServiceException(ex);
        }
    }

    @PostMapping(value = "/report/execute-async/{reportId}", produces = "application/json; charset=UTF-8")
    @Operation(summary = "Provides the ability to run a report async",
            responses = {@ApiResponse(responseCode = "200", description = "Report strat to execute"), @ApiResponse(responseCode = "503", description = "Thread pool is overflowing"), @ApiResponse(responseCode = "500", description = "Internal server error")},
            description = "Provides execute report async. Report run in thread pool. Set reportId in service path and put in request body map of report properties")
    public void executeReportAsync(
            @Parameter(name = "reportId", description = "Report id in service configuration DB", example = "123") @PathVariable("reportId") long id,
            @Parameter(name = "settings", description = "Settings that include the path and name of the unloaded file and a map of parameters") @RequestBody ReportRequest settings) {
        try {
            long requestNumber = System.currentTimeMillis();
            logger.info(String.format("Report id [%s] with request number [%s]. Start report export", id, requestNumber));
            logger.info(String.format("Received report properties [%s]", settings.toString()));
            
            ReportConfig reportConfig = reportLoaderService.getReportConfig(id);
            
            if(settings.getExportFilePath() != null && !settings.getExportFilePath().trim().equals("")) {
                reportConfig.setExportFilePath(settings.getExportFilePath());
            }
            
            logger.trace(String.format("Report id [%s] with request number [%s]. Report config is loaded", id, requestNumber));
            
            reportExecutor.executeReport(requestNumber, reportConfig, settings.getProperties());
            
            logger.info(String.format("Send response for report by id [%s], request number [%s]", id, requestNumber));
        } catch (Exception ex) {
            throw new ReportServiceException(ex);
        }
    }
}
