package ru.open.way4service.reportservice.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ReportResponse", description = "Scheme of sending report result back to client")
public class ReportResponse {
    @Schema(description = "Request number for report")
    private long requestNumber;
    @Schema(description = "Path to export file")
    private String exportFilePath;
     
    public ReportResponse(long requestNumber, String exportFilePath) {
        this.requestNumber = requestNumber;
        this.exportFilePath = exportFilePath;
    }
}
