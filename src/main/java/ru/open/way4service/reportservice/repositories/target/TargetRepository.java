package ru.open.way4service.reportservice.repositories.target;

import java.sql.Connection;

import ru.open.way4service.reportservice.errors.ReportServiceException;

public interface TargetRepository {
    Connection getConnection() throws ReportServiceException;
}
