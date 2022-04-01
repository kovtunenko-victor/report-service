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
    @Schema(description = "Unique name for export file")
    private String exportFileName;
     
    public ReportResponse(long requestNumber, String exportFileName) {
        this.requestNumber = requestNumber;
        this.exportFileName = exportFileName;
    }
}
