package com.reflux.store.repositories.cart;
import com.reflux.store.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT cart FROM Cart cart WHERE cart.user.email = ?1")
    Cart findCartByEmail(String email);
    Optional<Cart> findFirstByUserId(Long userId);

    @Query("SELECT cart FROM Cart cart JOIN FETCH cart.cartItems items JOIN FETCH items.product product WHERE product.id = ?1")
    List<Cart> findCartsByProductId(Long productId);
}
