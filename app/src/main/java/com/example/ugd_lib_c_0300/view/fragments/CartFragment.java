package com.example.ugd_lib_c_0300.view.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ugd_lib_c_0300.adapter.AdapterCardCart;
import com.example.ugd_lib_c_0300.adapter.OnCartClickListener;
import com.example.ugd_lib_c_0300.databinding.FragmentCartBinding;
import com.example.ugd_lib_c_0300.model.PurchasedGame;
import com.example.ugd_lib_c_0300.view.MainViewModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CartFragment extends Fragment implements OnCartClickListener {

    private FragmentCartBinding binding;
    private MainViewModel viewModel;

    private AdapterCardCart adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * TODO 2.4 isi logic untuk method onViewCreate()
     * tambahkan onclicklistener untuk button cetakNota yang nanti akan memanggil method createPdf()
     * berikan pengecekan apakah sudah scan qr.
     *
     * @param view
     * @param savedInstanceState
     *
     * TODO BONUS 2.1 panggil method clearCart setelah pdf berhasil dibuat.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getKeranjangBelanja().observe(getViewLifecycleOwner(), data -> {
            setupAdapter(data);
            if (data.size() == 0) {
                binding.cetakNota.setVisibility(View.GONE);
                binding.textKosong.setVisibility(View.VISIBLE);
            } else {
                binding.cetakNota.setVisibility(View.VISIBLE);
                binding.textKosong.setVisibility(View.GONE);
            }
        });

        binding.cetakNota.setOnClickListener(v -> {
            try {
                cetakPdf();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * TODO 2.2 isi logic untuk Method cetakPDF()
     * method ini digunakan untuk membuat pdf berdasarkan data game yang dibeli
     * (keranjangBelanja dikelas viewmodel)
     * jangan lupa pada bagian nama pembeli di set sesuai dengan nama pembeli di QR
     *
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    private void cetakPdf() throws FileNotFoundException, DocumentException {
        File folder = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        if (!folder.exists()) {
            folder.mkdir();
        }

        Date currentTime = Calendar.getInstance().getTime();
        String pdfName = currentTime.getTime() + ".pdf";

        File pdfFile = new File(folder.getAbsolutePath(), pdfName);
        FileOutputStream outputStream = new FileOutputStream(pdfFile);

        com.itextpdf.text.Document document = new
                com.itextpdf.text.Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // bagian header
        Paragraph judul = new Paragraph("DATA CART \n\n",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 16,
                        Font.BOLD, BaseColor.BLACK));

        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);

        // Buat tabel
        PdfPTable tables = new PdfPTable(new float[]{16, 8});

        // Settingan ukuran tabel
        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);

        Paragraph kepada = new Paragraph( "Kepada Yth: \n" + "Budi Setiawan" + "\n",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 10,
                        Font.NORMAL, BaseColor.BLACK));
        cellSupplier.addElement(kepada);
        tables.addCell(cellSupplier);

        Paragraph NomorTanggal = new Paragraph(
                "No : " + "123456789" + "\n\n" +
                        "Tanggal : " + new SimpleDateFormat("dd/MM/yyyy",
                        Locale.getDefault()).format(currentTime) + "\n",
                new
                        com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                        com.itextpdf.text.Font.NORMAL, BaseColor.BLACK));

        NomorTanggal.setPaddingTop(5);
        tables.addCell(NomorTanggal);
        document.add(tables);
        com.itextpdf.text.Font f = new

                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\nBerikut detail data: \n\n", f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{5, 5, 5, 5, 5});

        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);

        // Setup Column
        PdfPCell h1 = new PdfPCell(new Phrase("Game"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Price"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h3 = new PdfPCell(new Phrase("Qty"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);


        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h3);

        // Beri warna untuk kolumn
        for (PdfPCell cells : tableHeader.getRow(0).getCells()) {
            cells.setBackgroundColor(BaseColor.PINK);
        }

        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{5, 5, 5, 5, 5});

        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        // masukan data pegawai jadi baris
        LiveData<List<PurchasedGame>>  keranjang = viewModel.getKeranjangBelanja();
        for (PurchasedGame P : keranjang.getValue()) {
            tableData.addCell(P.getGame().getTitle());
            tableData.addCell(P.getGame().getFormattedPrice());
            tableData.addCell(P.getJumlah() + "");

        }

        document.add(tableData);

        com.itextpdf.text.Font h = new

                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL);
        String tglDicetak = currentTime.toLocaleString();
        Paragraph P = new Paragraph("\nDicetak tanggal " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P);
        document.close();
        previewPdf(pdfFile);
        Toast.makeText(getActivity(), "PDF berhasil dibuat", Toast.LENGTH_SHORT).show();

    }

    /**
     * TODO 2.3 isi logic untuk Method previewPDF()
     * method ini digunakan untuk menampilkan pdf setelah digenerate
     *
     * @param pdfFile
     * @HINT: apabila saat dijalankan method ini terlihat seperti tidak berjalan coba cek apakah
     * hp kalian memiliki aplikasi untuk membuka pdf.
     */
    private void previewPdf(File pdfFile) {
        PackageManager packageManager = getActivity().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(testIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Uri uri;
            uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() +
                            ".provider",
                    pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            getActivity().grantUriPermission(getActivity().getPackageName(), uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(pdfIntent);
        }
    }


    /**
     * TODO 2.1 isi logic untuk method setupAdapter()
     * method ini digunakan untuk membuat dan memasukan adapter ke recyclerview
     *
     * @param purchasedGameList
     */
    private void setupAdapter(List<PurchasedGame> purchasedGameList) {
        adapter = new AdapterCardCart(purchasedGameList, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter = null;
    }

    @Override
    public void onTambah(PurchasedGame purchasedGame, int position) {
        viewModel.addOneFromCart(purchasedGame.getGame());
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onKurang(PurchasedGame purchasedGame, int position) {
        viewModel.removeFromCart(purchasedGame.getGame());
        adapter.notifyItemChanged(position);
    }

}