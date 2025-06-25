package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() throws SQLException {
        String PASSWORD = "";
        String USER = "root";
        String URL = "jdbc:mysql://localhost:3306/shopdb";
        this.connection =  DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public Connection getConnection(){
        return connection;
    }

    public static DBConnection getInstance() throws SQLException{
        return  null==instance?instance = new DBConnection():instance;
    }
}
