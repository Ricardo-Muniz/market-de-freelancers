package br.com.nouva.freelancer.Async;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.nouva.freelancer.MainActivity;
import br.com.nouva.freelancer.Model.ModelReadFile;
import br.com.nouva.freelancer.R;
import br.com.nouva.freelancer.Response.ResponseAddProject;
import br.com.nouva.freelancer.Service.ServiceHome;
import br.com.nouva.freelancer.Service.ServiceImages;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadImages extends AsyncTask<Void, Integer, Boolean> {

    private ServiceImages apiService;
    private ServiceHome apiServiceHome;
    private WeakReference<MainActivity> context;
    private ArrayList<ModelReadFile> arrayOBJ;


    private final static String TAG = "UploadImages";

    private List<MultipartBody.Part> parts = new ArrayList<>();


    public UploadImages(MainActivity main, ArrayList<ModelReadFile> arrayOBJ) {
        this.context = new WeakReference<>(main);
        this.arrayOBJ = arrayOBJ;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected final Boolean doInBackground(Void... arrayLists) {
        boolean response = false;
        int count = 0;

        ModelReadFile modelReadFile = new ModelReadFile();
        ArrayList<ModelReadFile> files = new ArrayList<>();

        for (int i = 0; i < arrayOBJ.size(); i++) {
            int percent = i + 1;
            publishProgress(percent);

            String target = arrayOBJ.get(i).getTarget();
            Uri uri = arrayOBJ.get(i).getUri();
            modelReadFile.setTarget(target);
            modelReadFile.setUri(uri);
            files.add(modelReadFile);

            MultipartBody.Part body = readFileMultipart(target);
            parts.add(body);

            Log.d(TAG, "Part body: " + body);
            Log.d(TAG, "Parts: " + parts);

            count++;
            if (count == arrayOBJ.size()) response = true;
        }
        return response;
    }


    private MultipartBody.Part prepareFilePart(File file) {
        Log.d(TAG, "Prepare part: " + file.getName());

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestBody);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        initRetrofitClient();
        initRetrofitClientSave();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            multipartUpload(parts);
        }
    }

    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        apiService = new Retrofit.Builder().baseUrl("https://api-projects-freelancer.web.app/").client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceImages.class);
    }

    private void initRetrofitClientSave() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        apiServiceHome = new Retrofit.Builder().baseUrl("https://api-projects-freelancer.web.app/").client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceHome.class);
    }

    private void multipartUpload(List<MultipartBody.Part> body) {
        try {
            Thread.sleep(3000);
            String joined = TextUtils.join(", ", body);
            Log.d(TAG, "Upload: " + joined);

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "file");
            Call<ResponseBody> req = apiService.postImage(body, name);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        String uid = "";
                        try {
                            JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                            uid = json.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (!uid.isEmpty()) completeSaveDataProject(uid);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "error upload image: " + t.toString());
                    context.get().progressSubmit(false);
                    final Snackbar snackbar = Snackbar.make(context.get().findViewById(android.R.id.content), "Problem connect to server.",
                            Snackbar.LENGTH_INDEFINITE);

                    snackbar.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    }).setActionTextColor(ContextCompat.getColor(context.get(), R.color.colorAlertOrange)).show();
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

    }

    private void completeSaveDataProject(String uid) {
        Call<ResponseAddProject> request = apiServiceHome.addProject(context.get().getContentProject(uid));
        request.enqueue(new Callback<ResponseAddProject>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAddProject> call, @NonNull Response<ResponseAddProject> response) {
                if (Objects.requireNonNull(response.body()).success) {

                    context.get().progressSubmit(false);
                    context.get().newActSucces();

                    if (response.code() == 200) Thread.currentThread().interrupt();
                }

                Log.d(TAG, "response code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseAddProject> call, @NonNull Throwable t) {
                Log.e(TAG, "error: " + t.getMessage());
                context.get().progressSubmit(false);
                final Snackbar snackbar = Snackbar.make(context.get().findViewById(android.R.id.content), "Problem connect to server.",
                        Snackbar.LENGTH_INDEFINITE);

                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).setActionTextColor(ContextCompat.getColor(context.get(), R.color.colorAlertOrange)).show();

            }
        });
    }

    private MultipartBody.Part readFileMultipart(String nameFile) {
        Log.d(TAG, "Create file name: " + nameFile);
        String endDirectory = "/Android/data/br.com.nouva.freelancer/files/" + nameFile;
        String extStorageDirectory = Environment.getExternalStorageDirectory() + endDirectory;
        File file = new File(extStorageDirectory);

        return prepareFilePart(file);
    }

}
