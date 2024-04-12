package ch.webec.recipeapp.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String quantity;
    private String unit;

    public Ingredient(String name, String quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    protected Ingredient(){}

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
