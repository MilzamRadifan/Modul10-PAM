package com.example.modul10.api;

import com.example.modul10.model.AddMahasiswaResponse;
import com.example.modul10.model.DeleteMahasiswaResponse;
import com.example.modul10.model.MahasiswaResponse;
import com.example.modul10.model.UpdateMahasiswaResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
  @GET("mahasiswa")
  Call<MahasiswaResponse> getMahasiswaByNrp(@Query("nrp") String nrp);

  @GET("mahasiswa")
  Call<MahasiswaResponse> getAllMahasiswa();
  @POST("mahasiswa")
  @FormUrlEncoded
  Call<AddMahasiswaResponse> addMahasiswa(
          @Field("nrp") String nrp,
          @Field("nama") String nama,
          @Field("email") String email,
          @Field("jurusan") String jurusan
  );

  @PUT("mahasiswa")
  @FormUrlEncoded
  Call<UpdateMahasiswaResponse> updMahasiswa(
          @Field("id") String id,
          @Field("nrp") String nrp,
          @Field("nama") String nama,
          @Field("email") String email,
          @Field("jurusan") String jurusan
  );

//  @DELETE gabisa pake @FormUrlEncoded
//  @DELETE("mahasiswa")
//  @FormUrlEncoded
//  Call<DeleteMahasiswaResponse> delMahasiswa(
//          @Field("id") String id
//  );

  @HTTP(method = "DELETE", path = "mahasiswa", hasBody = true)
  @FormUrlEncoded
  Call<DeleteMahasiswaResponse> delMahasiswa(@Field("id") String id);
}