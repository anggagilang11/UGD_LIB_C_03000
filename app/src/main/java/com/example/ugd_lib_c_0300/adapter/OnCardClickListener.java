package com.example.ugd_lib_c_0300.adapter;


import com.example.ugd_lib_c_0300.model.Game;

public interface OnCardClickListener {
    void onClick(Game game);

    void onChartClick(Game game);

    void onDescriptionClick(String desc);
}
