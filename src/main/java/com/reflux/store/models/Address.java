package com.reflux.store.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 50, message = "Street name must be between 5 to 50 characters")
    private String street;

    @NotBlank
    @Size(min = 3, max = 25, message = "Building name name must be between 3 to 25 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 3, max = 20, message = "City name name must be between 3 to 20 characters")
    private String city;

    @NotBlank
    @Size(min = 3, max = 20, message = "State name name must be between 3 to 20 characters")
    private String state;

    @NotBlank
    @Size(min = 2, max = 55, message = "Country name name must be between 2 to 55 characters")
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String country, String state, String city) {
        this.street = street;
        this.country = country;
        this.state = state;
        this.city = city;
    }
}
