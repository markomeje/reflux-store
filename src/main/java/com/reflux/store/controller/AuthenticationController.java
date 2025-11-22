package com.reflux.store.controller;
import com.reflux.store.enums.UserRoleEnum;
import com.reflux.store.exception.ResourceNotFoundException;
import com.reflux.store.models.Role;
import com.reflux.store.models.User;
import com.reflux.store.repositories.RoleRepository;
import com.reflux.store.repositories.UserRepository;
import com.reflux.store.request.auth.LoginRequest;
import com.reflux.store.request.auth.SignupRequest;
import com.reflux.store.response.auth.UserInfoResponse;
import com.reflux.store.security.AuthUserDetails;
import com.reflux.store.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        JwtUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
            userDetails.getId(),
            userDetails.getUsername(),
            roles
        );

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequest signupRequest) {
        if(userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> reqRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if(reqRoles == null) {
            Role userRole = roleRepository.findRoleByName(UserRoleEnum.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            roles.add(userRole);
        }else {
            reqRoles.forEach(reqRole -> {
                String role = reqRole.toUpperCase();
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findRoleByName(UserRoleEnum.ADMIN)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                        roles.add(adminRole);
                        break;
                    case "SELLER":
                        Role sellerRole = roleRepository.findRoleByName(UserRoleEnum.SELLER)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findRoleByName(UserRoleEnum.USER)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                        roles.add(userRole);

                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok().body("User registered successfully");
    }

    @GetMapping("/user")
    public ResponseEntity<?> currentUser(Authentication authentication) {
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        UserInfoResponse response = new UserInfoResponse(
            userDetails.getId(),
            userDetails.getUsername(),
            roles
        );

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body("You are logged out");
    }

}
