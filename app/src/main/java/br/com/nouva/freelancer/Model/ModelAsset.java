package br.com.nouva.freelancer.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class ModelAsset {

    private int index;
    private Uri uri;
    private boolean status;
    private Bitmap bitmap;
    private ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ArrayList<Bitmap> getBitmapArray() {
        return bitmapArray;
    }

    public void setBitmapArray(ArrayList<Bitmap> bitmapArray) {
        this.bitmapArray = bitmapArray;
    }

    public Object[] getImage() {
        this.index = getIndex();
        this.uri = getUri();
        this.status = isStatus();
        this.bitmap = getBitmap();
        return getImage();
    }

    public void setImage(int index, Uri uri, boolean status, Bitmap bitmap) {
        this.index = index;
        this.uri = uri;
        this.status = status;
        this.bitmap = bitmap;
    }


}
