package com.reflux.store.interfaces;
import com.reflux.store.dto.cart.CartDto;
import jakarta.transaction.Transactional;
import java.util.List;

public interface CartServiceInterface {
    CartDto addProductToCart(Long productId, Integer quantity);
    List<CartDto> getAllCarts();
    CartDto getUserCart(Long userId);

    @Transactional
    CartDto updateCartProductQuantity(Long productId, Integer quantity);

    @Transactional
    String deleteCartProduct(Long cartId, Long productId);
    void updateProductInCarts(Long cartId, Long productId);
}
