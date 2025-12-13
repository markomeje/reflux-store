package com.reflux.store.controllers;
import com.reflux.store.dto.cart.CartDto;
import com.reflux.store.security.AuthSecurity;
import com.reflux.store.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final AuthSecurity authSecurity;

    public CartController(
        CartService cartService,
        AuthSecurity authSecurity
    ) {
        this.cartService = cartService;
        this.authSecurity = authSecurity;
    }

    @PostMapping("/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart(
        @PathVariable Long productId,
        @PathVariable Integer quantity
    ) {
        CartDto cartDto = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartDto>> getAllCart() {
        List<CartDto> cartDtos = cartService.getAllCarts();
        return new ResponseEntity<>(cartDtos, HttpStatus.OK);
    }

    @GetMapping("/user/cart")
    public ResponseEntity<CartDto> getUserCart() {
        CartDto cartDto = cartService.getUserCart(authSecurity.getLoggedInUserId());
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/update/product/{productId}/quantity/{operator}")
    public ResponseEntity<CartDto> updateCartProductQuantity(
        @PathVariable Long productId,
        @PathVariable String operator
    ) {
        Integer quantity = operator.equalsIgnoreCase("delete") ? -1 : 1;
        CartDto cartDto = cartService.updateCartProductQuantity(productId, quantity);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartId}/product/{productId}")
    public ResponseEntity<String> removeProductFromCart(
        @PathVariable Long cartId,
        @PathVariable Long productId
    ) {
        String status = cartService.deleteCartProduct(cartId, productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
