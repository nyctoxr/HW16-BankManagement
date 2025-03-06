package main;

import service.*;

public class Main {
    public static void main(String[] args) {
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        CardServiceImpl cardService = new CardServiceImpl();
        EmployeeServiceImpl employeeService = new EmployeeServiceImpl();
        AccountServiceImpl accountService = new AccountServiceImpl();
        BankBranchServiceImpl bankBranchService = new BankBranchServiceImpl();


    }
}