package com.wipro.order_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.wipro.order_service.dto.CartDTO;
import com.wipro.order_service.entity.Cart;
import com.wipro.order_service.entity.MenuEntity;
import com.wipro.order_service.repository.CartRepository;
import com.wipro.order_service.repository.MenuRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private MenuEntity menu;
    private Cart cart;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        menu = new MenuEntity();
        menu.setId(1L);
        menu.setName("Cheese Pizza");
        menu.setPrice(200.0);
        menu.setCategory("Medium");

        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(1L);
        cart.setMenu(menu);
        cart.setQuantity(2);
    }

    //  TEST: addToCart
    @Test
    void testAddToCart() {

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDTO dto = cartService.addToCart(1L, 1L, 2);

        assertNotNull(dto);
        assertEquals(1L, dto.getUserId());
        assertEquals(2, dto.getQuantity());
        assertEquals("Cheese Pizza", dto.getMenu().getName());

        verify(menuRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    //  TEST: getCartByUser
    @Test
    void testGetCartByUser() {

        when(cartRepository.findByUserId(1L)).thenReturn(Arrays.asList(cart));

        List<CartDTO> list = cartService.getCartByUser(1L);

        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getUserId());

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    //  TEST: updateCart
    @Test
    void testUpdateCart() {

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDTO dto = cartService.updateCart(1L, 5);

        assertEquals(5, dto.getQuantity());

        verify(cartRepository).findById(1L);
        verify(cartRepository).save(cart);
    }

    //  TEST: deleteCartItem
    @Test
    void testDeleteCartItem() {

        doNothing().when(cartRepository).deleteById(1L);

        cartService.deleteCartItem(1L);

        verify(cartRepository, times(1)).deleteById(1L);
    }

    // ✅ TEST: clearCart
    @Test
    void testClearCart() {

        doNothing().when(cartRepository).deleteByUserId(1L);

        cartService.clearCart(1L);

        verify(cartRepository, times(1)).deleteByUserId(1L);
    }

    //  TEST: addToCart - Menu Not Found
    @Test
    void testAddToCart_MenuNotFound() {

        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(1L, 1L, 2);
        });

        assertEquals("Menu not found", exception.getMessage());

        verify(menuRepository).findById(1L);
        verify(cartRepository, never()).save(any());
    }
}