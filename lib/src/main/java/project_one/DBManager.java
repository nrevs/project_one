package project_one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    Connection connection;
    public UserDAO userDAO;

    public DBManager() {
        String url = "jdbc:postgresql://database-project-one.cjsdfjt5gj6o.us-east-1.rds.amazonaws.com:5432/projone";
        String uname = "nrevs";
        String pwDB = "KTw6bEi8dy9vxGdRjfrM";
        try {
            connection = DriverManager.getConnection(url, uname, pwDB);
            System.out.println("-1");
            userDAO = new UserDAO(connection);
            System.out.println("-2");

        } catch(SQLException sqlE) {
            
        }
    }
}
