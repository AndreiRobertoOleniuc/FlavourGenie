package ch.webec.recipeapp.models;

import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.Length;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Recipe {
    @Id
    @GeneratedValue
    private Long id; // assuming there is an ID field for the entity

    private String recipeName;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Category> categories;
    private String recipeDifficulty;
    private String description;
    private String cookingTime;
    private String recipeImageDescription;
    @Column(length = 1000)
    private String instruction;
    private String recipeImage;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Recipe(String recipeName, List<Ingredient> ingredients, List<Category> categories,
                  String recipeDifficulty, String description, String cookingTime,
                  String recipeImageDescription, String instruction, String recipeImage, User user) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.categories = categories;
        this.recipeDifficulty = recipeDifficulty;
        this.description = description;
        this.cookingTime = cookingTime;
        this.recipeImageDescription = recipeImageDescription;
        this.instruction = instruction;
        this.recipeImage = recipeImage;
        this.user = user;
    }

    protected Recipe(){}

    public String getRecipeName() {
        return recipeName;
    }

    public Long getId() {
        return id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getRecipeDifficulty() {
        return recipeDifficulty;
    }

    public String getDescription() {
        return description;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public String getRecipeImageDescription() {
        return recipeImageDescription;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public User getUser() {
        return user;
    }
}
