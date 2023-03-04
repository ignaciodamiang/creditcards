package com.nacho.creditcards;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class DBConnectionTest {

    @Test
    public void testDatabaseConnection() throws ClassNotFoundException, SQLException, IOException {
        // Load the properties from the application.properties file
        Properties props = new Properties();
        InputStream input = DBConnectionTest.class.getClassLoader().getResourceAsStream("application.properties");
        props.load(input);

        // Read the values from the properties object and use them to connect to the database
        String dbUrl = props.getProperty("spring.datasource.url");
        String dbUsername = props.getProperty("spring.datasource.username");
        String dbPassword = props.getProperty("spring.datasource.password");
        String dbDriver = props.getProperty("spring.datasource.driver-class-name");

        // Print out the database configuration properties
        System.out.println("dbUrl: " + dbUrl);
        System.out.println("dbUsername: " + dbUsername);
        System.out.println("dbPassword: " + dbPassword);
        System.out.println("dbDriver: " + dbDriver);

        // Load the MySQL JDBC driver
        Class.forName(dbDriver);

        // Create the database connection
        Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

        // Verify that the connection is not null
        assertNotNull(conn, "Database connection should not be null");

        // Close the connection
        conn.close();
    }
}
