package ch.webec.recipeapp.models;

import jakarta.persistence.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Recipe recipe;
    private int rating;

    public Feedback() {
    }

    public Feedback(int rating, User user, Recipe recipe) {
        this.rating = rating;
        this.user = user;
        this.recipe = recipe;
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

    public User getUser() {
        return user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", user=" + user +
                ", rating=" + rating +
                '}';
    }
}
