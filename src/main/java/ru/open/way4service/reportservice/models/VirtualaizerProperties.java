package ru.open.way4service.reportservice.models;

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
    private String virtualaizerMaxSize;
    
    @Column(name = "virtualaizer_block_size")
    private String virtualaizerBlockSize;
    
    @Column(name = "virtualaizer_min_grow_count")
    private String virtualaizerMinGrowCount;
    
    @Column(name = "virtualaizer_compression_level")
    private String virtualaizerCompressionLevel;
    
    @OneToMany(mappedBy = "virtualaizerProps")
    Set<Report> reports;
    
    public VirtualaizerProperties() {
        
    }
}
