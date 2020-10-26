package br.com.nouva.freelancer.Model;

import android.net.Uri;

public class ModelImagesParser {

    private int index;
    private Uri uri;
    private boolean status;
    private ModelAsset asset;

    public ModelImagesParser() {
    }

    public ModelImagesParser(int index, Uri uri, boolean status) {
        this.index = index;
        this.uri = uri;
        this.status = status;
    }

    public ModelAsset getAsset() {
        return asset;
    }

    public void setAsset(ModelAsset asset) {
        this.asset = asset;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setImage(int index, Uri uri, boolean status) {
        this.index = index;
        this.uri = uri;
        this.status = status;
    }
}
