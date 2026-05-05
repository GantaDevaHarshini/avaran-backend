package com.avaran.backend.controller;

import com.avaran.backend.model.Orders;
import com.avaran.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository repo;

    // ✅ EXISTING: Save order
    @PostMapping
    public Orders placeOrder(@RequestBody Orders order) {
        return repo.save(order);
    }

    // 🔥 ADD THIS METHOD HERE
    @GetMapping
    public List<Orders> getAllOrders() {
        return repo.findAll();
    }
}