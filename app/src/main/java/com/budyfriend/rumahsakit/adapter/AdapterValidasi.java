package com.budyfriend.rumahsakit.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.budyfriend.rumahsakit.R;
import com.budyfriend.rumahsakit.model.dataKaryawan;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterValidasi extends RecyclerView.Adapter<AdapterValidasi.KaryawanViewHolder> {
    Context context;
    ArrayList<dataKaryawan> dataKaryawanArrayList;
    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference();

    public AdapterValidasi(Context context, ArrayList<dataKaryawan> dataKaryawanArrayList) {
        this.context = context;
        this.dataKaryawanArrayList = dataKaryawanArrayList;
    }

    @NonNull
    @Override
    public KaryawanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daftar, parent, false);
        return new KaryawanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KaryawanViewHolder holder, int position) {
        holder.viewBind(dataKaryawanArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataKaryawanArrayList.size();
    }

    public class KaryawanViewHolder extends RecyclerView.ViewHolder {
        TextView txt_nama,
                txt_jk,
                txt_tanggal_lahir,
                txt_telepon,
                txt_alamat;
        Button tolak,
                terima;

        public KaryawanViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nama = itemView.findViewById(R.id.txt_nama);
            txt_jk = itemView.findViewById(R.id.txt_jk);
            txt_tanggal_lahir = itemView.findViewById(R.id.txt_tanggal_lahir);
            txt_telepon = itemView.findViewById(R.id.txt_telepon);
            txt_alamat = itemView.findViewById(R.id.txt_alamat);
            tolak = itemView.findViewById(R.id.tolak);
            terima = itemView.findViewById(R.id.terima);
        }


        public void viewBind(final dataKaryawan dataKaryawan) {

            txt_nama.setText("Nama : "+dataKaryawan.getNama_lengkap());
            txt_jk.setText("Jenis Kelamin : "+dataKaryawan.getJk());
            txt_tanggal_lahir.setText(dataKaryawan.getTempat_lahir()+" "+dataKaryawan.getTgl_lahir());
            txt_telepon.setText("Telepon : "+dataKaryawan.getTelepon());
            txt_alamat.setText("Alamat : "+dataKaryawan.getAlamat());

            terima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Apa kamu yakin ingin terima?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseReference.child("login").child(dataKaryawan.getUsername()).child("active").setValue(true);
                            databaseReference.child("karyawan").child(dataKaryawan.getUsername()).child("active").setValue(true);
                            Toast.makeText(context, "Data berhasil diterima", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

                }
            });

            tolak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Apa kamu yakin ingin tolak?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseReference.child("login").child(dataKaryawan.getUsername()).removeValue();
                            Toast.makeText(context, "Data berhasil ditolak", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

                }
            });
        }
    }
}
