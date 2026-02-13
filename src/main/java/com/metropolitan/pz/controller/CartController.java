package com.metropolitan.pz.controller;

import com.metropolitan.pz.entities.Cart;
import com.metropolitan.pz.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
//@Transactional
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Endpoint to add a cart item
    @PostMapping
    public Cart addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }

    // Endpoint to get cart items for a user
    @GetMapping("/{userId}")
    public List<Cart> getCartItems(@PathVariable Long userId) {
        return cartService.getCartItemsByUserId(userId);
    }

    // Endpoint to update a cart item
    @PutMapping("/{cartId}")
    public Cart updateCartItem(@PathVariable Long cartId, @RequestBody Cart cart) {
        return cartService.updateCartItem(cartId, cart);
    }

    // Endpoint to delete a cart item
    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartId) {
        cartService.deleteCartItem(cartId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<?> checkout(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.checkout(userId));
    }

}

