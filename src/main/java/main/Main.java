package main;

import entity.Customer;
import service.CustomerServiceImpl;


import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        CustomerServiceImpl customerService = new CustomerServiceImpl();

        // ایجاد یه مشتری
        Customer customer = new Customer();
        customer.setFirstname("Sara");
        customer.setLastname("Ahmadi");
        customer.setNationalID("9876543210");
        customerService.create(customer);

    }
}