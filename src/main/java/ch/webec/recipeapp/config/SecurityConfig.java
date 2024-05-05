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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

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

            User user = new User(
                oidcUser.getEmail(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                oidcUser.getPicture()
            );
            userService.saveUser(user);

            return oidcUser;
        };

//        OAuth2UserService<OAuth2UserRequest, OAuth2User> githubUserService = userRequest -> {
//            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
//
//            User user = new User(
//                    oauth2User.getAttribute("email"),
//                    oauth2User.getAttribute("name"),
//                    oauth2User.getAttribute("login"), // GitHub does not provide given name and family name, use login instead
//                    oauth2User.getAttribute("avatar_url") // GitHub uses avatar_url for the profile picture
//            );
//            userService.saveUser(user);
//
//            return oauth2User;
//        };

        return http.authorizeHttpRequests(req ->
                        req.requestMatchers("/api/**").permitAll()
                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/register").permitAll()
                        .requestMatchers( "/css/**", "/img/**", "/js/**").permitAll()
                                .anyRequest().authenticated()
                )
//                .formLogin(login -> login
//                        .loginPage("/login").permitAll()
//                        .defaultSuccessUrl("/recipe")
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                        .failureForwardUrl("/login?error=true")
//                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login").permitAll()
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService)
//                                .userService(githubUserService) // For OAuth2 providers
                        )
                )
                .csrf(AbstractHttpConfigurer::disable)
//                .userDetailsService(userService)
                .build();
    }
}
