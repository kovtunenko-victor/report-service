package ru.open.way4service.reportservice.services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ExportReportTypes;
import ru.open.way4service.reportservice.models.ReportConfig;
import ru.open.way4service.reportservice.models.VirtualaizerTypes;
import ru.open.way4service.reportservice.repositories.target.TargetRepository;

@Service
public class JasperReportExecutorServiceImpl implements ReportExecutorService {
    @Autowired
    TargetRepository targetRepository;
    
    @Override
    @Async("taskExecutor")
    public void executeReport(ReportConfig reportConfig, Map<String, Object> properties) throws ReportServiceException {
        Map<String, Object> localProperties = new HashMap<>(properties);
        getJasperVirtualizer(reportConfig, localProperties);
        JasperPrint jasperPrint = getJasperPrint(reportConfig, localProperties);
        exportJasperReport(reportConfig, jasperPrint);
    }

    private JasperReport loadJasperReport(File tamplateFilePath) throws ReportServiceException {
        try {
            return (JasperReport) JRLoader.loadObject(tamplateFilePath);
        } catch (JRException ex) {
            throw new ReportServiceException("See nested exception", ex);
        }
    }

    private void getJasperVirtualizer(ReportConfig reportConfig, Map<String, Object> properties)
            throws ReportServiceException {
        if (!reportConfig.getVirtualaizerType().equals(VirtualaizerTypes.NOT_USE)) {
            JRVirtualizer virtualaizer = null;
            
            String virtualaizerFilesPath = reportConfig.getVirtualaizerProps().getObjectVirtualaizerFilesPath(reportConfig).getPath();
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

            properties.put(JRParameter.REPORT_VIRTUALIZER, virtualaizer);
        }
    }

    private JasperPrint getJasperPrint(ReportConfig reportConfig, Map<String, Object> properties)
            throws ReportServiceException {
        try {
            JasperReport report = loadJasperReport(reportConfig.getObjectTamplateFilePath());
            SimpleJasperReportsContext context = new SimpleJasperReportsContext();
            JRBaseFiller filler = JRFiller.createFiller(context, report);
            JasperPrint print = filler.fill(properties, targetRepository.getConnection());
            return print;
        } catch (JRException ex) {
            throw new ReportServiceException("See nested exception", ex);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void exportJasperReport(ReportConfig reportConfig, JasperPrint jasperPrint) throws ReportServiceException {
        try {
            JRAbstractExporter exporter = getJasperExporter(reportConfig.getExportType());
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportConfig.getObjectExportPath()));
            exporter.exportReport();
        } catch (JRException ex) {
            throw new ReportServiceException("See nested exception", ex);
        }
    }

    @SuppressWarnings("rawtypes")
    private JRAbstractExporter getJasperExporter(ExportReportTypes reportTypes) throws ReportServiceException {
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
}
