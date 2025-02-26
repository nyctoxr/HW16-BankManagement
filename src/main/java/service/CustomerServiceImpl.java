package service;

import base.BaseServiceImpl;
import entity.Customer;

public class CustomerServiceImpl extends BaseServiceImpl<Customer> {
    public CustomerServiceImpl() {
        super(Customer.class);
    }
}

