package com.xnjava.redmine;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xinn
 * @date 2020/5/23 0023 下午 4:23
 */
public class RedmineProject {

    private String name;
    private Double hour;
    private List<RedmineIssue> redmineIssueList;
    private Double rate;

    public RedmineProject(String name, Double hour, List<RedmineIssue> redmineIssueList) {
        this.name = name;
        this.hour = hour;
        this.redmineIssueList = redmineIssueList;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        BigDecimal bigDecimal = new BigDecimal(rate);
        this.rate = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHour() {
        return hour;
    }

    public void setHour(Double hour) {
        this.hour = hour;
    }

    public List<RedmineIssue> getRedmineIssueList() {
        return redmineIssueList;
    }

    public void setRedmineIssueList(List<RedmineIssue> redmineIssueList) {
        this.redmineIssueList = redmineIssueList;
    }
}
