package project_one;

import java.sql.Timestamp;

public class UsrSession {
    private String sessionid;
    private int userid;
    private Timestamp expir;
    private int reqcount;


    public UsrSession(String sessionId, int userId, Timestamp timestamp, int reqCount) {
        this.sessionid = sessionId;
        this.userid = userId;
        this.expir = timestamp;
        this.reqcount = reqCount;
    }

    public String getSessionId() {
        return this.sessionid;
    }

    public int getUserId() {
        return this.userid;
    }

    public Timestamp getExpiration() {
        return this.expir;
    }

    public int getRequestCount() {
        return reqcount;
    }

}
