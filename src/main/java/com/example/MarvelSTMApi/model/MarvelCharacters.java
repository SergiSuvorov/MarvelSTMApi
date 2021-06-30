package com.example.MarvelSTMApi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "characters")
public class MarvelCharacters {
    @Id
    private String id;
    private String realName;
    private String fullName;
    private String photo;
    private String description;

    public MarvelCharacters(){};

    public MarvelCharacters( String fullName, String realName,String description, String photo) {
        this.realName = realName.toUpperCase();
        this.fullName = fullName.toUpperCase();
        this.description = description;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }



    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
