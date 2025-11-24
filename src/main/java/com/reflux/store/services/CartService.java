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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;

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

        cartDto.setProducts(productStream.collect(toList()));
        return cartDto;
    }

    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()) {
            throw new ApiException("No cart found");
        }

        return carts
            .stream()
            .map(cart -> {
                CartDto cartDto = modelMapper.map(cart, CartDto.class);
                List<ProductDto> products = cart.getCartItems().stream().map(product ->
                    modelMapper.map(product.getProduct(), ProductDto.class)).toList();
                cartDto.setProducts(products);
                return cartDto;
            })
            .collect(toList());
    }

    @Override
    public CartDto getUserCart(Long userId) {
        Cart userCart = cartRepository.findFirstByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User cart not found"));

        CartDto cartDto = modelMapper.map(userCart, CartDto.class);
        userCart.getCartItems().forEach(cartItem -> cartItem.getProduct().setQuantity(cartItem.getQuantity()));
        List<ProductDto> products = userCart.getCartItems()
            .stream()
            .map(product ->
                modelMapper.map(product.getProduct(), ProductDto.class)
            ).toList();

        cartDto.setProducts(products);
        return cartDto;
    }

    @Override
    @Transactional
    public CartDto updateCartProductQuantity(Long productId, Integer quantity) {
        Cart cart = cartRepository.findCartByEmail(authSecurity.getLoggedInEmail());
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if(product.getQuantity() <= 0) {
            throw new ApiException("Product is out of stock");
        }

        if(product.getQuantity() < quantity) {
            throw new ApiException("The product quantity exceeds available stock");
        }

        Long cartId = cart.getId();
        CartItem cartItem = cartItemRepository.findCartItem(cartId, productId);
        if (cartItem == null) {
            throw new ApiException("Product is not available in the cart");
        }

        int newQuantity = cartItem.getQuantity() + quantity;
        if(newQuantity < 0) {
            throw new ApiException("Invalid product quantity. Try again");
        }

        if(newQuantity == 0) {
            this.deleteCartProduct(cartId, productId);
        }else {
            cartItem.setDiscount(product.getDiscount());
            cartItem.setSpecialPrice(product.getSpecialPrice());
            cartItem.setQuantity(quantity);
            cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
            cartRepository.save(cart);
        }

        cartItemRepository.save(cartItem);
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDto> itemProducts = cartItems.stream().map(item -> {
            ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
            productDto.setQuantity(item.getQuantity());
            return productDto;
        });

        cartDto.setProducts(itemProducts.collect(toList()));
        return cartDto;
    }

    @Override
    @Transactional
    public String deleteCartProduct(
        @PathVariable Long cartId,
        @PathVariable Long productId
    ) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findCartItem(cart.getId(), productId);
        if (cartItem == null) {
            throw new ApiException("Invalid cart item.");
        }

        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getSpecialPrice() * cartItem.getQuantity()));
        cartItemRepository.deleteCartItem(cartId, productId);
        return "Product removed successfully";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findCartByEmail(authSecurity.getLoggedInEmail());
        if(cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        Product product = productRepository.findById(productId)
           .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        CartItem cartItem = cartItemRepository.findCartItem(cart.getId(), productId);
        if(cartItem == null) {
            throw new ResourceNotFoundException("Cart item not found");
        }

        double cartTotalPrice = cart.getTotalPrice() - (cartItem.getSpecialPrice() * cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartTotalPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItemRepository.save(cartItem);

    }

}
