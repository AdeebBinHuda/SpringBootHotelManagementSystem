package com.hotel.SpringBootHotelbooking.entity;

import jakarta.persistence.*;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @Column(name="is_log_out")
    private boolean logOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token() {
    }

    public Token(long id, String token, boolean logOut, User user) {
        this.id = id;
        this.token = token;
        this.logOut = logOut;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogOut() {
        return logOut;
    }

    public void setLogOut(boolean logOut) {
        this.logOut = logOut;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
