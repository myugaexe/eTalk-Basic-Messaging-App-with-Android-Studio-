package com.example.etalk;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Kontak> kontakList;
    private KontakAdapter kAdapter;
    private ListView contactListView;
    private ImageView tambahKontak, cariKontak;
    private EditText cariKontakInput;

    //Tambahan Data dari System
    private String[][] dataKontak = {
            {"Kontak1", "11111111111"},
            {"Kontak2", "11111111112"},
            {"Kontak3", "11111111113"},
            {"Kontak4", "11111111114"},
            {"Kontak5", "11111111115"},
            {"Kontak6", "11111111116"},
            {"Kontak7", "11111111117"},
            {"Kontak8", "11111111118"},
            {"Kontak9", "11111111119"},
            {"Kontak10", "1111111120"},
            {"Kontak11", "1111111121"},
            {"Kontak12", "1111111122"},
            {"Kontak13", "1111111123"},
            {"Kontak14", "1111111124"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactListView = findViewById(R.id.contactListView);
        tambahKontak = findViewById(R.id.TambahKontak);
        cariKontak = findViewById(R.id.ImageButtonCariKontak);
        cariKontakInput = findViewById(R.id.CariKontak);

        kontakList = new ArrayList<>();

        addDefaultContacts();

        kAdapter = new KontakAdapter(this, kontakList);
        contactListView.setAdapter(kAdapter);

        tambahKontak.setOnClickListener(v -> showContactForm());

        cariKontak.setOnClickListener(v -> searchContacts());
    }

    private void addDefaultContacts() {
        for (String[] data : dataKontak) {
            String nama = data[0];
            String nomorHp = data[1];
            kontakList.add(new Kontak(nama, nomorHp));
        }
    }

    private void showContactForm() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View addContactView = inflater.inflate(R.layout.add_kontak, null);

        EditText nameInput = addContactView.findViewById(R.id.nameInput);
        EditText phoneInput = addContactView.findViewById(R.id.phoneInput);

        nameInput.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});

        phoneInput.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(addContactView)
                .setTitle("Tambah Kontak")
                .setPositiveButton("Save", (dialog, which) -> {
                    String nama = nameInput.getText().toString().trim();
                    String nomorHp = phoneInput.getText().toString().trim();

                    if (!nama.isEmpty() && !nomorHp.isEmpty()) {
                        kontakList.add(new Kontak(nama, nomorHp));
                        kAdapter.notifyDataSetChanged();
                        showCustomToast("Kontak disimpan", R.drawable.ic_logobiru);
                    } else {
                        showCustomToast("Nama dan No HP tidak boleh kosong!", R.drawable.ic_logobiru);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void searchContacts() {
        String query = cariKontakInput.getText().toString().trim().toLowerCase();
        if (query.isEmpty()) {
            showCustomToast("Masukkan nama atau nomor telepon untuk mencari", R.drawable.ic_logobiru);
            return;
        }

        ArrayList<Kontak> filteredList = new ArrayList<>();
        for (Kontak kontak : kontakList) {
            if (kontak.getNama().toLowerCase().contains(query) || kontak.getNomorTelepon().contains(query)) {
                filteredList.add(kontak);
            }
        }

        if (filteredList.isEmpty()) {
            showCustomToast("Kontak tidak ditemukan", R.drawable.ic_logobiru);
        } else {
            showCustomToast("Kontak ditemukan", R.drawable.ic_logobiru);
            kAdapter = new KontakAdapter(this, filteredList);
            contactListView.setAdapter(kAdapter);

            new android.os.Handler().postDelayed(() -> {
                kAdapter = new KontakAdapter(MainActivity.this, kontakList);
                contactListView.setAdapter(kAdapter);
            }, 5000);
        }
    }

    private void showCustomToast(String message, int imageResource) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_root));

        TextView toastText = layout.findViewById(R.id.toastText);
        toastText.setText(message);

        ImageView toastImage = layout.findViewById(R.id.toastImage);
        toastImage.setImageResource(imageResource);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
