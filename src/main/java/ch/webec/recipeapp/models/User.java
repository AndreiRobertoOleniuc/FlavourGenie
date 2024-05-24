package ch.webec.recipeapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User{
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
    private String password;

    public User() {
    }

    public User(String email, String firstName, String lastName, String picture) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
    }

    public User(String email, String firstName, String lastName, String picture,String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
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

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getPicture(), user.getPicture()) && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getFirstName(), getLastName(), getPicture(), getPassword());
    }
}
