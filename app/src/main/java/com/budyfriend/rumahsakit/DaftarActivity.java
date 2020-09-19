package com.budyfriend.rumahsakit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.budyfriend.rumahsakit.model.dataKaryawan;
import com.budyfriend.rumahsakit.model.dataLogin;
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

public class DaftarActivity extends AppCompatActivity {
    FloatingActionButton fab_daftar;
    EditText txt_username,
            txt_nama_lengkap,
            txt_tempat_lahir,
            txt_tgl_lahir,
            txt_alamat,
            txt_telepon,
            txt_password,
            txt_konfirmasi;
    Button btn_tanggal;
    RadioGroup rd_group;
    Context context;
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Calendar calendar = Calendar.getInstance();
    RadioButton radioButton;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        context = this;
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pendaftaran");
        fab_daftar = findViewById(R.id.fab_daftar);
        txt_password = findViewById(R.id.password);
        txt_konfirmasi = findViewById(R.id.konfirmasi);
        txt_telepon = findViewById(R.id.telepon);
        txt_username = findViewById(R.id.username);
        txt_nama_lengkap = findViewById(R.id.nama_lengkap);
        txt_tempat_lahir = findViewById(R.id.tempat_lahir);
        txt_tgl_lahir = findViewById(R.id.tgl_lahir);
        txt_alamat = findViewById(R.id.alamat);
        btn_tanggal = findViewById(R.id.btn_tanggal);
        rd_group = findViewById(R.id.rd_group);

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

        fab_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String telepon = txt_telepon.getText().toString();
                final String username = txt_username.getText().toString();
                final String nama_lengkap = txt_nama_lengkap.getText().toString();
                final String tempat_lahir = txt_tempat_lahir.getText().toString();
                final String tgl_lahir = txt_tgl_lahir.getText().toString();
                final String alamat = txt_alamat.getText().toString();
                final String password = txt_password.getText().toString();
                String konfirmasi = txt_konfirmasi.getText().toString();
                if (telepon.isEmpty()) {
                    txt_telepon.setError("Data tidak boleh kosong");
                    txt_telepon.requestFocus();
                } else if (username.isEmpty()) {
                    txt_username.setError("Data tidak boleh kosong");
                    txt_username.requestFocus();
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
                } else if (password.isEmpty()) {
                    txt_password.setError("Data tidak boleh kosong");
                    txt_password.requestFocus();
                } else if (konfirmasi.isEmpty()) {
                    txt_konfirmasi.setError("Data tidak boleh kosong");
                    txt_konfirmasi.requestFocus();
                } else if (!password.equals(konfirmasi)) {
                    txt_konfirmasi.setError("Konfirmasi sandi salah");
                    txt_konfirmasi.requestFocus();
                } else {
                    int selec = rd_group.getCheckedRadioButtonId();
                    radioButton = findViewById(selec);
                    databaseReference.child("login").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(context, "Data sudah ada", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("karyawan").child(username).setValue(
                                        new dataKaryawan(username, nama_lengkap, tempat_lahir, tgl_lahir, alamat, telepon, radioButton.getText().toString(),false)
                                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Menunggu validasi pendaftaran", Toast.LENGTH_SHORT).show();
                                        databaseReference.child("login").child(username)
                                                .setValue(new dataLogin(username, password, "karyawan", false));
                                        startActivity(new Intent(DaftarActivity.this,LoginActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        });
    }
}