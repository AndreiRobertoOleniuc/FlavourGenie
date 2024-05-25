package ch.webec.recipeapp.controllers.shared;

import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final UserRepository userRepository;

    public GlobalControllerAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication authentication){
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        String uri = request.getRequestURI();

        // Skip advice for /login and other public routes
        if (uri.equals("/login") || uri.equals("/register")) {
            return;
        }

        User user = null;
        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            user = userRepository.findByEmail(oidcUser.getEmail());
        } else if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            user = userRepository.findByEmail(userDetails.getUsername());
        }

        if (user != null) {
            model.addAttribute("user", user);
        }
    }
}
