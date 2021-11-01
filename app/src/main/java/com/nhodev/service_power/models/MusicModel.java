package com.nhodev.service_power.models;

/**
 * Được tạo bởi Phạm Nhớ ngày 01/11/2021.
 **/
public class MusicModel {
    private int number;
    private String name;
    private String path;

    public MusicModel(int number, String name, String path) {
        this.number = number;
        this.name = name;
        this.path = path;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
