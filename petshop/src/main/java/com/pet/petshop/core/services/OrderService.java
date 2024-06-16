package com.pet.petshop.core.services;

import com.pet.petshop.auth.entity.User;
import com.pet.petshop.auth.repository.UserRepository;
import com.pet.petshop.core.dto.OrderDetailRequest;
import com.pet.petshop.core.dto.OrderDetailResponse;
import com.pet.petshop.core.dto.OrderRequest;
import com.pet.petshop.core.dto.OrderResponse;
import com.pet.petshop.core.entities.Order;
import com.pet.petshop.core.entities.OrderDetail;
import com.pet.petshop.core.entities.Product;
import com.pet.petshop.core.repositories.OrderRepository;
import com.pet.petshop.core.repositories.ProductRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return mapToOrderResponse(order);
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(orderRequest.getOrderDate());
        order.setTotalAmount(orderRequest.getTotalAmount());

        
        List<OrderDetail> orderDetails = orderRequest.getOrderDetails().stream()
                .map(detailRequest -> mapToOrderDetail(detailRequest, order))
                .collect(Collectors.toList());
        order.setOrderDetails(orderDetails);

        
        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

   @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        order.setUser(user);
        order.setOrderDate(orderRequest.getOrderDate());
        order.setTotalAmount(orderRequest.getTotalAmount());

        order.getOrderDetails().clear();
        List<OrderDetail> orderDetails = orderRequest.getOrderDetails().stream()
                .map(detailRequest -> {
                    Product product = productRepository.findById(detailRequest.getProductId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                    return OrderDetail.builder()
                            .order(order)
                            .product(product)
                            .quantity(detailRequest.getQuantity())
                            .price(detailRequest.getPrice())
                            .build();
                }).collect(Collectors.toList());
        order.getOrderDetails().addAll(orderDetails);

        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);
    }

    public String deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return "Orden con ID " + id + " eliminada correctamente";
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .orderDetails(order.getOrderDetails().stream()
                        .map(this::mapToOrderDetailResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderDetail mapToOrderDetail(OrderDetailRequest detailRequest, Order order) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(productRepository.findById(detailRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado")));
        orderDetail.setQuantity(detailRequest.getQuantity());
        orderDetail.setPrice(detailRequest.getPrice());
        return orderDetail;
    }

    private OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .productId(orderDetail.getProduct().getId())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .build();
    }

    @SuppressWarnings("unused")
private Order mapToOrder(OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<OrderDetail> orderDetails = orderRequest.getOrderDetails().stream()
                .map(detailRequest -> {
                    Product product = productRepository.findById(detailRequest.getProductId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                    return OrderDetail.builder()
                            .product(product)
                            .quantity(detailRequest.getQuantity())
                            .price(detailRequest.getPrice())
                            .build();
                }).collect(Collectors.toList());

        return Order.builder()
                .user(user)
                .orderDate(orderRequest.getOrderDate())
                .totalAmount(orderRequest.getTotalAmount())
                .orderDetails(orderDetails)
                .build();
    }
}
