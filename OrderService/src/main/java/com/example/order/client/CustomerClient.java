package com.example.order.client;

import com.example.customer.entity.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CustomerService")
public interface CustomerClient {

    // Corrected to fetch the customer by id
    @GetMapping("/customers/{id}")  // This is correct because we're expecting an id from the URL
    Customer getCustomerById(@PathVariable("id") Long id);
}
