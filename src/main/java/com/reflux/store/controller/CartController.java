package com.reflux.store.controller;

import com.reflux.store.dto.cart.CartDto;
import com.reflux.store.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private CartService cartService;

    @PostMapping("/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart(
        @PathVariable Long productId,
        @PathVariable Integer quantity
    ) {
        CartDto cartDto = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
