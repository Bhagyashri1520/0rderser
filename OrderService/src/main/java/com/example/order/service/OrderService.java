package com.example.order.service;

import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.customer.entity.Customer;
import com.example.order.client.CustomerClient;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerClient customerClient;

    // Create order and check customer details via Feign client
    public Order createOrder(Order order) {
        try {
            // Fetch the customer by ID
            Customer customer = customerClient.getCustomerById(order.getCustomerId());  // Use customerId, not customerName

            if (customer == null) {
                throw new RuntimeException("Customer not found!");
            }

            // Set the customer name in the order
            order.setCustomerName(customer.getName());

            // Save the order after confirming customer details
            return orderRepository.save(order);

        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Customer not found with ID: " + order.getCustomerId());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating the order: " + e.getMessage());
        }
    }


    // Get order by ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Update existing order
    public Order updateOrder(Long id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    // Delete order by ID
    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }
}
