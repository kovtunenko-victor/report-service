package ru.open.way4service.reportservice.models;

import java.io.File;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.open.way4service.reportservice.errors.ReportServiceException;

@Entity
@Table(name = "virtualaizer_properties")
@Getter
@Setter

@Hidden
@Schema(name = "VirtualaizerProperties", description = "Virtualaizer properties.")
public class VirtualaizerProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long virtualaizerPropId;
    
    @Column(name = "virtualaizer_files_path")
    private String virtualaizerFilesPath;
    
    @Column(name = "virtualaizer_max_size")
    private int virtualaizerMaxSize;
    
    @Column(name = "virtualaizer_block_size")
    private int virtualaizerBlockSize;
    
    @Column(name = "virtualaizer_min_grow_count")
    private int virtualaizerMinGrowCount;
    
    @Column(name = "virtualaizer_compression_level")
    private int virtualaizerCompressionLevel;
    
    @OneToMany(mappedBy = "virtualaizerProps")
    Set<ReportConfig> reports;
    
    public VirtualaizerProperties() {
        
    }
    
    public File getObjectVirtualaizerFilesPath(ReportConfig reportConfig) throws ReportServiceException {
        File targetDirectory;
        
        if(virtualaizerFilesPath != null) {
            targetDirectory = new File(virtualaizerFilesPath + "\\" + reportConfig.getObjectTamplateFilePath().getName());
            
            if(!targetDirectory.exists()) {
                targetDirectory.mkdir();
            }
        }  else {
            targetDirectory = new File(reportConfig.getObjectTamplateFilePath().getPath()+ "\\virtualaizerData");
            
            if(!targetDirectory.exists()) {
                targetDirectory.mkdir();
            }
        }
        
        return targetDirectory;
    }
}
