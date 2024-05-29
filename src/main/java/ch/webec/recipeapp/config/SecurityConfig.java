package ch.webec.recipeapp.config;

import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Copied from Co-Pilot
        OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = userRequest -> {
            OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

            if (userService.findUserByUsername(oidcUser.getEmail()) != null) {
                return oidcUser;
            }

            User user = new User(
                    oidcUser.getEmail(),
                    oidcUser.getGivenName(),
                    oidcUser.getFamilyName(),
                    oidcUser.getPicture()
            );
            userService.saveUser(user);

            return oidcUser;
        };

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = userRequest -> {
            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

            String username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email") : oauth2User.getAttribute("login");
            if (userService.findUserByUsername(username) != null) {
                return oauth2User;
            }

            User user = new User(
                    username,
                    oauth2User.getAttribute("name"),
                    null,
                    oauth2User.getAttribute("avatar_url")
            );
            userService.saveUser(user);

            return oauth2User;
        };

        return http.authorizeHttpRequests(req ->
                        req.requestMatchers("/api/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/404").permitAll()
                                .requestMatchers("/500").permitAll()
                                .requestMatchers("/502").permitAll()
                                .requestMatchers("/405").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/recipe")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login").permitAll().defaultSuccessUrl("/recipe")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService)
                                .userService(oauth2UserService)
                        )
                )
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userService)
                .build();
    }
}
