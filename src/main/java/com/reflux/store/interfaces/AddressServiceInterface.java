package com.reflux.store.interfaces;
import com.reflux.store.dto.address.AddressDto;
import com.reflux.store.models.User;

import java.util.List;

public interface AddressServiceInterface {
    AddressDto createUserAddress(AddressDto addressDto, User user);
    List<AddressDto> getAddressList();
    AddressDto getAddress(Long addressId);
    AddressDto updateUserAddress(Long addressId, AddressDto addressDto);
}
