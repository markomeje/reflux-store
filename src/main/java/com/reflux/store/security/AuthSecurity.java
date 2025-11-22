package com.reflux.store.security;
import com.reflux.store.exception.ResourceNotFoundException;
import com.reflux.store.models.User;
import com.reflux.store.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthSecurity {
    private final UserRepository userRepository;

    public AuthSecurity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getLoggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getEmail();
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Long getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getId();
    }
}
