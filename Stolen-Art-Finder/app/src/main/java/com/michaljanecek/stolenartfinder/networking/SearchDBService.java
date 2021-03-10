package com.michaljanecek.stolenartfinder.networking;


import com.michaljanecek.stolenartfinder.models.FoundPaintingModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SearchDBService {


    @Multipart
    @POST("api/painting/search/")
    Call<List<FoundPaintingModel>> searchByPainting(@Part MultipartBody.Part image,
                                                    @Part("k") RequestBody k);


}
