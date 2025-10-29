package com.reflux.store.interfaces;
import com.reflux.store.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsServiceInterface {
    UserDetails findUserByUsername(String username) throws ResourceNotFoundException;
}
