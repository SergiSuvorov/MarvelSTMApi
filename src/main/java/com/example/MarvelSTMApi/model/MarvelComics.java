package com.example.MarvelSTMApi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "comics")
public class MarvelComics {
    @Id
    private String id;
    private String title;
    private String description;
    private String writer;
    private Date dateOfPublication;
    private List<MarvelCharacters> characters;
    private String image;

    public MarvelComics(){};

    public MarvelComics(String title, String description, String writer, Date dateOfPublication, List<MarvelCharacters> characters, String image) {
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.dateOfPublication = dateOfPublication;
        this.characters = characters;
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateOfPublication() {
        return dateOfPublication;
    }

    public String getImage() {
        return image;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateOfPublication(Date dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<MarvelCharacters> getCharacters() {
        return characters;
    }

    public void setCharacters(List<MarvelCharacters> characters) {
        this.characters = characters;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}

