package com.reflux.store.repositories.cart;
import com.reflux.store.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT item FROM CartItem item WHERE item.cart.id = ?1 AND item.product.id = ?2")
    CartItem findCartItem(Long cartId, Long productId);

    @Modifying
    @Query("DELETE FROM CartItem item WHERE item.cart.id = ?1 AND item.product.id = ?2")
    void deleteCartItem(Long cartId, Long productId);
}
