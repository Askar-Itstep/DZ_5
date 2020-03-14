package com.example.dz_5.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.sql.Date;

@Entity
@TypeConverters(DateConvert.class)
public class Album extends DBEntity implements Serializable {
    @PrimaryKey
    private Long id;
    private String title;
    private String leadsinger;
    private Date date;
    private String pathImage;

    public Album(Long id, String title, String leadsinger, Date date) {
        this.id = id;
        this.title = title;
        this.leadsinger = leadsinger;
        this.date = date;
    }
    @Ignore
    public Album(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeadsinger() {
        return leadsinger;
    }

    public void setLeadsinger(String leadsinger) {
        this.leadsinger = leadsinger;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

}
