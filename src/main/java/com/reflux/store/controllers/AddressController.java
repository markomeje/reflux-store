package com.reflux.store.controllers;
import com.reflux.store.dto.address.AddressDto;
import com.reflux.store.models.User;
import com.reflux.store.security.AuthSecurity;
import com.reflux.store.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;
    private final AuthSecurity authSecurity;

    public AddressController(AddressService addressService, AuthSecurity authSecurity) {
        this.addressService = addressService;
        this.authSecurity = authSecurity;
    }

    @PostMapping("/create")
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) {
        User user = authSecurity.getLoggedInUser();
        AddressDto savedAddressDto = addressService.createUserAddress(addressDto, user);
        return new ResponseEntity<>(savedAddressDto, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AddressDto>> addressList() {
        List<AddressDto> addressList = addressService.getAddressList();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long addressId) {
        AddressDto addressDto = addressService.getAddress(addressId);
        return new ResponseEntity<>(addressDto, HttpStatus.OK);
    }

    @GetMapping("/update/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long addressId, @RequestBody AddressDto addressDto) {
        AddressDto updatedAddressDto = addressService.updateUserAddress(addressId, addressDto);
        return new ResponseEntity<>(updatedAddressDto, HttpStatus.OK);
    }
}
