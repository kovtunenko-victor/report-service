package ru.open.way4service.reportservice.repositories.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceRepository {
    @PersistenceContext(unitName = "serviceEntityManager")
    private EntityManager entityManager;

    public void test() {
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> test1(connection));
    }

    public void test1(Connection connection) {
        try {
            String sql = "select id, date_end, date_start, name, title from surveys";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
            
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {

        }
    }
}
