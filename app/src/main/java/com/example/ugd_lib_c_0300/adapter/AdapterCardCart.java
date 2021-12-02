package com.example.ugd_lib_c_0300.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.ugd_lib_c_0300.R;
import com.example.ugd_lib_c_0300.databinding.AdapterCartBinding;
import com.example.ugd_lib_c_0300.model.PurchasedGame;

import java.util.List;

public class AdapterCardCart extends RecyclerView.Adapter<AdapterCardCart.CartViewHolder> {

    private List<PurchasedGame> purchasedGameList;
    private OnCartClickListener listener;

    public AdapterCardCart(List<PurchasedGame> purchasedGameList, OnCartClickListener listener) {
        this.purchasedGameList = purchasedGameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(
                AdapterCartBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false
                ), listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(purchasedGameList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return purchasedGameList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private AdapterCartBinding binding;
        private OnCartClickListener lister;

        public CartViewHolder(AdapterCartBinding binding, OnCartClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.lister = listener;
        }


        /**
         * TODO 2.0 isi logic untuk Method Bind()
         * Method ini digunakan untuk bind data ke adapter_cart.xml
         *
         * @param purchasedGame
         * @HINT: method bind biasa seperti guided, kalo buat onclick interface udah ada contoh di adapter yang laen
         * referensi lain: https://www.youtube.com/watch?v=69C1ljfDvl0&ab_channel=CodingWithMitch
         * <p>
         * TODO BONUS 2.2 tambahkan logic untuk button tambah dan kurang
         */
        public void bind(PurchasedGame purchasedGame, int position) {
            binding.title.setText(purchasedGame.getGame().getTitle());
            binding.price.setText(purchasedGame.getGame().getFormattedPrice());
            binding.jumlahBeli.setText(purchasedGame.getJumlah() + "");

            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .placeholder(R.drawable.ic_launcher_background)
                    .priority(Priority.IMMEDIATE)
                    .encodeFormat(Bitmap.CompressFormat.PNG)
                    .format(DecodeFormat.DEFAULT);

            Glide.with(binding.getRoot())
                    .applyDefaultRequestOptions(requestOptions)
                    .load(purchasedGame.getGame().getUrlImage())
                    .into(binding.thumbNail);


            binding.buttonTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lister.onTambah(purchasedGame, position);
                }
            });

            binding.buttonKurang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lister.onKurang(purchasedGame, position);
                }
            });

        }
    }
}
