package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.ProductService;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestHeader("Authorization") String tokenHeader) {

        // Extract the token from the Bearer prefix
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            tokenHeader = tokenHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Validate the token
        if (!jwtUtil.isTokenValid(tokenHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract user_name or roles if needed
        String username = jwtUtil.extractUsername(tokenHeader);

        // You can perform additional checks like role validation here
        // Example: Check if the user has the required role
        Set<String> roles = jwtUtil.extractRoles(tokenHeader);
         if (!roles.contains("ROLE_USER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
         }
        // Fetch and return product details
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}

