package com.example.ugd_lib_c_0300.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TODO 1.0 Tambah Anotasi Kelas Game
 * Kelas Model untuk data game yang diparse dari json file
 *
 * @HINT: mirip Guided tinggal kasih anotasi GSONnya :)
 */
public class Game {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("urlImage")
    private String urlImage;
    @SerializedName("currency")
    private String currency;
    @SerializedName("price")
    private double price;
    @SerializedName("description")
    private String description;
    @SerializedName("peakYearlyOnlineUser")
    private List<YearlyPeak> peakYearlyOnlineUser;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    /**
     * TODO 1.1 isi logic untuk Method getFormattedPrice()
     *
     * @return String gabungan currency dan price
     * @HINT: kalo harganya 0 balikin string Free to Play
     */
    public String getFormattedPrice() {
        return this.getPrice() == 0 ? "Free To Play" : this.getCurrency() + " " + this.getPrice();
    }

    public String getDescription() {
        return description;
    }

    public List<YearlyPeak> getPeakYearlyOnlineUser() {
        return peakYearlyOnlineUser;
    }
}
