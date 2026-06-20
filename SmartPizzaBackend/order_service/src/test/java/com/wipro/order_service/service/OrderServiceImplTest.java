package com.wipro.order_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.wipro.order_service.dto.OrderDTO;
import com.wipro.order_service.entity.Cart;
import com.wipro.order_service.entity.MenuEntity;
import com.wipro.order_service.entity.OrderEntity;
import com.wipro.order_service.repository.CartRepository;
import com.wipro.order_service.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrderServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Cart cart;
    private MenuEntity menu;
    private OrderEntity order;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        menu = new MenuEntity();
        menu.setId(1L);
        menu.setName("Pizza");
        menu.setPrice(100.0);
        menu.setCategory("Medium");

        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(1L);
        cart.setMenu(menu);
        cart.setQuantity(2);

        order = new OrderEntity();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus("PLACED");
        order.setTotalPrice(200.0);
        order.setOrderItems(Arrays.asList());
    }

    @Test
    void testPlaceOrder() {

        when(cartRepository.findByUserId(1L)).thenReturn(Arrays.asList(cart));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> i.getArgument(0));

        OrderDTO dto = orderService.placeOrder(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getUserId());
        assertEquals("PLACED", dto.getStatus());
        assertEquals(200.0, dto.getTotalPrice());

        verify(cartRepository).findByUserId(1L);
        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void testPlaceOrder_EmptyCart() {

