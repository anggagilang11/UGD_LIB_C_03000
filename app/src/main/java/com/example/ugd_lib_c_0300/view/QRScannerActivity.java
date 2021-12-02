package com.example.ugd_lib_c_0300.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;


import com.example.ugd_lib_c_0300.databinding.ActivityQrscannerBinding;


public class QRScannerActivity extends AppCompatActivity {

    private ActivityQrscannerBinding binding;

    private CodeScanner codeScanner;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new
                    ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    setupScanner();
                } else {
                    Toast.makeText(this, "Butuh Izin Akses Kamera",
                            Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrscannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupPermission();
    }

    private void setupPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            setupScanner();
        } else if
        (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showDialogOK("Camera Services Permission required for this app",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent();

                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    finish();
                                    break;
                            }
                        }
                    });
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA);
        }
    }

    private void setupScanner() {
        codeScanner = new CodeScanner(this, binding.scannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                Intent intent = new Intent();
                intent.putExtra("QR_RESULT", result.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        binding.scannerView.setOnClickListener(v -> {
            codeScanner.startPreview();
        });
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener
            okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (codeScanner != null)
            codeScanner.releaseResources();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (codeScanner != null)
            codeScanner.startPreview();
    }
}
