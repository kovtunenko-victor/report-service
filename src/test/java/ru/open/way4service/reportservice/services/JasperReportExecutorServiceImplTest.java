package ru.open.way4service.reportservice.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ExportReportTypes;
import ru.open.way4service.reportservice.models.ReportConfig;
import ru.open.way4service.reportservice.models.VirtualaizerTypes;

@SpringBootTest
public class JasperReportExecutorServiceImplTest {
    @Autowired
    @Qualifier("JasperReportExecutor")
    ReportExecutorService executorService;
    
    @Test
    void methodExecuteReportShuldExportReport() throws ReportServiceException, InterruptedException {
        executorService.executeReport(buildReportConfig(), buildReportProperties());
        Thread.sleep(10000000);
    }
    
    private ReportConfig buildReportConfig() {
        ReportConfig reportConfig = new ReportConfig();
        reportConfig.setReportId(0);
        reportConfig.setTitle("Title");
        reportConfig.setVirtualaizerType(VirtualaizerTypes.NOT_USE);
        reportConfig.setExportType(ExportReportTypes.XLS);
        reportConfig.setExportFilePath("C:\\reports\\report1\\export\\report_1.xlsx");
        reportConfig.setTamplateFilePath("C:\\reports\\report1\\template\\report1.jasper");
        return reportConfig;
    }
    
    private Map<String, Object>  buildReportProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("PARAM1", "TestName");
        return properties;
    }
}
