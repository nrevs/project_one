package project_one;


public class User {
    private int id;
    private String username;
    private String email;
    private boolean admin;
    private int activesessions;


    public User(int id, String username, String email, boolean admin, int activesessions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.admin = admin;
        this.activesessions = activesessions;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public int getActiveSessions() {
        return this.activesessions;
    }


}
