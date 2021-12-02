package com.example.ugd_lib_c_0300.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ugd_lib_c_0300.databinding.FragmentDescriptionDialogBinding;

public class DescriptionDialogFragment extends DialogFragment {

    private FragmentDescriptionDialogBinding binding;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * TODO 1.10 isi logic untuk method onCreateDialog()
     * pada method ini kalian hanya perlu mengambil data dari bundle lalu ditampilkan di textview
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentDescriptionDialogBinding.inflate(LayoutInflater.from(getContext()));
        String description = getArguments().getString("description");
        binding.txtDesc.setText(description);
        return new AlertDialog.Builder(requireActivity()).setView(binding.getRoot()).create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}