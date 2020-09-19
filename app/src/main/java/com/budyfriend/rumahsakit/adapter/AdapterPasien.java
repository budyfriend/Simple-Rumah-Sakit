package com.budyfriend.rumahsakit.adapter;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budyfriend.rumahsakit.R;
import com.budyfriend.rumahsakit.dialog.dialogPasien;
import com.budyfriend.rumahsakit.model.dataPasien;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterPasien extends RecyclerView.Adapter<AdapterPasien.KaryawanViewHolder> {
    Context context;
    ArrayList<dataPasien> dataPasienArrayList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Activity activity;


    public AdapterPasien(Context context, ArrayList<dataPasien> dataPasienArrayList, Activity activity) {
        this.context = context;
        this.dataPasienArrayList = dataPasienArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public KaryawanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pasien, parent, false);
        return new KaryawanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KaryawanViewHolder holder, int position) {
        holder.viewBind(dataPasienArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataPasienArrayList.size();
    }

    public class KaryawanViewHolder extends RecyclerView.ViewHolder {
        TextView txt_nama,
                txt_jk,
                txt_tanggal_lahir,
                txt_telepon,
                txt_alamat;

        public KaryawanViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nama = itemView.findViewById(R.id.txt_nama);
            txt_jk = itemView.findViewById(R.id.txt_jk);
            txt_tanggal_lahir = itemView.findViewById(R.id.txt_tanggal_lahir);
            txt_telepon = itemView.findViewById(R.id.txt_telepon);
            txt_alamat = itemView.findViewById(R.id.txt_alamat);

        }


        public void viewBind(final dataPasien dataPasien) {

            txt_nama.setText("Nama : " + dataPasien.getNama_lengkap());
            txt_jk.setText("Jenis Kelamin : " + dataPasien.getJk());
            txt_tanggal_lahir.setText(dataPasien.getTempat_lahir() + " " + dataPasien.getTgl_lahir());
            txt_telepon.setText("Telepon : " + dataPasien.getTelepon());
            txt_alamat.setText("Alamat : " + dataPasien.getAlamat());

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                    dialogPasien pasien = new dialogPasien("ubah", context, dataPasien);
                    pasien.show(fragmentManager, "fm-pasien");
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Apa kamu yakin ingin menghapus data ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseReference.child("pasien").child(dataPasien.getId()).removeValue();
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

        }
    }
}
