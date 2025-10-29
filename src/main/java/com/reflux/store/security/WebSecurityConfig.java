package com.reflux.store.security;
import com.reflux.store.enums.UserRoleEnum;
import com.reflux.store.models.Role;
import com.reflux.store.models.User;
import com.reflux.store.repositories.RoleRepository;
import com.reflux.store.repositories.UserRepository;
import com.reflux.store.security.jwt.JwtAuthEntryPoint;
import com.reflux.store.security.jwt.JwtAuthTokenFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Set;

@Configuration
public class WebSecurityConfig {
    private final JwtAuthEntryPoint unauthorizedHandler;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public WebSecurityConfig(
        JwtAuthEntryPoint unauthorizedHandler,
        RoleRepository roleRepository,
        UserRepository userRepository
    ) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public JwtAuthTokenFilter jwtAuthTokenFilter() {
        return new JwtAuthTokenFilter();
    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        UserDetailsService userDetailsService = (UserDetailsService) this.authUserDetailsService;
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChainAlt(HttpSecurity http) throws Exception {
//        String authEndpoint = "/api/auth/**";
//        String docsEndpoint = "/docs/**";
//        String publicEndpoint = "/api/public/**";
//
//        http.authorizeHttpRequests(
//            auth -> auth.requestMatchers(authEndpoint).permitAll()
//            .requestMatchers(docsEndpoint).permitAll()
//            .requestMatchers(publicEndpoint).permitAll()
//            .anyRequest()
//            .authenticated()
//        );
//
//        http.authenticationProvider(daoAuthenticationProvider());
//        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
//        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        http.addFilterBefore(jwtAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/v1/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            );

        http.addFilterBefore(jwtAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/api/docs/**"));
    }

    @Bean
    public CommandLineRunner initializeData(PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findRoleByName(UserRoleEnum.USER)
                .orElseGet(() -> {
                    Role newUserRole = new Role(UserRoleEnum.USER);
                    return roleRepository.save(newUserRole);
                });

            Role sellerRole = roleRepository.findRoleByName(UserRoleEnum.SELLER)
                .orElseGet(() -> {
                    Role newSellerRole = new Role(UserRoleEnum.SELLER);
                    return roleRepository.save(newSellerRole);
                });

            Role adminRole = roleRepository.findRoleByName(UserRoleEnum.ADMIN)
                .orElseGet(() -> {
                    Role newAdminRole = new Role(UserRoleEnum.ADMIN);
                    return roleRepository.save(newAdminRole);
                });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(userRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);

            if(!userRepository.existsByUsername("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if(!userRepository.existsByUsername("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if(!userRepository.existsByUsername("admin")) {
                User admin1 = new User("admin1", "admin1@example.com", passwordEncoder.encode("password3"));
                userRepository.save(admin1);
            }

            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("seller1").ifPresent(user -> {
                user.setRoles(sellerRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("admin1").ifPresent(user -> {
                user.setRoles(adminRoles);
                userRepository.save(user);
            });
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}