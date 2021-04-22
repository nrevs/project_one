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

    private String _id = "id";
    private String _un = "username";
    private String _email = "email";
    private String _as = "activesessions";
    private String _admin = "admin";
    private String _tmpexpire = "tmpexpire";


    public UserDAO(Connection connection) {
        _connection = connection;
    }


    public User getUserFromPassword(String username, String pw) {

        try {
            PreparedStatement pStatement = _connection.prepareStatement(
               // "SELECT password = crypt(?,password), id, username, email, admin, activesessions FROM users WHERE username = ?;"
                "SELECT password = crypt(?,password), tmppassword = crypt(?,tmppassword), id, username, email, admin, activesessions, tmpexpire FROM users WHERE username = ?;"
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
                    User usr = new User(rSet.getInt(_id), rSet.getString(_un), rSet.getString(_email), rSet.getBoolean(_admin), rSet.getInt(_as), null);

                    // clear tmppassword, tmpexpire
                    pStatement = _connection.prepareStatement(
                        "UPDATE users SET tmppassword = null, tmpexpire = null WHERE id = ?;"
                    );
                    pStatement.setInt(1, usr.getId());
                    pStatement.executeUpdate();
                    pStatement.close();

                    return usr;
                } else if (rSet.getBoolean(2)) {
                    System.out.println("temp password matched");
                    // Matched temporary password...
                    User usr = new User(rSet.getInt(_id), rSet.getString(_un), rSet.getString(_email), rSet.getBoolean(_admin), rSet.getInt(_as), rSet.getTimestamp(_tmpexpire));

                    // clear tmppassword, tmpexpire
                    pStatement = _connection.prepareStatement(
                        "UPDATE users SET tmppassword = null, tmpexpire = null WHERE id = ?;"
                    );
                    pStatement.setInt(1, usr.getId());
                    pStatement.executeUpdate();
                    pStatement.close();

                    if ( usr.getTmpExpire().before( new Timestamp( System.currentTimeMillis() ) ) ) {
                        // ...but past expiration -> return null
                        return null;
                    } else {
                        // ...and before expiration -> return user with expiration tmpexpire
                        return usr;
                    }

                } else {
                    System.out.println("no password matched...");
                    // User exists, but wrong password -> treating like user 
                    // does not exist, return null 
                    logger.info("user exists but wrong password");
                    pStatement.close();
                    return null;
                }
            }

            System.out.println("user does not exist");
            pStatement.close();
            return null;

        } catch(SQLException e) {
             e.printStackTrace();
             return null;
        }
    }

    /*
    *   Just getting User from Email, no password checks
    */
    public User getUserFromEmail(String email) {
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE email = ?;"
            );

            pStatement.setString(1, email);
            
            ResultSet rSet = pStatement.executeQuery();
            System.out.println("getUserFromEmail");
            

            if(rSet.next()==false) {
                // No user
                return null;
            } else {
                // First (should be only) user returned 
                User usr = new User(rSet.getInt(_id), rSet.getString(_un), rSet.getString(_email), rSet.getBoolean(_admin), rSet.getInt(_as), null);
                pStatement.close();
                return usr;
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
                    User usr = new User(rSet.getInt(_id), rSet.getString(_un), rSet.getString(_email), rSet.getBoolean(_admin), rSet.getInt(_as), rSet.getTimestamp(_tmpexpire));
                    pStatement.close();
                    return usr;
                }
            } else {
                pStatement.close();
                return null;
            }
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return null;
    }

    public User createUser(String username, String password, String email, boolean admin) {
        try {
            System.out.println("createUser");
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE username = ?;"
            );
            
            pStatement.setString(1, username);
            
            ResultSet rSet = pStatement.executeQuery();
            if (rSet.next()==false) {
                // No user with that username so we can create one
                System.out.println("going to insert user");

                //String sqlStr = String.format()
                pStatement = _connection.prepareStatement(
                    "INSERT INTO users (username, password, email, admin) VALUES (?, crypt(?,gen_salt('bf')), ?, ?);"
                );
                pStatement.setString(1, username);
                pStatement.setString(2, password);
                pStatement.setString(3, email);
                pStatement.setBoolean(4, admin);
                int rows = pStatement.executeUpdate();
                System.out.println("rows: "+String.valueOf(rows));
                if (rows>0) {
                    pStatement = _connection.prepareStatement(
                        "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE username = ?;"
                    );
                    pStatement.setString(1, username);
                    rSet = pStatement.executeQuery();
                    
                    User usr = new User(rSet.getInt(this._id), rSet.getString(this._un), rSet.getString(this._email), rSet.getBoolean(this._admin), rSet.getInt(this._as), rSet.getTimestamp(this._tmpexpire));
                    pStatement.close();
                    return usr;
                }
            }
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return null;
    }

    public User updateUserPassword(String username, String newpassword) {
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "UPDATE users SET password = crypt(?,gen_salt('bf')), tmppassword = null, tmpexpire = null WHERE username = ?");
            
            pStatement.setString(1, newpassword);
            pStatement.setString(2, username);

            if (pStatement.executeUpdate()>0) {
                pStatement = _connection.prepareStatement(
                    "SELECT id, username, email, admin, activesessions, tmpexpire FROM users WHERE username = ?;"
                );
                pStatement.setString(1, username);
                ResultSet rSet = pStatement.executeQuery();
                
                if(rSet.next()==false) {
                    // no username with username, uh oh, something went wrong, strange because we just updated password for user with username
                    pStatement.close();
                    return null;
                } else {
                    // return 
                    User usr = new User(rSet.getInt(_id), rSet.getString(_un), rSet.getString(_email), rSet.getBoolean(_admin), rSet.getInt(_as), rSet.getTimestamp(_tmpexpire));
                    pStatement.close();
                    return usr;
                }
            } else {
                // Query executed with row count of 0, nothing done => no user with username found, update failed
                return null;
            }
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return null;
    }
}
