package com.example.modul10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modul10.api.ApiConfig;
import com.example.modul10.model.Mahasiswa;
import com.example.modul10.model.UpdateMahasiswaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMahasiswaActivity extends AppCompatActivity {

  private EditText edtNrp, edtNama, edtEmail, edtJurusan;
  private Button btUpdate;
  private ProgressBar progressBar;
  private String id, nrp, nama, email, jurusan;

  private Mahasiswa mahasiswa;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_mahasiswa);

    edtNrp = findViewById(R.id.edtNrp);
    edtNama = findViewById(R.id.edtNama);
    edtEmail = findViewById(R.id.edtEmail);
    edtJurusan = findViewById(R.id.edtJurusan);
    btUpdate = findViewById(R.id.btUpdate);
    progressBar = findViewById(R.id.progressBar);

    id = getIntent().getStringExtra("id");
    nrp = getIntent().getStringExtra("nrp");
    nama = getIntent().getStringExtra("nama");
    email = getIntent().getStringExtra("email");
    jurusan = getIntent().getStringExtra("jurusan");

    edtNrp.setText(nrp);
    edtNama.setText(nama);
    edtEmail.setText(email);
    edtJurusan.setText(jurusan);

    btUpdate.setOnClickListener(view -> {
      showLoading(true);
      updateMahasiswa();
    });
  }

  private void updateMahasiswa() {
    showLoading(true);
    String updatedNrp = edtNrp.getText().toString();
    String updatedNama = edtNama.getText().toString();
    String updatedEmail = edtEmail.getText().toString();
    String updatedJurusan = edtJurusan.getText().toString();

    if (updatedNrp.isEmpty() || updatedNama.isEmpty() || updatedEmail.isEmpty() || updatedJurusan.isEmpty()) {
      Toast.makeText(UpdateMahasiswaActivity.this, "Silahkan lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show();
      showLoading(false);
    } else {
      Call<UpdateMahasiswaResponse> client = ApiConfig.getApiService().updMahasiswa(id, updatedNrp, updatedNama, updatedEmail, updatedJurusan);
      client.enqueue(new Callback<UpdateMahasiswaResponse>() {
        @Override
        public void onResponse(Call<UpdateMahasiswaResponse> call, Response<UpdateMahasiswaResponse> response) {
          showLoading(false);
          if (response.isSuccessful() && response.body() != null) {
            Toast.makeText(UpdateMahasiswaActivity.this, "Berhasil mengupdate data mahasiswa!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateMahasiswaActivity.this, SearchMahasiswaActivity.class);
            startActivity(intent);
            finish();
          } else {
            Toast.makeText(UpdateMahasiswaActivity.this, "Gagal mengupdate data mahasiswa", Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void onFailure(Call<UpdateMahasiswaResponse> call, Throwable t) {
          showLoading(false);
          Toast.makeText(UpdateMahasiswaActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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