package com.example.ugd_lib_c_0300.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.ugd_lib_c_0300.model.Game;
import com.example.ugd_lib_c_0300.model.PurchasedGame;
import com.example.ugd_lib_c_0300.model.Response;
import com.example.ugd_lib_c_0300.model.UserProfile;
import com.example.ugd_lib_c_0300.repositories.MainRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private MainRepository repo;

    private MutableLiveData<List<Game>> gameListLiveData;
    private MutableLiveData<String> errMsg;

    private MutableLiveData<List<PurchasedGame>> keranjangBelanja;

    private MutableLiveData<UserProfile> userProfileLiveData;

    {
        gameListLiveData = new MutableLiveData<>(new ArrayList<>());
        keranjangBelanja = new MutableLiveData<>(new ArrayList<>());
        userProfileLiveData = new MutableLiveData<>(null);
        errMsg = new MutableLiveData<>("");
    }

    @Inject
    public MainViewModel(MainRepository repo) {
        this.repo = repo;
    }

    public void loadResponse() {
        try {
            if (this.gameListLiveData.getValue().size() == 0) {
                LiveData<Response> temp = this.repo.getResponse();

                if (temp == null || temp.getValue() == null || temp.getValue().getData().isEmpty())
                    throw new Exception("Data gagal diambil");

                this.gameListLiveData.setValue(temp.getValue().getData());
                this.errMsg.setValue(temp.getValue().getMessage() + " size: " + temp.getValue().getData().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            errMsg.setValue(e.getMessage());
        }
    }

    /**
     * TODO 3.1 isi logic untuk method scanQRUserProfile()
     * @param jsonString
     * @throws Exception
     * @HINT: gunakan GSON untuk parse jsonString dengan kelas model UserProfile lalu tampung di
     * userProfileLiveData
     */
    public void scanQRUserProfile(String jsonString) throws Exception {

    }

    /**
     * TODO 1.12 isi logic untuk Method addToCart()
     * Method ini digunakan ketika mau menambahkan game diawal jadi saat milih game dari
     * StoreFragment buat dimasukin ke CartFragment
     *
     * @param game
     * @HINT: gunakan method cariGame() untuk cek apakah game yang dipilih dari storeFragment
     * sudah ada didalam Cart.
     */
    public void addToCart(Game game) {
        PurchasedGame searchGame = cariGame(game.getId());
        if(searchGame == null)
        {
            searchGame = new PurchasedGame(game, 1);
            keranjangBelanja.getValue().add(searchGame);
        }
        else{
            List<PurchasedGame> purchasedGames = keranjangBelanja.getValue();
            for(int i = 0; i < purchasedGames.size();i++) {
                if(purchasedGames.get(i).getGame().getId() == game.getId())
                {
                    purchasedGames.get(i).setJumlah(purchasedGames.get(i).getJumlah() + 1);
                    keranjangBelanja.postValue(purchasedGames);
                    break;
                }
            }
        }

    }

    /**
     * TODO BONUS 2.0 isi logic untuk method clearCart
     * Method ini digunakan untuk clearing cart setelah pdf digeneraete
     */
    public void clearCart() {
        keranjangBelanja.getValue().clear();
    }

    /**
     * TODO BONUS 2.3 isi logic untuk method addOneFromCart()
     * Method ini digunakan untuk button tambah di cartFragment
     * @param game
     * @HINT: gunakan method cariGame() untuk mendapat referensi game yang dimaksud
     */
    public void addOneFromCart(Game game) {
        PurchasedGame searchGame = cariGame(game.getId());
        if(searchGame == null)
        {
            searchGame = new PurchasedGame(game, 1);
            keranjangBelanja.getValue().add(searchGame);
        }
        else{
            List<PurchasedGame> purchasedGames = keranjangBelanja.getValue();
            for(int i = 0; i < purchasedGames.size();i++) {
                if(purchasedGames.get(i).getGame().getId() == game.getId())
                {
                    purchasedGames.get(i).setJumlah(purchasedGames.get(i).getJumlah() + 1);
                    keranjangBelanja.postValue(purchasedGames);
                    break;
                }
            }
        }
    }

    /**
     * TODO BONUS 2.4 isi logic untuk method removeFromCart()
     * Method ini digunakan untuk button kurang di cartFragment
     * @param game
     */
    public void removeFromCart(Game game) {
        PurchasedGame searchGame = cariGame(game.getId());
        if(searchGame == null)
        {

        }
        else{
            List<PurchasedGame> purchasedGames = keranjangBelanja.getValue();
            for(int i = 0; i < purchasedGames.size();i++) {
                if(purchasedGames.get(i).getGame().getId() == game.getId())
                {
                    purchasedGames.remove(i);
                    keranjangBelanja.postValue(purchasedGames);
                    break;
                }
            }
        }
    }

    private PurchasedGame cariGame(int id) {
        if (keranjangBelanja.getValue() != null) {
            List<PurchasedGame> belanjaan = keranjangBelanja.getValue();
            for (PurchasedGame game : belanjaan) {
                if (game.getGame().getId() == id)
                    return game;
            }
        }
        return null;
    }

    public LiveData<List<Game>> getGameListLiveData() {
        return gameListLiveData;
    }

    public LiveData<List<PurchasedGame>> getKeranjangBelanja() {
        return keranjangBelanja;
    }

    public LiveData<String> getErrMsg() {
        return errMsg;
    }

    public LiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }
}
