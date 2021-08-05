package ru.open.way4service.reportservice.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ExportReportTypes", description = "ExportReportTypes enum")
public enum ExportReportTypes {
    XLS, DOC
}
