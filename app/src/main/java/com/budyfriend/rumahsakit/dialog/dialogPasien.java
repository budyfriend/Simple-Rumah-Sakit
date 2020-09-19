package com.budyfriend.rumahsakit.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.budyfriend.rumahsakit.DaftarActivity;
import com.budyfriend.rumahsakit.LoginActivity;
import com.budyfriend.rumahsakit.R;
import com.budyfriend.rumahsakit.model.dataKaryawan;
import com.budyfriend.rumahsakit.model.dataLogin;
import com.budyfriend.rumahsakit.model.dataPasien;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class dialogPasien extends DialogFragment {
    String pilih;
    Context context;
    dataPasien dataPasien;

    public dialogPasien(String pilih, Context context) {
        this.pilih = pilih;
        this.context = context;
    }

    public dialogPasien(String pilih, Context context, com.budyfriend.rumahsakit.model.dataPasien dataPasien) {
        this.pilih = pilih;
        this.context = context;
        this.dataPasien = dataPasien;
    }

    Button btn_simpan;
    EditText
            txt_nama_lengkap,
            txt_tempat_lahir,
            txt_tgl_lahir,
            txt_alamat,
            txt_telepon;

    Button btn_tanggal;
    RadioGroup rd_group;

    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Calendar calendar = Calendar.getInstance();
    RadioButton radioButton;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ProgressDialog progressDialog;
    RadioButton rb_laki_laki,
            rb_perempuan;


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.form_pasien, container, false);

        progressDialog = new ProgressDialog(context);
        btn_simpan = v.findViewById(R.id.btn_simpan);
        txt_telepon = v.findViewById(R.id.telepon);
        txt_nama_lengkap = v.findViewById(R.id.nama_lengkap);
        txt_tempat_lahir = v.findViewById(R.id.tempat_lahir);
        txt_tgl_lahir = v.findViewById(R.id.tgl_lahir);
        txt_alamat = v.findViewById(R.id.alamat);
        btn_tanggal = v.findViewById(R.id.btn_tanggal);
        rd_group = v.findViewById(R.id.rd_group);
        rb_laki_laki = v.findViewById(R.id.rb_laki_laki);
        rb_perempuan = v.findViewById(R.id.rb_perempuan);

        if (pilih.equals("ubah")) {
            txt_telepon.setText(dataPasien.getTelepon());
            txt_nama_lengkap.setText(dataPasien.getNama_lengkap());
            txt_tempat_lahir.setText(dataPasien.getTempat_lahir());
            txt_tgl_lahir.setText(dataPasien.getTgl_lahir());
            txt_alamat.setText(dataPasien.getAlamat());

            if (dataPasien.getJk().equals("Laki-laki")) {
                rb_laki_laki.setChecked(true);
                rb_perempuan.setChecked(false);
            } else {
                rb_laki_laki.setChecked(true);
                rb_perempuan.setChecked(false);
            }
        }

        btn_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        txt_tgl_lahir.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().getTouchables().get(0).performClick(); //Menampilkan tahun dulu
                datePickerDialog.show(); // Tampilkan DatePicker dialog
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String telepon = txt_telepon.getText().toString();
                final String nama_lengkap = txt_nama_lengkap.getText().toString();
                final String tempat_lahir = txt_tempat_lahir.getText().toString();
                final String tgl_lahir = txt_tgl_lahir.getText().toString();
                final String alamat = txt_alamat.getText().toString();
                if (telepon.isEmpty()) {
                    txt_telepon.setError("Data tidak boleh kosong");
                    txt_telepon.requestFocus();
                } else if (nama_lengkap.isEmpty()) {
                    txt_nama_lengkap.setError("Data tidak boleh kosong");
                    txt_nama_lengkap.requestFocus();
                } else if (tempat_lahir.isEmpty()) {
                    txt_tempat_lahir.setError("Data tidak boleh kosong");
                    txt_tempat_lahir.requestFocus();
                } else if (tgl_lahir.isEmpty()) {
                    txt_tgl_lahir.setError("Data tidak boleh kosong");
                    txt_tgl_lahir.requestFocus();
                } else if (alamat.isEmpty()) {
                    txt_alamat.setError("Data tidak boleh kosong");
                    txt_alamat.requestFocus();
                } else {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    int selec = rd_group.getCheckedRadioButtonId();
                    radioButton = v.findViewById(selec);
                    if (pilih.equals("ubah")){
                        databaseReference.child("pasien").child(dataPasien.getNama_lengkap()).setValue(
                                new dataPasien(nama_lengkap, tempat_lahir, tgl_lahir, alamat, telepon, radioButton.getText().toString())
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                dismiss();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                dismiss();
                                progressDialog.dismiss();
                            }
                        });

                    }else {
                        databaseReference.child("pasien").child(nama_lengkap).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(context, "Data sudah ada", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseReference.child("pasien").child(nama_lengkap).setValue(
                                            new dataPasien(nama_lengkap, tempat_lahir, tgl_lahir, alamat, telepon, radioButton.getText().toString())
                                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                            progressDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            dismiss();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }



                }
            }
        });
        return v;
    }
}
