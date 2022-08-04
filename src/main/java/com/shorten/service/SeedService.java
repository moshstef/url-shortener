package com.shorten.service;

import io.agroal.api.AgroalDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class SeedService {

    @Inject
    AgroalDataSource dataSource;

    public Long getSeed() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT nextval('seed')")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    return resultSet.getLong(1);
                }
            }
        }
    }

}
