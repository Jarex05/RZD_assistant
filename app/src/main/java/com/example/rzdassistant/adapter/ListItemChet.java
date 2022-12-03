package com.example.rzdassistant.adapter;

import java.io.Serializable;

public class ListItemChet implements Serializable {

    private String title_chet;
    private String piket_start_chet;
    private String title_finish_chet;
    private String piket_finish_chet;
    private String speed_chet;
    private int id_chet = 0;

    public String getTitle_chet() {
        return title_chet;
    }

    public void setTitle_chet(String title_chet) {
        this.title_chet = title_chet;
    }

    public String getPiket_start_chet() {
        return piket_start_chet;
    }

    public void setPiket_start_chet(String piket_start_chet) {
        this.piket_start_chet = piket_start_chet;
    }

    public String getTitle_finish_chet() {
        return title_finish_chet;
    }

    public void setTitle_finish_chet(String title_finish_chet) {
        this.title_finish_chet = title_finish_chet;
    }

    public String getPiket_finish_chet() {
        return piket_finish_chet;
    }

    public void setPiket_finish_chet(String piket_finish_chet) {
        this.piket_finish_chet = piket_finish_chet;
    }

    public String getSpeed_chet() {
        return speed_chet;
    }

    public void setSpeed_chet(String speed_chet) {
        this.speed_chet = speed_chet;
    }

    public int getId_chet() {
        return id_chet;
    }

    public void setId_chet(int id_chet) {
        this.id_chet = id_chet;
    }
}
