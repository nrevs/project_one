package project_one;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsrSessionDAO {
    private Logger logger = LogManager.getLogger(UsrSessionDAO.class);

    private Connection _connection;

    private String _sessionid = "sessionid";
    private String _userid = "userId";
    private String _expir = "expir";
    private String _reqcount = "reqcount";

    public UsrSessionDAO(Connection connection) {
        _connection = connection;
    }

    public List<UsrSession> createUsrSession(int userId, String sessionId, Timestamp timestamp) {
        List<UsrSession> usrSessions = new ArrayList<UsrSession>();
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "INSERT INTO activesessions (sessionid, userid, expir, reqcount) VALUES (?, ?, ?, 0);"
            );
            pStatement.setString(1, sessionId);
            pStatement.setInt(2, userId);
            pStatement.setTimestamp(3, timestamp);
            int rows = pStatement.executeUpdate();

            if (rows>0) {
                usrSessions = getUsrSessionsByUserId(userId);
                return usrSessions;
            }


        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return usrSessions;
    }

    public List<UsrSession> getUsrSessionsByUserId(int userId) {
        List<UsrSession> usrSessions = new ArrayList<UsrSession>();
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT * from activesessions WHERE userid = ?;"
            );
            pStatement.setInt(1, userId);
            ResultSet rSet = pStatement.executeQuery();

            while(rSet.next()) {
                UsrSession sesh = new UsrSession(rSet.getString(_sessionid), rSet.getInt(_userid), rSet.getTimestamp(_expir), rSet.getInt(_reqcount));
                usrSessions.add(sesh);
            }
            return usrSessions;

        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return usrSessions;
    }

}
