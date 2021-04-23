package project_one;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    
    public int createUsrSession(int userId) {

        int rows = 0;
        Timestamp tstamp = Timestamp.from(Instant.now().plus(24, ChronoUnit.HOURS));

        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "INSERT INTO activesessions (userid, expir) VALUES (?, ?);"
            );
            pStatement.setInt(1, userId);
            pStatement.setTimestamp(2, tstamp);
            rows = pStatement.executeUpdate();

        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return rows;
    }


    public int clearStaleSessions() {
        int rows = 0;
        try {

            PreparedStatement pStatement = _connection.prepareStatement(
                "DELETE FROM activesessions WHERE expir < ?;"
            );
            Timestamp rightNow = new Timestamp( System.currentTimeMillis() );
            pStatement.setTimestamp(1, rightNow);
            rows = pStatement.executeUpdate();
            
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return rows;
    }


    public List<UsrSession> getUsrSessionsByUserId(int userId) {

        createUsrSession(userId);
        clearStaleSessions();

        List<UsrSession> usrSessions = new ArrayList<UsrSession>();
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT userid, expir, reqcount, sessionid FROM activesessions WHERE userid = ?;"
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
