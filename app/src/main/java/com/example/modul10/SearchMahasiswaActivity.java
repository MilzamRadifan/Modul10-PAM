package com.example.modul10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modul10.api.ApiConfig;
import com.example.modul10.model.DeleteMahasiswaResponse;
import com.example.modul10.model.Mahasiswa;
import com.example.modul10.model.MahasiswaResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMahasiswaActivity extends AppCompatActivity {
  private EditText edtChecNrp;
  private Button btnSearch;
  private FloatingActionButton btAdd;
  private ProgressBar progressBar;
  private List<Mahasiswa> mahasiswaList;
  private RecyclerView recyclerView;
  private MahasiswaAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_mahasiswa);

    edtChecNrp = findViewById(R.id.edtChckNrp);
    btnSearch = findViewById(R.id.btnSearch);
    progressBar = findViewById(R.id.progressBar);
    recyclerView = findViewById(R.id.rvMahasiswa);
    btAdd = findViewById(R.id.btAdd);
    mahasiswaList = new ArrayList<>();

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new MahasiswaAdapter(mahasiswaList, this::onDeleteClicked, this::onUpdateClicked);
    recyclerView.setAdapter(adapter);

    btnSearch.setOnClickListener(view -> {
      showLoading(true);
      String nrp = edtChecNrp.getText().toString();
      if (nrp.isEmpty()) {
        edtChecNrp.setError("Silahakan Isi nrp terlebih dahulu");
        showLoading(false);
      } else {
        searchMahasiswa(nrp);
      }
    });

    btAdd.setOnClickListener(view -> {
      Intent intent = new Intent(SearchMahasiswaActivity.this, AddMahasiswaActivity.class);
      startActivity(intent);
    });

    sortDataFromApi();
  }

  private void sortDataFromApi() {
    Call<MahasiswaResponse> client = ApiConfig.getApiService().getAllMahasiswa();
    client.enqueue(new Callback<MahasiswaResponse>() {
      @Override
      public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
          mahasiswaList.clear();
          mahasiswaList.addAll(response.body().getData());
          adapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
        t.printStackTrace();
      }
    });
  }

  private void searchMahasiswa(String nrp) {
    Call<MahasiswaResponse> client = ApiConfig.getApiService().getMahasiswaByNrp(nrp);
    client.enqueue(new Callback<MahasiswaResponse>() {
      @Override
      public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
        showLoading(false);
        if (response.isSuccessful() && response.body() != null) {
          mahasiswaList = response.body().getData();
          adapter.updateData(mahasiswaList);
        } else {
          Log.e("Error", "Response not successful");
        }
      }

      @Override
      public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
        showLoading(false);
        Log.e("Error Retrofit", "onFailure: " + t.getMessage());
      }
    });
  }

  private void onDeleteClicked(Mahasiswa mahasiswa) {
    showLoading(true);
    Call<DeleteMahasiswaResponse> call = ApiConfig.getApiService().delMahasiswa(mahasiswa.getId());
    call.enqueue(new Callback<DeleteMahasiswaResponse>() {
      @Override
      public void onResponse(Call<DeleteMahasiswaResponse> call, Response<DeleteMahasiswaResponse> response) {
        showLoading(false);
        if (response.isSuccessful() && response.body() != null) {
          Toast.makeText(SearchMahasiswaActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
          sortDataFromApi();
        } else {
          Log.e("Error", "Delete not successful");
        }
      }

      @Override
      public void onFailure(Call<DeleteMahasiswaResponse> call, Throwable t) {
        showLoading(false);
        Log.e("Error Retrofit", "onFailure: " + t.getMessage());
      }
    });
  }

  private void onUpdateClicked(Mahasiswa mahasiswa) {
    Intent intent = new Intent(SearchMahasiswaActivity.this, UpdateMahasiswaActivity.class);
    intent.putExtra("id", mahasiswa.getId());
    intent.putExtra("nrp", mahasiswa.getNrp());
    intent.putExtra("nama", mahasiswa.getNama());
    intent.putExtra("email", mahasiswa.getEmail());
    intent.putExtra("jurusan", mahasiswa.getJurusan());
    startActivity(intent);
  }

  private void showLoading(Boolean isLoading) {
    if (isLoading) {
      progressBar.setVisibility(View.VISIBLE);
    } else {
      progressBar.setVisibility(View.GONE);
    }
  }
}

class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

  private List<Mahasiswa> mahasiswaList;
  private final OnDeleteClickListener onDeleteClickListener;
  private final OnUpdateClickListener onUpdateClickListener;

  public interface OnDeleteClickListener {
    void onDeleteClicked(Mahasiswa mahasiswa);
  }

  public interface OnUpdateClickListener {
    void onUpdateClicked(Mahasiswa mahasiswa);
  }

  public MahasiswaAdapter(List<Mahasiswa> mahasiswaList, OnDeleteClickListener onDeleteClickListener, OnUpdateClickListener onUpdateClickListener) {
    this.mahasiswaList = mahasiswaList;
    this.onDeleteClickListener = onDeleteClickListener;
    this.onUpdateClickListener = onUpdateClickListener;
  }

  public void updateData(List<Mahasiswa> mahasiswaList) {
    this.mahasiswaList = mahasiswaList;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mhs, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Mahasiswa mahasiswa = mahasiswaList.get(position);
    holder.tvValNrp.setText(mahasiswa.getNrp());
    holder.tvValNama.setText(mahasiswa.getNama());
    holder.tvValEmail.setText(mahasiswa.getEmail());
    holder.tvValJurusan.setText(mahasiswa.getJurusan());

    holder.btnDelete.setOnClickListener(v -> onDeleteClickListener.onDeleteClicked(mahasiswa));
    holder.itemView.setOnClickListener(v -> onUpdateClickListener.onUpdateClicked(mahasiswa));
  }

  @Override
  public int getItemCount() {
    return mahasiswaList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvValNrp, tvValNama, tvValEmail, tvValJurusan;
    Button btnDelete;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvValNrp = itemView.findViewById(R.id.tvValNrp);
      tvValNama = itemView.findViewById(R.id.tvValNama);
      tvValEmail = itemView.findViewById(R.id.tvValEmail);
      tvValJurusan = itemView.findViewById(R.id.tvValJurusan);
      btnDelete = itemView.findViewById(R.id.button);
    }
  }
}