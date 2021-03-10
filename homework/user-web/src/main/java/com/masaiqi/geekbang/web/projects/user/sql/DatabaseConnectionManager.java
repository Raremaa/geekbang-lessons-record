package com.masaiqi.geekbang.web.projects.user.sql;


import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * @author masaiqi
 * @date 2021/3/1 9:36 PM
 */
public class DatabaseConnectionManager {

    private final Logger logger = Logger.getLogger(DatabaseConnectionManager.class.getName());

    @Resource(name = "jdbc/UserPlatformDB")
    private DataSource dataSource;

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;


    public Connection getConnection() {
        Connection connection;
        try{
            connection = dataSource.getConnection();
        }catch (SQLException sqlException) {
            // Throw Unchecked Exception
            throw new RuntimeException(sqlException.getCause());
        }
        return connection;
    }

    public static void releaseConnection(Connection connection) {
        if(connection != null) {
            try{
                connection.close();
            } catch (SQLException sqlException) {
                // Throw Unchecked Exception
                throw new RuntimeException(sqlException.getCause());
            }
        }
    }

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";
}
