package com.reflux.store.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "id")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 20, name = "username")
    private String username;

    @NotBlank
    @Size(min = 1, max = 120)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "products",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Set<Product> products = new HashSet<>();
}
