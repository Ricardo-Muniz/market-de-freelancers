package br.com.nouva.freelancer.Model;

public class Project {

    public String name;
    public String type;
    public String title;
    public String content;
    public String uid;

    public Project(String uid, String name, String type, String title, String content) {
        this.name = name;
        this.type = type;
        this.title = title;
        this.content = content;
        this.uid = uid;
    }


}
