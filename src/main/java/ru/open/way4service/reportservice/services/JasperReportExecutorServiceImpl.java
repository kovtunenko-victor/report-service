package ru.open.way4service.reportservice.services;

import java.io.File;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRFiller;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.DeflateStreamCompression;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import ru.open.way4service.reportservice.models.ReportConfig;
import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ExportReportTypes;
import ru.open.way4service.reportservice.models.VirtualaizerProperties;
import ru.open.way4service.reportservice.models.VirtualaizerTypes;
import ru.open.way4service.reportservice.repositories.target.TargetRepository;

@Service("JasperReportExecutor")
public class JasperReportExecutorServiceImpl implements ReportExecutorService<String> {

    private Logger logger = LoggerFactory.getLogger(JasperReportExecutorServiceImpl.class);
    @Autowired
    TargetRepository targetRepository;

    @Override
    @Async("taskExecutor")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<String> executeReport(long requestNumber, ReportConfig reportConfig, Map<String, Object> properties) {
        Map<String, Object> localProperties = new HashMap<>(properties);
        try {
            logger.trace(String.format("Report id [%s] with request number [%s]. Start build jasper report", reportConfig.getReportId(), requestNumber));
            JasperReportBuilder reportBuilder = new JasperReportBuilder(reportConfig);
            logger.trace(String.format("Report id [%s] with request number [%s]. End build jasper report", reportConfig.getReportId(), requestNumber));

            logger.trace(String.format("Report id [%s] with request number [%s]. Start create jasper filler", reportConfig.getReportId(), requestNumber));
            JRBaseFiller filler = reportBuilder.getJasperFiller();
            logger.trace(String.format("Report id [%s] with request number [%s]. End create jasper filler", reportConfig.getReportId(), requestNumber));

            logger.trace(String.format("Report id [%s] with request number [%s]. Start create jasper exporter", reportConfig.getReportId(), requestNumber));
            JRAbstractExporter exporter = getJasperExporter(reportConfig.getExportType());
            logger.trace(String.format("Report id [%s] with request number [%s]. End create jasper exporter", reportConfig.getReportId(), requestNumber));

            if (reportBuilder.getJasperVirtualaizer() != null) {
                logger.trace(String.format("Report id [%s] with request number [%s]. Start create jasper virtualaizer", reportConfig.getReportId(), requestNumber));
                localProperties.put(JRParameter.REPORT_VIRTUALIZER, reportBuilder.getJasperVirtualaizer());
                logger.trace(String.format("Report id [%s] with request number [%s]. End create jasper virtualaizer", reportConfig.getReportId(), requestNumber));
            }

            if (!localProperties.containsKey("SUBREPORT_DIR")) {
                logger.trace(String.format("Report id [%s] with request number [%s]. Set SUBREPORT_DIR [%s]", reportConfig.getReportId(), requestNumber, ReportConfig.Utils.getObjectTamplateFilePath(reportConfig.getTamplateFilePath()).getParent() + File.separator));
                localProperties.put("SUBREPORT_DIR", ReportConfig.Utils.getObjectTamplateFilePath(reportConfig.getTamplateFilePath()).getParent() + File.separator);
            }

            logger.trace(String.format("Report id [%s] with request number [%s]. Start open db connection", reportConfig.getReportId(), requestNumber));
            File exportFile = ReportConfig.Utils.getObjectExportFilePath(reportConfig.getExportFilePath());
            
            try (Connection connection = targetRepository.getConnection()) {
                logger.trace(String.format("Report id [%s] with request number [%s]. Start fill jasper print", reportConfig.getReportId(), requestNumber));
                Thread enderThread = ReportTimeoutProvider.provideTimeout(Thread.currentThread(), reportConfig.getReportId(), requestNumber);
                JasperPrint print = filler.fill(localProperties, connection);
                
                logger.trace(String.format("Report id [%s] with request number [%s]. End fill jasper print", reportConfig.getReportId(), requestNumber));
                exporter.setExporterInput(new SimpleExporterInput(print));
               
                logger.trace(String.format("Report id [%s] with request number [%s]. File to export [%s]", reportConfig.getReportId(), requestNumber, exportFile));
                
                try (OutputStream fileOutputStream = ReportConfig.Utils.getFileOutputStream(exportFile)) {
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileOutputStream));
                    logger.trace(String.format("Report id [%s] with request number [%s]. Start to export report", reportConfig.getReportId(), requestNumber));
                    exporter.exportReport();
                }
                
                logger.info(String.format("Report id [%s] with request number [%s]. Report exported", reportConfig.getReportId(), requestNumber));
                enderThread.interrupt();
            }

