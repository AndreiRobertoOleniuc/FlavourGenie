package ch.webec.recipeapp.config;
import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;  // Inject the existing PasswordEncoder

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Copied from Co-Pilot
        OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = userRequest -> {
            OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

            if(userService.findUserByEmail(oidcUser.getEmail()) != null){
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
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/recipe")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login").permitAll().defaultSuccessUrl("/recipe")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService)
                        )
                )
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userService)
                .build();
    }
}
