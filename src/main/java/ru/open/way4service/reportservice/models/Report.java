package ru.open.way4service.reportservice.models;

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

@Entity
@Table(name = "reports")
@Getter
@Setter

@Hidden
@Schema(name = "Report", description = "Report entity")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long reportId;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "virtualaizer_prop_id")
    private VirtualaizerProperties virtualaizerProps;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "tamplate_path")
    private String tamplatePath;
    
    @Column(name = "export_path")
    private String exportPath;
    
    @Column(name = "export_type")
    private String exportType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "virtualaizer_type")
    private VirtualaizerTypes virtualaizerType;

    public Report() {
        
    }
}
