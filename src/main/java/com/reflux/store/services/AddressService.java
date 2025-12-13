package com.reflux.store.services;
import com.reflux.store.dto.address.AddressDto;
import com.reflux.store.exception.ResourceNotFoundException;
import com.reflux.store.interfaces.AddressServiceInterface;
import com.reflux.store.models.Address;
import com.reflux.store.models.User;
import com.reflux.store.repositories.UserRepository;
import com.reflux.store.repositories.address.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService implements AddressServiceInterface {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public AddressService(
        AddressRepository addressRepository,
        ModelMapper modelMapper,
        UserRepository userRepository
    ) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public AddressDto createUserAddress(AddressDto addressDto, User user) {
        Address address = modelMapper.map(addressDto, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAddressList() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
            .map(address -> modelMapper.map(address, AddressDto.class))
            .toList();
    }

    @Override
    public AddressDto getAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
           .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public AddressDto updateUserAddress(Long addressId, AddressDto addressDto) {
        Address existingAddress = addressRepository.findById(addressId)
           .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        existingAddress.setCountry(addressDto.getCountry());
        existingAddress.setBuildingName(addressDto.getBuildingName());
        existingAddress.setState(addressDto.getState());
        existingAddress.setCity(addressDto.getCity());
        existingAddress.setStreet(addressDto.getStreet());
        Address updatedAddress = addressRepository.save(existingAddress);

        User user = existingAddress.getUser();
        user.getAddresses().removeIf(address -> address.getId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);
        return modelMapper.map(updatedAddress, AddressDto.class);
    }
}
