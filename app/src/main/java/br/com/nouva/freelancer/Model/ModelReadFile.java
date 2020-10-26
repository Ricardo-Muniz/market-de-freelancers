package br.com.nouva.freelancer.Model;

import android.net.Uri;

public class ModelReadFile {

    private Uri uri;
    private String target;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setImage(Uri uri, String target) {
        this.uri = uri;
        this.target = target;
    }
}
