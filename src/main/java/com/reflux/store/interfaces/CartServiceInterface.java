package com.reflux.store.interfaces;
import com.reflux.store.dto.cart.CartDto;

public interface CartServiceInterface {
    CartDto addProductToCart(Long productId, Integer quantity);
}
