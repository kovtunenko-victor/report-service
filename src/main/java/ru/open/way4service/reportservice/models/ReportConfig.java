package ru.open.way4service.reportservice.models;

import java.io.File;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.open.way4service.reportservice.errors.ReportServiceException;

@Entity
@Table(name = "reports")
@Getter
@Setter

@Hidden
@Schema(name = "Report", description = "Report entity")
public class ReportConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long reportId;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "virtualaizer_prop_id")
    private VirtualaizerProperties virtualaizerProps;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "tamplate_file_path")
    private String tamplateFilePath;
    
    @Column(name = "export_file_path")
    private String exportFilePath;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "export_type")
    private ExportReportTypes exportType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "virtualaizer_type")
    private VirtualaizerTypes virtualaizerType;
    
    public ReportConfig() {
        
    }
    
    public File getObjectTamplateFilePath() throws ReportServiceException  {
        if(tamplateFilePath != null) {
            return new File(tamplateFilePath);
        } else {
            throw new ReportServiceException("Tamplate file path is null");
        }
    }
    
    public File getObjectExportPath() throws ReportServiceException {
        if(exportFilePath != null) {
            return getUniqueFileName(new File(exportFilePath));
        } else {
            throw new ReportServiceException("Export file path is null");
        }
    }
    
    private File getUniqueFileName(File exportFilePath) {
        String[] splitedFileName = exportFilePath.getName().split("\\.");
        String extension = splitedFileName[splitedFileName.length - 1];
        
        StringBuilder resultFileName = new StringBuilder();
        resultFileName.append(exportFilePath.getParent()).append("\\");
        
        for(int i = 0; i < splitedFileName.length - 1; i++) {
            resultFileName.append(splitedFileName[i]).append(".");
        }
        
        resultFileName.deleteCharAt(resultFileName.lastIndexOf("."));
        resultFileName.append("_").append(System.currentTimeMillis()).append(".").append(extension);
        
        return new File(exportFilePath.getParent() + "\\" + resultFileName.toString());
    }
}
