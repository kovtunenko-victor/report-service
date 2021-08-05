package ru.open.way4service.reportservice.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.repositories.target.TargetRepository;

@SpringBootTest
public class TargetRepositoryTest {
    @Autowired
    TargetRepository targetRepository;

    @Test
    void methodGetConnectionShuldReturnNotNullConnection() throws ReportServiceException {
        Connection connect = targetRepository.getConnection();
        assertThat(connect).isNotNull();
    }

    @Test
    void methodGetConnectionShuldReturnNotNullConnectionAndExecTestQuery() throws ReportServiceException, SQLException {
        Connection connect = targetRepository.getConnection();
        String sql = "select 1 as test from dual";
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        resultSet.next();

        resultSet.close();
        statement.close();
        connect.close();
    }
}
