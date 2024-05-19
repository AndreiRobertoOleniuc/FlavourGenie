package ch.webec.recipeapp.config;
import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Copied from Co-Pilot
        OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = userRequest -> {
            OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

            if(userService.findUserByEmail(oidcUser.getEmail()) != null){
                return oidcUser;
            };

            User user = new User(
                oidcUser.getEmail(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                oidcUser.getPicture()
            );
            userService.saveUser(user);

            return oidcUser;
        };

        return http.authorizeHttpRequests(req ->
                        req.requestMatchers("/api/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/404").permitAll()
                                .requestMatchers("/500").permitAll()
                                .requestMatchers("/502").permitAll()
                                .requestMatchers("/405").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers( "/css/**", "/img/**", "/js/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login").permitAll().defaultSuccessUrl("/recipe")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService)
                        )
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
