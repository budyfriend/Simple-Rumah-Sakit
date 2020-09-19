package com.budyfriend.rumahsakit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import com.budyfriend.rumahsakit.adapter.AdapterValidasi;
import com.budyfriend.rumahsakit.model.dataKaryawan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context;
    ArrayList<dataKaryawan> karyawanArrayList = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    AdapterValidasi validasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        recyclerView = findViewById(R.id.recyclerView);
        context  = this;

        showData();
    }



    private void showData() {
        databaseReference.child("karyawan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                karyawanArrayList.clear();
                for (DataSnapshot item : snapshot.getChildren()){
                    dataKaryawan karyawan = item.getValue(dataKaryawan.class);
                    if (karyawan != null){
                        if (!karyawan.isActive()){
                            karyawan.setId(item.getKey());
                            karyawanArrayList.add(karyawan);
                        }

                    }

                }
                validasi  = new AdapterValidasi(context,karyawanArrayList);
                recyclerView.setAdapter(validasi);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}