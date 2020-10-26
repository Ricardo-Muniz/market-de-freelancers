package br.com.nouva.freelancer.Service;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceImages {

    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part List<MultipartBody.Part> image, @Part("file") RequestBody name);
}
