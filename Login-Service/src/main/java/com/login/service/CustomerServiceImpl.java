package com.login.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.model.CustomerDetails;
import com.login.model.User;
import com.login.repository.CustomerDeatilsRepository;


@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private CustomerDeatilsRepository customerDeatilsRepository;
	 
	CustomerDetails customerDetails=null;
	
	public CustomerDetails saveCustomer(CustomerDetails customer) {
		customerDetails = customerDeatilsRepository.findByAadharNumber(customer.getAadharNumber());
		if (customerDetails == null) {
			customer.setVerificationStatus(false);
			customerDeatilsRepository.save(customer);
			return customer;
		}else {
			return customerDetails;
		}
	}

	
}
