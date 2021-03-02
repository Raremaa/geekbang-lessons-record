package com.masaiqi.geekbang.web.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author masaiqi
 * @date 2021/3/1 9:36 PM
 */
public class DatabaseConnectionManager {

    public static Connection getConnection() {
        String databaseURL = "jdbc:derby:db/user-platform;create=true";
        /**
         * 这里本质上是通过Java SPI加载Derby Driver
         * DriverManager 有个 static代码块 -> 调用#loadInitialDrivers方法 -> SPI加载Driver
         */
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(databaseURL);
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

    public static void main(String[] args) throws SQLException {
        Statement statement = getConnection().createStatement();
        // 创建 users 表
        System.out.println(statement.execute(CREATE_USERS_TABLE_DDL_SQL)); // false
    }
}
