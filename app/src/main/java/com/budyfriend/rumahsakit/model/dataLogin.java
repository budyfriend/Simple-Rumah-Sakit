package com.budyfriend.rumahsakit.model;

public class dataLogin {
    private String  id;
    private String  username;
    private String  password;
    private String  level;
    private boolean  active;

    public dataLogin() {
    }

    public dataLogin(String username, String password, String level, boolean active) {
        this.username = username;
        this.password = password;
        this.level = level;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLevel() {
        return level;
    }
}
