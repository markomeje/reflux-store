package com.reflux.store.repositories.cart;
import com.reflux.store.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT cart FROM Cart cart WHERE cart.user.email = ?1")
    Cart findCartByEmail(String email);
}
