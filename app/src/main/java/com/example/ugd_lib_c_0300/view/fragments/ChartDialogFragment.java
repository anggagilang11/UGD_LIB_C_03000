package com.example.ugd_lib_c_0300.view.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.data.View;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

x
import com.example.ugd_lib_c_0300.databinding.FragmentChartDialogBinding;
import com.example.ugd_lib_c_0300.model.YearlyPeak;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChartDialogFragment extends DialogFragment {

    private FragmentChartDialogBinding binding;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * TODO 1.11 isi logic untuk kelas onCreateDialog
     * di bagian ini kalian perlu ambil data peakYearlyOnlineUser dari bundle lalu gunaain GSON untuk menjadikan
     * data menjadi list kembali. Untuk jenis chartnya kalian bebas menggunakan segala jenis chart yang disediakan library.
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentChartDialogBinding.inflate(LayoutInflater.from(requireContext()));
        String json = getArguments().getString("peakYearlyOnlineUser");
        APIlib.getInstance().setActiveAnyChartView(binding.myChart);

        binding.myChart.setProgressBar(binding.progressBar);
        Cartesian cartesian = AnyChart.column();

        binding.myChart.setChart(cartesian);


        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<YearlyPeak>>(){}.getType();
        List<YearlyPeak> data  = gson.fromJson(json, listType);

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Graph");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Year");
        cartesian.yAxis(0).title("Value");

        return new AlertDialog.Builder(requireActivity()).setView(binding.getRoot()).create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}