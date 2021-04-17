package project_one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    private Connection _connection;

    public UserDAO(Connection connection) {
        _connection = connection;
    }

    public boolean validate(String username, String pw) {
        System.out.println("1");
        boolean res = false;

        try {
            System.out.println("2");
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT password = crypt(?,password) FROM users WHERE username = ?;"
            );
            System.out.println("3");
            pStatement.setString(1, pw);
            System.out.println("4");
            pStatement.setString(2, username);
            System.out.println("5");
            ResultSet rSet = pStatement.executeQuery();
            System.out.println("6");
            while(rSet.next()){
                System.out.println(String.valueOf(rSet.getBoolean(1)));
                System.out.println(String.valueOf(rSet.getRow()));
            }
        } catch(SQLException e) {
             e.printStackTrace();
        }

        return res;
    }
}
