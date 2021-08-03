package ru.open.way4service.reportservice.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "report")
@Getter
@Setter

@Hidden
@Schema(name = "Report", description = "Report entity")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long reportId;
    private String title;
    private String tamplatePath;
    private String exportPath;
    private String exportType;
    private String virtualaizerType;
    private String virtualaizerFilesPath;
    private String virtualaizerMaxSize;
    private String virtualaizerBlockSize;
    private String virtualaizerMinGrowCount;
    private String virtualaizerCompressionLevel;
}
