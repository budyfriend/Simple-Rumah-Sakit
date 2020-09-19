package com.budyfriend.rumahsakit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.budyfriend.rumahsakit.model.dataLogin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText txt_username,
            txt_password;

    Button btn_login,
            btn_daftar;
    Context context;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        txt_username = findViewById(R.id.username);
        txt_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        btn_daftar = findViewById(R.id.btn_daftar);

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DaftarActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txt_username.getText().toString();
                final String password = txt_password.getText().toString();
                if (username.isEmpty()){
                    txt_username.setError("Data tidak boleh kosong");
                    txt_username.requestFocus();
                }else if (password.isEmpty()){
                    txt_password.setError("Data tidak boleh kosong");
                    txt_password.requestFocus();
                }else {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    databaseReference.child("login").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                dataLogin login = snapshot.getValue(dataLogin.class);
                                if (login!= null){
                                    login.setId(snapshot.getKey());
                                    if (login.getPassword().equals(password)){
                                        if(login.isActive()){
                                            if (login.getLevel().equals("admin")){
                                                startActivity(new Intent(context,DashboardActivity.class));
//                                                finish();
                                                progressDialog.dismiss();
                                            }else {
                                                startActivity(new Intent(context,KaryawanActivity.class));
//                                                finish();
                                                progressDialog.dismiss();
                                            }
                                        }else {
                                            Toast.makeText(context,"Data belum aktif",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }else {
                                        Toast.makeText(context,"Password Salah",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            }else {
                                Toast.makeText(context,"Data belum terdaftar",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });

    }
}