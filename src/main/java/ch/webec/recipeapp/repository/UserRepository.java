package ch.webec.recipeapp.repository;

import ch.webec.recipeapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{
    User findByEmail(String email);
}
