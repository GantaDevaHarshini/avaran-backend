package com.avaran.backend.controller;

import com.avaran.backend.model.Orders;
import com.avaran.backend.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:3000}")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Orders order) {
        if (isBlank(order.getName()) || isBlank(order.getMobile()) || isBlank(order.getAddress())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Name, mobile, and address are required."));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(order));
    }

    @GetMapping
    public List<Orders> getAllOrders() {
        return repo.findAll();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private record ErrorResponse(String message) {
    }
}
