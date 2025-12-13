package com.reflux.store.services;
import com.reflux.store.dto.order.OrderDto;
import com.reflux.store.dto.order.OrderItemDto;
import com.reflux.store.exception.ResourceNotFoundException;
import com.reflux.store.interfaces.OrderServiceInterface;
import com.reflux.store.models.Address;
import com.reflux.store.models.Cart;
import com.reflux.store.models.CartItem;
import com.reflux.store.models.Product;
import com.reflux.store.models.order.Order;
import com.reflux.store.models.order.OrderItem;
import com.reflux.store.models.payment.Payment;
import com.reflux.store.repositories.address.AddressRepository;
import com.reflux.store.repositories.cart.CartRepository;
import com.reflux.store.repositories.order.OrderItemRepository;
import com.reflux.store.repositories.order.OrderRepository;
import com.reflux.store.repositories.payment.PaymentRepository;
import com.reflux.store.repositories.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements OrderServiceInterface {
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(CartService cartService, ProductRepository productRepository, ModelMapper modelMapper, OrderRepository orderRepository, CartRepository cartRepository, AddressRepository addressRepository, PaymentRepository paymentRepository, OrderItemRepository orderItemRepository) {
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.paymentRepository = paymentRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public OrderDto placeOrder(String email, String paymentMethod, Long addressId, String gatewayName, String gatewayPaymentId, String gatewayStatus, String gatewayResponseMessage) {
        Cart cart = cartRepository.findCartByEmail(email);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        if(cart.getCartItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        Address address = addressRepository.findById(addressId)
           .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setStatus("ACCEPTED");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, gatewayPaymentId, gatewayStatus, gatewayResponseMessage, gatewayName);

        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);
        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            cartService.deleteCartProduct(cart.getId(), product.getId());
        });

        OrderDto orderDto = modelMapper.map(savedOrder, OrderDto.class);
        orderItems.forEach(item -> {
            orderDto.getOrderItems().add(
                modelMapper.map(item, OrderItemDto.class)
            );
        });

        orderDto.setAddressId(addressId);
        return orderDto;
    }
}
