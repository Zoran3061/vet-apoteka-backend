package com.metropolitan.pz;

import com.metropolitan.pz.controller.CartController;
import com.metropolitan.pz.entities.Cart;
import com.metropolitan.pz.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddToCart() {
        Cart cart = new Cart();
        when(cartService.addToCart(cart)).thenReturn(cart);

        Cart result = cartController.addToCart(cart);

        assertEquals(cart, result);
        verify(cartService, times(1)).addToCart(cart);
    }

    @Test
    public void testGetCartItems() {
        Long userId = 1L;
        List<Cart> cartItems = new ArrayList<>();
        when(cartService.getCartItemsByUserId(userId)).thenReturn(cartItems);

        List<Cart> result = cartController.getCartItems(userId);

        assertEquals(cartItems, result);
        verify(cartService, times(1)).getCartItemsByUserId(userId);
    }

    @Test
    public void testUpdateCartItem() {
        Long cartId = 1L;
        Cart cart = new Cart();
        when(cartService.updateCartItem(cartId, cart)).thenReturn(cart);

        Cart result = cartController.updateCartItem(cartId, cart);

        assertEquals(cart, result);
        verify(cartService, times(1)).updateCartItem(cartId, cart);
    }

    @Test
    public void testDeleteCartItem() {
        Long cartId = 1L;
        ResponseEntity<?> expectedResponse = ResponseEntity.ok().build();
        doNothing().when(cartService).deleteCartItem(cartId);

        ResponseEntity<?> result = cartController.deleteCartItem(cartId);

        assertEquals(expectedResponse, result);
        verify(cartService, times(1)).deleteCartItem(cartId);
    }

    @Test
    public void testDeleteCartItemByUserId() {
        Long userId = 1L;
        ResponseEntity<?> expectedResponse = ResponseEntity.ok().build();
        doNothing().when(cartService).deleteCartItemByUserId(userId);

        ResponseEntity<?> result = cartController.deleteCartItem(userId);

        assertEquals(expectedResponse, result);
        verify(cartService, times(1)).deleteCartItemByUserId(userId);
    }
}

