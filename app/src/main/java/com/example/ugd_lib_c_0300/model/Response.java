package com.example.ugd_lib_c_0300.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TODO 1.3 Tambah Anotasi Kelas Response
 */
public class Response {

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Game> data;

    public String getMessage() {
        return message;
    }

    public List<Game> getData() {
        return data;
    }
}
