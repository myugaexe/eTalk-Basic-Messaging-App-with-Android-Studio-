package com.example.etalk;

import android.content.Context;
import android.content.Intent;  // Tambahkan ini untuk menggunakan Intent
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;

public class KontakAdapter extends ArrayAdapter<Kontak> {

    private Context context;
    private ArrayList<Kontak> kontakList;

    public KontakAdapter(Context context, ArrayList<Kontak> kontakList) {
        super(context, 0, kontakList);
        this.context = context;
        this.kontakList = kontakList;
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView toastText = layout.findViewById(R.id.toastText);
        toastText.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Kontak kontak = getItem(position);

        TextView contactName = convertView.findViewById(R.id.contactName);
        TextView contactDetails = convertView.findViewById(R.id.contactDetails);
        ImageView editImage = convertView.findViewById(R.id.editImage);
        ImageView deleteImage = convertView.findViewById(R.id.deleteImage);

        contactName.setText(kontak.getNama());
        contactDetails.setText(kontak.getNomorTelepon());

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("contactName", kontak.getNama());
            context.startActivity(intent);
        });

        editImage.setOnClickListener(view -> showEditContactDialog(position));

        deleteImage.setOnClickListener(view -> {
            kontakList.remove(position);
            notifyDataSetChanged();
            showCustomToast("Kontak dihapus");
        });

        return convertView;
    }

    private void showEditContactDialog(int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View editContactView = inflater.inflate(R.layout.add_kontak, null);

        EditText nameInput = editContactView.findViewById(R.id.nameInput);
        EditText phoneInput = editContactView.findViewById(R.id.phoneInput);

        Kontak kontak = kontakList.get(position);
        nameInput.setText(kontak.getNama());
        phoneInput.setText(kontak.getNomorTelepon());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(editContactView)
                .setTitle("Edit Kontak")
                .setPositiveButton("Save", (dialog, which) -> {
                    String nama = nameInput.getText().toString().trim();
                    String nomorHp = phoneInput.getText().toString().trim();

                    if (!nama.isEmpty() && !nomorHp.isEmpty()) {
                        kontakList.set(position, new Kontak(nama, nomorHp));
                        notifyDataSetChanged();
                        showCustomToast("Kontak diperbarui");
                    } else {
                        showCustomToast("Nama dan No HP tidak boleh kosong!");
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}
