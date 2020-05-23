package com.xnjava.redmine;

/**
 * @author xinn
 * @date 2020/5/23 0023 下午 3:24
 */
public class RedmineIssue {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 耗费工时
     */
    private Double hour;

    /**
     * issue
     */
    private String issue;

    public RedmineIssue(String name, Double hour, String issue) {
        this.name = name;
        this.hour = hour;
        this.issue = issue;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
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
}
