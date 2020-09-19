package com.budyfriend.rumahsakit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.budyfriend.rumahsakit.adapter.AdapterPasien;
import com.budyfriend.rumahsakit.dialog.dialogPasien;
import com.budyfriend.rumahsakit.model.dataPasien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KaryawanActivity extends AppCompatActivity {
    Context context;
    RecyclerView recyclerView;
    FloatingActionButton fab_add;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    AdapterPasien adapterPasien;
    ArrayList<dataPasien> dataPasienArrayList = new ArrayList<>();
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan);
        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        recyclerView = findViewById(R.id.recyclerView);
        fab_add = findViewById(R.id.fab_add);


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPasien pasien  = new dialogPasien("tambah",context);
                pasien.show(getSupportFragmentManager(),"fm-pasien");
            }
        });

        showData();
    }

    private void showData() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        databaseReference.child("pasien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataPasienArrayList.clear();
                for (DataSnapshot item : snapshot.getChildren()){
                    dataPasien  pasien = item.getValue(dataPasien.class);
                    if (pasien!= null){
                        pasien.setId(item.getKey());
                        dataPasienArrayList.add(pasien);
                    }
                }
                adapterPasien = new AdapterPasien(context,dataPasienArrayList, KaryawanActivity.this);
                recyclerView.setAdapter(adapterPasien);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}