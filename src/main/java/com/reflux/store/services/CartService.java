package com.reflux.store.services;
import com.reflux.store.dto.cart.CartDto;
import com.reflux.store.dto.product.ProductDto;
import com.reflux.store.exception.ApiException;
import com.reflux.store.exception.ResourceNotFoundException;
import com.reflux.store.interfaces.CartServiceInterface;
import com.reflux.store.models.Cart;
import com.reflux.store.models.CartItem;
import com.reflux.store.models.Product;
import com.reflux.store.repositories.ProductRepository;
import com.reflux.store.repositories.cart.CartItemRepository;
import com.reflux.store.repositories.cart.CartRepository;
import com.reflux.store.security.AuthSecurity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartService implements CartServiceInterface {

    private final AuthSecurity authSecurity;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CartService(
        AuthSecurity authSecurity,
        CartRepository cartRepository,
        CartItemRepository cartItemRepository,
        ProductRepository productRepository,
        ModelMapper modelMapper
    ) {
        this.authSecurity = authSecurity;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDto addProductToCart(Long productId, Integer quantity) {
        Cart cart = cartRepository.findCartByEmail(authSecurity.getLoggedInEmail());
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setTotalPrice(0.00);
            newCart.setUser(authSecurity.getLoggedInUser());
            cart = cartRepository.save(newCart);
        }

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository.findCartItem(cart.getId(), productId);
        if (cartItem != null) {
            throw new ApiException("Product already added to cart");
        }

        if(product.getQuantity() <= 0) {
            throw new ApiException("Product is out of stock");
        }

        if(product.getQuantity() < quantity) {
            throw new ApiException("The product quantity is less than the quantity you want to add");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        newCartItem.setProductPrice(product.getSpecialPrice());
        newCartItem.setDiscount(product.getDiscount());
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity() - quantity);
        double cartTotalPrice = cart.getTotalPrice() + (product.getSpecialPrice() * quantity);
        cart.setTotalPrice(cartTotalPrice);
        cartRepository.save(cart);

        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDto> productStream = cartItems.stream().map(item -> {
            ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
            productDto.setQuantity(item.getQuantity());
            productDto.setDiscount(item.getDiscount());
            return productDto;
        });

        cartDto.setProducts(productStream.collect(Collectors.toList()));
        return cartDto;
    }
}
