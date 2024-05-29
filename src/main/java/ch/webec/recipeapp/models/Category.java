package ch.webec.recipeapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String category;

    public Category(String category) {
        this.category = category;
    }

    protected Category() {
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }
}
