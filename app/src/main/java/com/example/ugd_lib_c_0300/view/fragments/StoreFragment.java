package com.example.ugd_lib_c_0300.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ugd_lib_c_0300.adapter.AdapterCardGame;
import com.example.ugd_lib_c_0300.adapter.OnCardClickListener;
import com.example.ugd_lib_c_0300.databinding.FragmentStoreBinding;
import com.example.ugd_lib_c_0300.model.Game;
import com.example.ugd_lib_c_0300.view.MainViewModel;
import com.google.gson.Gson;

import java.util.List;

public class StoreFragment extends Fragment implements OnCardClickListener {

    private FragmentStoreBinding binding;
    private MainViewModel viewModel;

    private AdapterCardGame adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * TODO 1.7 isi logic untuk method onViewCreated()
     * disini kalian hanya perlu mengobserve gameListLiveData dari viewmodel lalu panggil method
     * setupRecyclerview() didalamnya.
     *
     * @param view
     * @param savedInstanceState
     *
     * TODO BONUS 1.1 tambahkan queryTextListener untuk serchview
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.loadResponse();

        viewModel.getGameListLiveData().observe(getViewLifecycleOwner(), this::setupRecyclerView);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    /**
     * TODO 1.6 isi logic untuk Method setupRecyclerView()
     * method ini digunakan untuk membuat dan memasukan adapter ke recyclerview
     *
     * @param data
     * @HINT: panggil method ini sewaktu live data sedang diobserve
     */
    private void setupRecyclerView(List<Game> data) {
        adapter = new AdapterCardGame(data, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(Game game) {
        viewModel.addToCart(game);
        Toast.makeText(getContext(), "Game Behasil ditambahkan", Toast.LENGTH_SHORT).show();
    }

    /**
     * TODO 1.9 isi logic untuk Method onChartClick()
     * Method ini digunakan untuk memanggil dialogfragment yang akan menampilkan chart dari
     * jumlah pemain tertinggi setiap tahun.
     *
     * @param game
     * @HINT: gunakan FragmentManager untuk berpindah fragment dengan menambahkan argument bundle yang
     * menyimpan String hasil keluaran GSON dari list peakYearlyOnlineUser.
     */
    @Override
    public void onChartClick(Game game) {
        Bundle b = new Bundle();
        Gson g = new Gson();
        b.putString("json", g.toJson(game.getPeakYearlyOnlineUser()));
        ChartDialogFragment f = new ChartDialogFragment();
        f.setArguments(b);
        getFragmentManager().beginTransaction().add(f, "ChartDialogFragment").commit();
    }

    /**
     * TODO 1.8 isi logic untuk Method onDescriptionCLick
     * Method ini digunakan untuk memanggil dialogfragment yang menampilkan deskripsi dari game
     *
     * @param desc
     * @HINT: gunakan FragmentManager untuk berpindah fragment dengan menambahkan argument bundle yang
     * menyimpan string deskripsi
     */
    @Override
    public void onDescriptionClick(String desc) {
        Bundle b = new Bundle();
        Gson g = new Gson();
        b.putString("description", g.toJson(desc));
        DescriptionDialogFragment f = new DescriptionDialogFragment();
        f.setArguments(b);
        getFragmentManager().beginTransaction().add(f, "DescriptionDialogFragment").commit();
    }
}