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
import java.util.UUID;

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

    public int getRequestCountBySessionId(String sessionId) {
        int count = 11;
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT reqcount FROM activesessions WHERE sessionid=?;"
            );
            UUID uuid = UUID.fromString(sessionId);
            pStatement.setObject(1, uuid);
            ResultSet rSet = pStatement.executeQuery();
            rSet.next();
            count = rSet.getInt("reqcount");

        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return count;
    }

    public void incrementRequestCount(String sessionId) {
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "UPDATE activesessions SET reqcount = reqcount + 1 WHERE sessionid=?;"
            );
            UUID uuid = UUID.fromString(sessionId);
            pStatement.setObject(1, uuid);
            pStatement.executeUpdate();

        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public int getActiveSessionsCountByUserId(int userId) {
        int asessions = 0;
        clearStaleSessionsByUserId(userId);
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT COUNT(userid)::int FROM activesessions WHERE userid=?;"
            );
            pStatement.setInt(1, userId);
            ResultSet rSet = pStatement.executeQuery();
            rSet.next();
            asessions = rSet.getInt(1);

        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return asessions;
    }
    
    public int createUsrSessionByUserId(int userId) {

        int rows = 0;
        Timestamp tstamp = Timestamp.from(Instant.now().plus(24, ChronoUnit.HOURS));
        System.out.println("timestamp: "+tstamp.toString());

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

    public int clearStaleSessionsByUserId(int userId) {
        int rows = 0;
        try {

            PreparedStatement pStatement = _connection.prepareStatement(
                "DELETE FROM activesessions WHERE userid = ? AND expir < ?;"
            );
            pStatement.setInt(1, userId);
            Timestamp rightNow = new Timestamp( System.currentTimeMillis() );
            pStatement.setTimestamp(2, rightNow);
            rows = pStatement.executeUpdate();
            
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return rows;
    }


    public List<UsrSession> getUsrSessionsByUserId(int userId) {

        UserDAO userDAO = new UserDAO(this._connection);
        int asessions = userDAO.updateActiveSessions(userId);
        if (asessions<5) {
            createUsrSessionByUserId(userId);
        }
        userDAO.updateActiveSessions(userId);
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
