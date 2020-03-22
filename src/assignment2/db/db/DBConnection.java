package assignment2.db.db;

import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static String userName = "root";
    // The password for the MySQL account.
    private static String password = "";
    // The name of the computer running MySQL.
    private static String serverName = "localhost";
    // The port of the MySQL server.
    private static int portNumber = 3308;
    // The name of the database we are testing with.
    private static String dbName = "assign2";
    // Global connection object
    private static Connection conn = null;


    public Connection getConnection() throws SQLException {
        // Create connection properties for the login and password for the db connection
        try {
            Properties connectionProps = new Properties();
            connectionProps.put("user", this.userName);
            // connectionProps.put("password", this.password);
            // Create the connection with the database
            conn = DriverManager.getConnection("jdbc:mysql://"
                            + this.serverName + ":" + this.portNumber + "/" + this.dbName,
                    connectionProps);
            // Return connection
            System.out.println("db connected");
            return conn;
        } catch (SQLException e) {
            System.out.println("db not connected");
            return null;
        }
    }
}



