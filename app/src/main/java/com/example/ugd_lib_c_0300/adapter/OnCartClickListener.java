package com.example.ugd_lib_c_0300.adapter;


import com.example.ugd_lib_c_0300.model.PurchasedGame;

public interface OnCartClickListener {
    void onTambah(PurchasedGame purchasedGame, int position);
    void onKurang(PurchasedGame purchasedGame, int position);
}
