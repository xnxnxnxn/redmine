package com.xnjava.redmine;

import java.util.Date;

/**
 * @author xinn
 * @date 2020/5/23 0023 下午 5:17
 */
public class Info {
    private String username;
    private String password;
    private String startDate;
    private String endDate;

    public Info() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
