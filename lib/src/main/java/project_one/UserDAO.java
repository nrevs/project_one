package project_one;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UserDAO {
    
    private Logger logger = LogManager.getLogger(UserDAO.class);

    private Connection _connection;

    public UserDAO(Connection connection) {
        _connection = connection;
    }

    public User getUser(String username, String pw) {

        try {
            PreparedStatement pStatement = _connection.prepareStatement(
               // "SELECT password = crypt(?,password), id, username, email, admin, activesessions FROM users WHERE username = ?;"
                "SELECT password = crypt(?,password), id, username, email, admin, activesessions  FROM users WHERE username = ?;"
            );

            pStatement.setString(1, pw);
            pStatement.setString(2, username);
            
            ResultSet rSet = pStatement.executeQuery();
            while(rSet.next()){
                if (rSet.getBoolean(1)) {
                    String id = "id";
                    String un = "username";
                    String email = "email";
                    String as = "activesessions";
                    String admin = "admin";

                    int asI = rSet.getInt(as);
                    logger.info("number of active sessions: {}",asI);

                    User usr = new User(rSet.getInt(id), rSet.getString(un), rSet.getString(email), rSet.getBoolean(admin), rSet.getInt(as));
                    return usr;
                } else {
                    // User exists, but wrong password -> treating like user 
                    // does not exist, return null 
                    System.out.println("user exists but wrong password");
                    return null;
                }
            }
            System.out.println("user does not exist");
            return null;

        } catch(SQLException e) {
             e.printStackTrace();
             return null;
        }

    }
}
