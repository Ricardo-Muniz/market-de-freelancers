package br.com.nouva.freelancer.Model;

public class FilePost {

    public String name;
    public String type;
    public String title;
    public String content;
    private String encodedImage;

    public FilePost(String name, String type, String title, String content, String encodedImage) {
        this.name = name;
        this.type = type;
        this.title = title;
        this.content = content;
        this.encodedImage = encodedImage;
    }


}
