package com.example.financingappcurrent.Objects;

public class User {
    protected String name;
    protected String psw;
    protected String loginName;

    public User() {

    }

    public User(String name, String psw, String loginName) {
        this.name = name;
        this.psw = psw;
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
