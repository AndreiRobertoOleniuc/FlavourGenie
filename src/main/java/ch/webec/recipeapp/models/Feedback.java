package ch.webec.recipeapp.models;

import jakarta.persistence.*;

@Entity
public class Feedback {

    @Id
    private Long id;
    private int rating;

    public Feedback() {
    }

    public Feedback(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
