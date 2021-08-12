package ru.open.way4service.reportservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.open.way4service.reportservice.models.ReportConfig;
import ru.open.way4service.reportservice.repositories.service.ServiceRepository;

@Service
public class ReportLoaderServiceImpl implements ReportLoaderService {
    @Autowired
    ServiceRepository serviceRepository;
    
    @Override
    public ReportConfig getReportConfig(long reportId) {
        return serviceRepository.getReportById(reportId);
    }
    
    @Override
    public ReportConfig getReportConfig(String reportTitle) {
        return serviceRepository.getReportByTitle(reportTitle);
    }
}
