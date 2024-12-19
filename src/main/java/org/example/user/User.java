package org.example.user;

import org.example.service.ModelObject;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable, ModelObject {
    private String userid;
    private String usermail;
    private String password;
    private double gehalt;

    public User() {}

    public User(String userid, String username, String password, double gehalt) {
        this.userid = userid;
        this.usermail = username;
        this.password = password;
        this.gehalt = gehalt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(userid, user.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userid);
    }

    @Override
    public String toString() {
        return usermail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getGehalt() {
        return gehalt;
    }

    public void setGehalt(double gehalt) {
        this.gehalt = gehalt;
    }
}
