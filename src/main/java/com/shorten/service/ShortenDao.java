package com.shorten.service;

import com.shorten.dto.User;
import com.shorten.exception.UnknownUrlException;
import com.shorten.exception.UserNotFoundException;
import io.agroal.api.AgroalDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class ShortenDao {

    @Inject
    AgroalDataSource dataSource;

    public Long getSeed() throws SQLException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT nextval('seed')")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    return resultSet.getLong(1);
                }
            }
        }
    }


    public User getUser(String apiKey) throws SQLException, UserNotFoundException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from users where api_key = ?")) {
                preparedStatement.setString(1, apiKey);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return new User(resultSet.getString("api_key"), resultSet.getInt("urls_count"));
                    } else {
                        throw new UserNotFoundException();
                    }
                }
            }
        }
    }


    public void createUserUrl(String urlHash, String realUrl, String apiKey) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.beginRequest();
            saveUrl(connection, urlHash, realUrl, apiKey);
            incrementUserUrlsCount(connection, apiKey);
            connection.commit();
        }
    }

    private void incrementUserUrlsCount(Connection connection, String apiKey) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users set urls_count = urls_count+1 where api_key = ?")) {
            preparedStatement.setString(1, apiKey);
            preparedStatement.executeUpdate();
        }
    }

    private void saveUrl(Connection connection, String urlHash, String realUrl, String apiKey) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO urls (short_hash, real_url, api_key) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, urlHash);
            preparedStatement.setString(2, realUrl);
            preparedStatement.setString(3, apiKey);
            preparedStatement.executeUpdate();
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

    public String loadRealUrl(String urlHash) throws SQLException, UnknownUrlException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT real_url from urls where short_hash = ?")) {
                preparedStatement.setString(1, urlHash);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        return resultSet.getString(1);
                    } else {
                        throw new UnknownUrlException();
                    }

                }
            }
        }
    }
}
