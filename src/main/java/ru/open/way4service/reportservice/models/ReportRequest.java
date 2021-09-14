package ru.open.way4service.reportservice.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ReportRequest", description = "Scheme of sending report settings to run on the server")
public class ReportRequest {
    @Schema(description = "Path to export file")
    private String exportFilePath;
    @Schema(description = "Properties map for running the report")
    private Map<String, Object> properties;
}
