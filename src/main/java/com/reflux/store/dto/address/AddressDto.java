package com.reflux.store.dto.address;
import com.reflux.store.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String street;
    private String buildingName;
    private String city;
    private String state;
    private String country;
    private User user;
}