            return new AsyncResult<>(exportFile.getName());
        } catch (Throwable ex) {
            logger.error(String.format("Report id [%s] with request number [%s]. Exception at runtime report execute", reportConfig.getReportId(), requestNumber), ex);
            throw new ReportServiceException(ex);
        }
    }

    @SuppressWarnings("rawtypes")
    private JRAbstractExporter getJasperExporter(ExportReportTypes reportTypes) {
        JRAbstractExporter exporter = null;

        if (reportTypes.equals(ExportReportTypes.XLS)) {
            exporter = new JRXlsxExporter();
        }
        if (reportTypes.equals(ExportReportTypes.DOC)) {
            exporter = new JRDocxExporter();
        }
        if (exporter == null) {
            throw new ReportServiceException(
                    String.format("Report types [%s] unknown. Jasper exporter is null.", reportTypes.toString()));
        }

        return exporter;
    }

    private static class JasperReportFileLoader {

        private static Logger logger = LoggerFactory.getLogger(JasperReportFileLoader.class);

        public static JasperReport loadJasperReport(File tamplateFilePath) {
            try {
                return (JasperReport) JRLoader.loadObject(tamplateFilePath);
            } catch (JRException ex) {
                logger.error(ex.getMessage());
                throw new ReportServiceException(ex);
            }
        }
    }

    private static class JasperReportBuilder {

        private static Logger logger = LoggerFactory.getLogger(JasperReportBuilder.class);

        private final JRBaseFiller jasperFiller;
        private final JRVirtualizer jasperVirtualaizer;

        public JasperReportBuilder(ReportConfig reportConfig) {
            jasperFiller = buildJasperFiller(reportConfig);
            jasperVirtualaizer = buildJasperVirtualizer(reportConfig);
        }

        public JRBaseFiller getJasperFiller() {
            return jasperFiller;
        }

        public JRVirtualizer getJasperVirtualaizer() {
            return jasperVirtualaizer;
        }

        private JRVirtualizer buildJasperVirtualizer(ReportConfig reportConfig) {
            if (!reportConfig.getVirtualaizerType().equals(VirtualaizerTypes.NOT_USE)) {
                JRVirtualizer virtualaizer = null;

                if (reportConfig.getVirtualaizerProps() == null) {
                    throw new ReportServiceException("Virtualaizer properties is null");
                }

                String virtualaizerFilesPath = VirtualaizerProperties.Utils
                        .getObjectVirtualaizerFilesPath(reportConfig.getVirtualaizerProps().getVirtualaizerFilesPath(),
                                ReportConfig.Utils.getObjectTamplateFilePath(reportConfig.getTamplateFilePath()))
                        .getPath();
                int maxSize = reportConfig.getVirtualaizerProps().getVirtualaizerMaxSize();
                int blockSize = reportConfig.getVirtualaizerProps().getVirtualaizerBlockSize();
                int minGrowCount = reportConfig.getVirtualaizerProps().getVirtualaizerMinGrowCount();
                int compressionLevel = reportConfig.getVirtualaizerProps().getVirtualaizerCompressionLevel();

                if (reportConfig.getVirtualaizerType().equals(VirtualaizerTypes.FILE)) {
                    virtualaizer = new JRFileVirtualizer(maxSize, virtualaizerFilesPath);
                }
                if (reportConfig.getVirtualaizerType().equals(VirtualaizerTypes.SWAP)) {
                    virtualaizer = new JRSwapFileVirtualizer(maxSize,
                            new JRSwapFile(virtualaizerFilesPath, blockSize, minGrowCount));
                }
                if (reportConfig.getVirtualaizerType().equals(VirtualaizerTypes.GZIP)) {
                    virtualaizer = new JRGzipVirtualizer(maxSize);
                }
                if (reportConfig.getVirtualaizerType().equals(VirtualaizerTypes.SWAP_GZIP)) {
                    virtualaizer = new JRSwapFileVirtualizer(maxSize,
                            new JRSwapFile(virtualaizerFilesPath, blockSize, minGrowCount), true,
                            new DeflateStreamCompression(compressionLevel));
                }
                if (virtualaizer == null) {
                    throw new ReportServiceException("Virtualaizer is null");
                }

                return virtualaizer;
            } else {
                return null;
            }
        }

        private JRBaseFiller buildJasperFiller(ReportConfig reportConfig) {
            try {
                JasperReport report = JasperReportFileLoader.loadJasperReport(
                        ReportConfig.Utils.getObjectTamplateFilePath(reportConfig.getTamplateFilePath()));
                JasperReportsContext context = new SimpleJasperReportsContext();
                context.setProperty("net.sf.jasperreports.subreport.runner.factory", "net.sf.jasperreports.engine.fill.JRContinuationSubreportRunnerFactory");
                JRBaseFiller filler = JRFiller.createFiller(context, report);

                return filler;
            } catch (JRException ex) {
                logger.error(ex.getMessage());
                throw new ReportServiceException(ex);
            }
        }
    }

    private static class ReportTimeoutProvider {
        private static Logger logger = LoggerFactory.getLogger(ReportTimeoutProvider.class);
        private static final long TIMEOUT_IN_HOURS = 4;

        public static Thread provideTimeout(Thread mainThrad, long reportId, long requestNumber) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(TimeUnit.HOURS.toMillis(TIMEOUT_IN_HOURS));
                    if (mainThrad.isAlive()) {
                        logger.error(String.format("Report id [%s] with request number [%s]. Report execute timeout", reportId, requestNumber));
                        mainThrad.interrupt();
                    }
                } catch (InterruptedException ex) {

                } catch (Exception ex) {
                    logger.error(String.format("Report id [%s] with request number [%s]. Exception at timeout", reportId, requestNumber), ex);
                    throw new ReportServiceException(ex);
                }
            }, String.format("threadEnder - %s", requestNumber));

            thread.start();

            return thread;
        }
    }
}
