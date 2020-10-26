package br.com.nouva.freelancer.Async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import br.com.nouva.freelancer.MainActivity;
import br.com.nouva.freelancer.Model.ModelReadFile;

public class ProcessImages extends AsyncTask<Object, Integer, Boolean> {

    private static final String TAG = "ProcessImages";
    private ArrayList<ModelReadFile> filesINDEX = new ArrayList<>();
    private ArrayList<ModelReadFile> arrayOBJ;
    private WeakReference<MainActivity> context;

    public ProcessImages(MainActivity main, ArrayList<ModelReadFile> arrayOBJ) {
        this.context = new WeakReference<>(main);
        this.arrayOBJ = arrayOBJ;
    }

    @Override
    protected Boolean doInBackground(Object... arrayLists) {
        ModelReadFile model;
        boolean response = false;
        int count = 0;

        for (int i = 0; i < arrayOBJ.size(); i++) {
            try {
                ModelReadFile modelReadFile = new ModelReadFile();

                int percent = i + 1;
                publishProgress(percent);
                Thread.sleep(1000);

                Uri uri = arrayOBJ.get(i).getUri();
                String target = arrayOBJ.get(i).getTarget();

                modelReadFile.setUri(uri);
                modelReadFile.setTarget(target);

                model = new ModelReadFile();
                model.setImage(modelReadFile.getUri(), modelReadFile.getTarget());

                createStream(uri, target);

                count++;
                if (count == arrayOBJ.size()) response = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG, "Error thread process img: " + e.getMessage());
            }

        }

        return response;
    }

    private void createStream(Uri uri, String target) {

        if (target.equals("profile")) {
            String finalExtension = ".png";
            target = target + finalExtension;

        } else if(target.equals("_0_")) {
            String finalExtension = ".png";
            target = "wallpaper" + finalExtension;

        } else {
            String absoluteName = "asset";
            String finalExtension = ".png";
            target = absoluteName + target + finalExtension;

        }

        ModelReadFile filesNAME = new ModelReadFile();
        filesNAME.setTarget(target);
        filesNAME.setUri(uri);
        filesINDEX.add(filesNAME);

        String joined = TextUtils.join(", ", filesINDEX);
        Log.d(TAG, "List async: " + joined);


        String endDirectory = "/Android/data/br.com.nouva.freelancer/files";
        String extStorageDirectory = Environment.getExternalStorageDirectory() + endDirectory;

        try {
            InputStream stream = Objects.requireNonNull(context.get().getContentResolver().openInputStream(uri));
            Bitmap bm = BitmapFactory.decodeStream(stream);

            File file = new File(extStorageDirectory);
            file.mkdirs();

            FileOutputStream out = new FileOutputStream(new File(file, target));
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);

            try {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            context.get().uploadIMGS(filesINDEX);
        }
    }
}
