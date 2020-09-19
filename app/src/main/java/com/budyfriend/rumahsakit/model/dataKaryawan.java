package com.budyfriend.rumahsakit.model;

public class dataKaryawan {
    String id;
    String username;
    String nama_lengkap;
    String tempat_lahir;
    String tgl_lahir;
    String alamat;
    String telepon;
    String jk;
    boolean active;

    public dataKaryawan(String username, String nama_lengkap, String tempat_lahir, String tgl_lahir, String alamat, String telepon, String jk, boolean active) {
        this.username = username;
        this.nama_lengkap = nama_lengkap;
        this.tempat_lahir = tempat_lahir;
        this.tgl_lahir = tgl_lahir;
        this.alamat = alamat;
        this.telepon = telepon;
        this.jk = jk;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public dataKaryawan() {
    }

    public String getJk() {
        return jk;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTelepon() {
        return telepon;
    }
}
