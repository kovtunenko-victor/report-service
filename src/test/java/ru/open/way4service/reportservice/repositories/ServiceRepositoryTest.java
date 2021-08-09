package ru.open.way4service.reportservice.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ReportConfig;
import ru.open.way4service.reportservice.models.VirtualaizerTypes;
import ru.open.way4service.reportservice.repositories.service.ServiceRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
public class ServiceRepositoryTest {

    @Autowired
    ServiceRepository serviceRepository;

    @Test
    void methodGetReportByIdShuldReturnReportEntityByIdWithVirtualaizerTypesEqNotUse() throws ReportServiceException {
        ReportConfig report = serviceRepository.getReportById(1);

        assertThat(report).isNotNull();
        assertThat(report.getReportId()).isEqualTo(1);
        if (report.getVirtualaizerType() != VirtualaizerTypes.NOT_USE) {
            assertThat(report.getVirtualaizerProps()).isNotNull();
        }
    }

    @Test
    void methodGetReportByIdShuldReturnReportEntityByIdWithVirtualaizerProps() throws ReportServiceException {
        ReportConfig report = serviceRepository.getReportById(2);

        assertThat(report).isNotNull();
        assertThat(report.getReportId()).isEqualTo(2);
        if (report.getVirtualaizerType() != VirtualaizerTypes.NOT_USE) {
            assertThat(report.getVirtualaizerProps()).isNotNull();
        }
    }

    @Test
    void methodGetReportByIdShuldRaiseExceptionWhenIdNotFound() throws ReportServiceException {
        Throwable thrown = catchThrowable(() -> {
            serviceRepository.getReportById(-1);
        });

        assertThat(thrown).isInstanceOf(ReportServiceException.class);
        assertThat(thrown.getMessage()).isEqualTo("See nested exception");
        assertThat(thrown.getCause()).isNotNull();
        assertThat(thrown.getCause().getMessage()).isEqualTo("No entity found for query");
    }

    @Test
    void methodGetReportByTitleShuldReturnReportEntityByIdWithVirtualaizerTypesEqNotUse() throws ReportServiceException {
        ReportConfig report = serviceRepository.getReportByTitle("test_report1");

        assertThat(report).isNotNull();
        assertThat(report.getTitle()).isEqualTo("test_report1");
        if (report.getVirtualaizerType() != VirtualaizerTypes.NOT_USE) {
            assertThat(report.getVirtualaizerProps()).isNotNull();
        }
    }

    @Test
    void methodGetReportByTitleShuldReturnReportEntityByIdWithVirtualaizerProps() throws ReportServiceException {
        ReportConfig report = serviceRepository.getReportByTitle("test_report2");

        assertThat(report).isNotNull();
        assertThat(report.getTitle()).isEqualTo("test_report2");
        if (report.getVirtualaizerType() != VirtualaizerTypes.NOT_USE) {
            assertThat(report.getVirtualaizerProps()).isNotNull();
        }
    }

    @Test
    void methodGetReportByTitleShuldRaiseExceptionWhenTitleNotFound() throws ReportServiceException {
        Throwable thrown = catchThrowable(() -> {
            serviceRepository.getReportByTitle("test_report-1");
        });

        assertThat(thrown).isInstanceOf(ReportServiceException.class);
        assertThat(thrown.getMessage()).isEqualTo("See nested exception");
        assertThat(thrown.getCause()).isNotNull();
        assertThat(thrown.getCause().getMessage()).isEqualTo("No entity found for query");
    }

    @Test
    void methodGetReportByTitleShuldRaiseExceptionWhenTitleDuplicate() throws ReportServiceException {
        Throwable thrown = catchThrowable(() -> {
            serviceRepository.getReportByTitle("test_report3");
        });

        assertThat(thrown).isInstanceOf(ReportServiceException.class);
        assertThat(thrown.getMessage()).isEqualTo("See nested exception");
        assertThat(thrown.getCause()).isNotNull();
        assertThat(thrown.getCause().getMessage()).contains("query did not return a unique result");
    }
}
