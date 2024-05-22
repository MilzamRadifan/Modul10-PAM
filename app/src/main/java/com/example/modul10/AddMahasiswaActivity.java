package com.example.modul10;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.modul10.api.ApiConfig;
import com.example.modul10.model.AddMahasiswaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMahasiswaActivity extends AppCompatActivity {

  private EditText edtNrp;
  private EditText edtNama;
  private EditText edtEmail;
  private EditText edtJurusan;
  private ProgressBar progressBar;
  private Button btnAdd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_mahasiswa);

    edtNrp = findViewById(R.id.edtNrp);
    edtNama = findViewById(R.id.edtNama);
    edtEmail = findViewById(R.id.edtEmail);
    edtJurusan = findViewById(R.id.edtJurusan);
    progressBar = findViewById(R.id.progressBar);
    btnAdd = findViewById(R.id.btnAdd);
//    btnListData = findViewById(R.id.btnList);

    btnAdd.setOnClickListener(view -> {
      addDataMahasiswa();
    });


  }

  private void addDataMahasiswa() {
    showLoading(true);
    String nrp = edtNrp.getText().toString();
    String nama = edtNama.getText().toString();
    String email = edtEmail.getText().toString();
    String jurusan = edtJurusan.getText().toString();

    if (nrp.isEmpty() || nama.isEmpty() || email.isEmpty() || jurusan.isEmpty()){
      Toast.makeText(AddMahasiswaActivity.this, "Silahkan lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show();
      showLoading(false);
    } else {
      Call<AddMahasiswaResponse> client = ApiConfig.getApiService().addMahasiswa(nrp, nama, email, jurusan);
      client.enqueue(new Callback<AddMahasiswaResponse>() {
        @Override
        public void onResponse(Call<AddMahasiswaResponse> call, Response<AddMahasiswaResponse> response) {
          showLoading(false);
          if (response.isSuccessful()){
            if (response.body() != null){
              Toast.makeText(AddMahasiswaActivity.this, "Berhasil menambahkan silahkan cek data pada halaman list!", Toast.LENGTH_SHORT).show();

              Intent intent = new Intent(AddMahasiswaActivity.this, SearchMahasiswaActivity.class);
              startActivity(intent);
            }
          } else {
            if (response.body() != null){
              Log.e("", "onFailure: " + response.body().getMessage());
            }
          }
        }

        @Override
        public void onFailure(Call<AddMahasiswaResponse> call, Throwable t) {

        }
      });
    }
  }


  private void showLoading(Boolean isLoading) {
    if (isLoading) {
      progressBar.setVisibility(View.VISIBLE);
    } else {
      progressBar.setVisibility(View.GONE);
    }
  }
}