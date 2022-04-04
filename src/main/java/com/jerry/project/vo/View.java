package com.jerry.project.vo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_view")
public class View {
    @Id
    @GeneratedValue
    private Long id;
    private String IP;
    private int totalCount;
    private  int todayCount;
    @Temporal(TemporalType.TIMESTAMP)
    private  Date lastViewDate;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(int todayCount) {
        this.todayCount = todayCount;
    }

    public Date getLastViewDate() {
        return lastViewDate;
    }

    public void setLastViewDate(Date lastViewDate) {
        this.lastViewDate = lastViewDate;
    }
}