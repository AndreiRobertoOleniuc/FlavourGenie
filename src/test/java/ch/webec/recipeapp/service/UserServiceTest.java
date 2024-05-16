package ch.webec.recipeapp.service;

import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.UserRepository;
import ch.webec.recipeapp.services.UserService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    UserService service;

    UserServiceTest() {
        List<User> users = new ArrayList<>();
        //Generated a lot of users
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setEmail("user" + i + "@example.com");
            user.setFirstName("User" + i);
            user.setLastName("LastName" + i);
            user.setPicture("https://example.com/picture" + i + ".jpg");
            users.add(user);
        }

        var stub = mock(UserRepository.class);
        for (User user : users) {
            when(stub.findByEmail(user.getEmail())).thenReturn(user);
        }
        service = new UserService(stub);
    }

    @Test
    void testNotFoundUser() {
        User user = service.findUserByEmail("User");
        assertNull(user);
    }

    @Test
    void testFoundUser() {
        User user = service.findUserByEmail("user1@example.com");
        assertNotNull(user);
    }

    @Test
    void testUserProperty() {
        User user = service.findUserByEmail("user1@example.com");
        assertNotNull(user);
        assertEquals("User1", user.getFirstName());
        assertEquals("LastName1", user.getLastName());
        assertEquals("https://example.com/picture1.jpg", user.getPicture());
    }

    @Test
    void testUserEquals() {
        User user1 = service.findUserByEmail("user1@example.com");
        User user2 = service.findUserByEmail("user10@example.com");
        assertNotEquals(user1, user2);
    }
}
