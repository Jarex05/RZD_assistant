package com.example.rzdassistant.adapter;

import java.io.Serializable;

public class ListItemNechet implements Serializable {

    private String title_nechet;
    private String piket_start_nechet;
    private String title_finish_nechet;
    private String piket_finish_nechet;
    private String speed_nechet;
    private int id_nechet = 0;

    public String getTitle_nechet() {
        return title_nechet;
    }

    public void setTitle_nechet(String title_nechet) {
        this.title_nechet = title_nechet;
    }

    public String getPiket_start_nechet() {
        return piket_start_nechet;
    }

    public void setPiket_start_nechet(String piket_start_nechet) {
        this.piket_start_nechet = piket_start_nechet;
    }

    public String getTitle_finish_nechet() {
        return title_finish_nechet;
    }

    public void setTitle_finish_nechet(String title_finish_nechet) {
        this.title_finish_nechet = title_finish_nechet;
    }

    public String getPiket_finish_nechet() {
        return piket_finish_nechet;
    }

    public void setPiket_finish_nechet(String piket_finish_nechet) {
        this.piket_finish_nechet = piket_finish_nechet;
    }

    public String getSpeed_nechet() {
        return speed_nechet;
    }

    public void setSpeed_nechet(String speed_nechet) {
        this.speed_nechet = speed_nechet;
    }

    public int getId_nechet() {
        return id_nechet;
    }

    public void setId_nechet(int id_nechet) {
        this.id_nechet = id_nechet;
    }
}
