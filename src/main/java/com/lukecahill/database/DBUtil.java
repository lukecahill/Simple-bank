package main.java.com.lukecahill.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Luke on 31/01/2017.
 */
public class DBUtil {

    private static final String orcDbUrl = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String orcUsername = "hr";
    private static final String orcPassword = "hr";

    private static final String mySql = "jdbc:mysql://localhost:3306/bank";
    private static final String myUser = "root";
    private static final String myPass = "";

    public  static Connection getConnection(DBType type) throws SQLException {
        switch (type) {
            case ORACLE_DB:
                return DriverManager.getConnection(orcDbUrl, orcUsername, orcPassword);
            case MYSQL_DB:
                return DriverManager.getConnection(mySql, myUser, myPass);
            default:
                return null;
        }
    }

    public static void showErrorMessage(SQLException e) {
        System.err.println("Error: " + e.getMessage());
        System.err.println("Error code: " + e.getErrorCode());
    }
}
