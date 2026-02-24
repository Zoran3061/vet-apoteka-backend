package com.metropolitan.pz.service;

import com.metropolitan.pz.entities.Cart;
import com.metropolitan.pz.entities.Order;

import java.util.List;
import java.util.Optional;


public interface CartService {

    Cart addToCart(Cart cart);

    List<Cart> getCartItemsByUserId(Long userId);

    Optional<Cart> getCartItemById(Long cartId);

    Cart updateCartItem(Long cartId, Cart updatedCart);

    void deleteCartItem(Long cartId);

    void deleteCartItemByUserId(Long userId);

    Order checkout(Long userId);

}

