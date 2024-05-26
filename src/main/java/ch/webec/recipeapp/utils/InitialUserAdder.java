package ch.webec.recipeapp.utils;

import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.security.SecureRandom;

import static java.util.Collections.emptySet;

@Component
public class InitialUserAdder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    @Value("${basicUser_password:#{null}}")
    private String basicUserPassword;

    public InitialUserAdder(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        addInitialUsers();
    }

    private void addInitialUsers() {
        if(userRepo.findByUsername("basic") == null) {
           LoggerUtil.logInfo("Adding initial user 'basicUser'");

           String basicUserPw = basicUserPassword;
            if(basicUserPassword == null) {
                basicUserPw = generatePassword();
                LoggerUtil.logInfo("Password for 'basicUser': " + basicUserPw);
            }
            var basicUser = new User("basic", "Basic","User",
                    "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
                    passwordEncoder.encode(basicUserPw), emptySet());
            userRepo.save(basicUser);
        }
    }

    private String generatePassword() {
        var random = new SecureRandom();
        return new BigInteger(128, random).toString(32);
    }
}
