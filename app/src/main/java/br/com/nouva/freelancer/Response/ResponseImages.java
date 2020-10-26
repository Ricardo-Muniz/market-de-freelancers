package br.com.nouva.freelancer.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseImages {

    @SerializedName("message")
    @Expose
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
