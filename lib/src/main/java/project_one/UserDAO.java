package project_one;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UserDAO {
    
    private Logger logger = LogManager.getLogger(UserDAO.class);

    private Connection _connection;

    private String id = "id";
    private String un = "username";
    private String email = "email";
    private String as = "activesessions";
    private String admin = "admin";
    private String tmpexpire = "tmpexpire";


    public UserDAO(Connection connection) {
        _connection = connection;
    }


    public User getUserFromPassword(String username, String pw) {

        try {
            PreparedStatement pStatement = _connection.prepareStatement(
               // "SELECT password = crypt(?,password), id, username, email, admin, activesessions FROM users WHERE username = ?;"
                "SELECT password = crypt(?,password), crypt(?,tmppassword), id, username, email, admin, activesessions, tmpexpire FROM users WHERE username = ?;"
            );

            pStatement.setString(1, pw);
            pStatement.setString(2, pw);
            pStatement.setString(3, username);
            
            ResultSet rSet = pStatement.executeQuery();
            System.out.println("getUser");
            
            while(rSet.next()){
                if (rSet.getBoolean(1)) {
                    System.out.println("main password matched");
                    // Matched perm password
                    User usr = new User(rSet.getInt(id), rSet.getString(un), rSet.getString(email), rSet.getBoolean(admin), rSet.getInt(as), null);
                    return usr;
                } else if (rSet.getBoolean(2)) {
                    System.out.println("temp password matched");
                    // Matched temporary password...
                    User usr = new User(rSet.getInt(id), rSet.getString(un), rSet.getString(email), rSet.getBoolean(admin), rSet.getInt(as), rSet.getTimestamp(tmpexpire));

                    // clear tmppassword, tmpexpire
                    pStatement = _connection.prepareStatement(
                        "UPDATE users SET tmppassword = null, tmpexpire = null WHERE id = ?;"
                    );
                    pStatement.setInt(1, usr.getId());
                    pStatement.executeUpdate();
                    

                    if ( usr.getTmpExpire().before( new Timestamp( System.currentTimeMillis() ) ) ) {
                        // ...but past expiration -> return null
                        return null;
                    } else {
                        // ...and before expiration -> return user with expiration tmpexpire
                        return usr;
                    }

                } else {
                    // User exists, but wrong password -> treating like user 
                    // does not exist, return null 
                    logger.info("user exists but wrong password");
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


    public User getUserFromEmail(String email) {
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
               // "SELECT password = crypt(?,password), id, username, email, admin, activesessions FROM users WHERE username = ?;"
                "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE email = ?;"
            );

            pStatement.setString(1, email);
            
            ResultSet rSet = pStatement.executeQuery();
            System.out.println("getUserFromEmail");
            
            if(rSet.first()){
                User usr = new User(rSet.getInt(id), rSet.getString(un), rSet.getString(email), rSet.getBoolean(admin), rSet.getInt(as), null);
                return usr;
            } else {
                return null;
            }

        } catch(SQLException e) {
             e.printStackTrace();
             return null;
        }
    }


    public User setTempPassword(String email, String tmppass, Timestamp timestamp) {
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "UPDATE users SET tmppassword = crypt(?,gen_salt('bf')), tmpexpire = ? WHERE email = ?");
            
            pStatement.setString(1, tmppass);
            pStatement.setTimestamp(2, timestamp);
            pStatement.setString(3, email);
            if (pStatement.executeUpdate()>0) {
                pStatement = _connection.prepareStatement(
                    "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE email = ?;"
                );
                pStatement.setString(1, email);
                ResultSet rSet = pStatement.executeQuery();

                while(rSet.next()) {
                    User usr = new User(rSet.getInt(id), rSet.getString(un), rSet.getString(email), rSet.getBoolean(admin), rSet.getInt(as), rSet.getTimestamp(tmpexpire));
                    return usr;
                }
            } else {
                return null;
            }
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return null;
    }

    public User createUser(String username, String password, String email, boolean admin) {
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE username = ?;"
            );
            
            pStatement.setString(1, username);
            ResultSet rSet = pStatement.executeQuery();
            if (rSet.first()) {
                // Already user with that username
                return null;
            } else {

                pStatement = _connection.prepareStatement(
                    "INSERT INTO users (username, password, email, admin) "+
                    "VALUES (" +
                    "?, " +
                    "crypt(?,gen_salt('bf')), " +
                    "?, " +
                    "?" +
                    ");"
                );
                pStatement.setString(1, username);
                pStatement.setString(2, password);
                pStatement.setString(3, email);
                pStatement.setBoolean(4, admin);
                
                if (pStatement.executeUpdate()>0) {
                    pStatement = _connection.prepareStatement(
                        "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE username = ?;"
                    );
                    pStatement.setString(1, username);
                    rSet = pStatement.executeQuery();
                    User usr = new User(rSet.getInt(this.id), rSet.getString(this.un), rSet.getString(this.email), rSet.getBoolean(this.admin), rSet.getInt(this.as), rSet.getTimestamp(this.tmpexpire));
                    return usr;
                }
            }
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return null;
    }

    public User updateUserPassword(String username, String password) {
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "UPDATE users SET password = crypt(?,gen_salt('bf')), tmppassword = null, tmpexpire = null WHERE username = ?");
            
            pStatement.setString(1, password);
            pStatement.setString(2, username);

            if (pStatement.executeUpdate()>0) {
                pStatement = _connection.prepareStatement(
                    "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE username = ?;"
                );
                pStatement.setString(1, username);
                ResultSet rSet = pStatement.executeQuery();

                if(rSet.first()) {
                    User usr = new User(rSet.getInt(id), rSet.getString(un), rSet.getString(email), rSet.getBoolean(admin), rSet.getInt(as), rSet.getTimestamp(tmpexpire));
                    return usr;
                }
            } else {
                return null;
            }
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return null;
    }
}
