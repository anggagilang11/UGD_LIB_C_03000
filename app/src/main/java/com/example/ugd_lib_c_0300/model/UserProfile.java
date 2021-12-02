package com.example.ugd_lib_c_0300.model;


import com.google.gson.annotations.SerializedName;

/**
 * TODO 3.0 Tambah Anotasi kelas UserProfile
 * Kelas Model untuk data user hasil scan qr
 */
public class UserProfile {
    @SerializedName("username")
    private String username;
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }
}
