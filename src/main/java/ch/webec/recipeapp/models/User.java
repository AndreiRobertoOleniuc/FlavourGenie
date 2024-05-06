package ch.webec.recipeapp.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
    private String password;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Recipe> recipes;

    public User() {
    }

    public User(String email, String firstName, String lastName, String picture, List<Recipe> recipes) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.recipes = recipes;
    }

    public User(String email, String firstName, String lastName, String picture,  List<Recipe> recipes,String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.recipes = recipes;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }
}
