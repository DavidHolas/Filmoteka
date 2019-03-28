package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Movie implements Serializable {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty genre;
    private SimpleIntegerProperty year;

    public Movie(String name, String genre, int year) {
        this.name = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.year = new SimpleIntegerProperty();
    }

    public Movie() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.year = new SimpleIntegerProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public int getYear() {
        return year.get();
    }

    public void setYear(int year) {
        this.year.set(year);
    }

}
