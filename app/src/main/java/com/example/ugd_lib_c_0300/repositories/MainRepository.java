package com.example.ugd_lib_c_0300.repositories;

import android.content.Context;
import android.content.res.AssetManager;
import android.telecom.Call;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.example.ugd_lib_c_0300.model.Response;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class MainRepository {

    private final Context context;

    @Inject
    public MainRepository(Context context) {
        this.context = context;
    }

    /**
     * TODO 1.5 isi logic untuk Method getResponse()
     *
     * @return MutableLiveData dari kelas response yang nanti disimpan di viewmodel
     * @throws Exception
     * @HINT: mirip guided tapi bungkus hasil keluaran dengan MutableLiveData
     * cara menggunakan live data ada didokumentasi resmi android google
     * https://developer.android.com/topic/libraries/architecture/livedata?hl=id
     */
    public MutableLiveData<Response> getResponse() throws Exception {
        final MutableLiveData<Response> data = new MutableLiveData<>();
        BufferedReader r = (BufferedReader) loadJson();
        String json = r.lines().collect(Collectors.joining());
        Gson gson = new Gson();
        Response response = gson.fromJson(json, Response.class);
        data.postValue(response);
        r.close();
        return data;
    }

    /**
     * TODO 1.4 isi logic untuk Method loadJson()
     *
     * @return referensi file datagame.json dari folder assets
     * @throws Exception
     * @HINT: gunakan context yang ada dikelas ini agar bisa menggunakan method assets
     */
    private Reader loadJson() throws Exception {
        String json = null;
        try {
            AssetManager am = context.getAssets();

            InputStream is = am.open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new BufferedReader(new InputStreamReader(is));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
