package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.CustomerDto;
import com.tecnooc.desktop.app.posx.model.Customer;
import com.tecnooc.desktop.app.posx.repository.CustomerRepository;
import com.tecnooc.desktop.app.posx.service.CustomerService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jomit
 */
@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository repository;
    
    private List<CustomerDto> toCustomerDto(List<Customer> entities) {
        ArrayList<CustomerDto> customers = new ArrayList<>();
        for (Customer customer : entities) {
            customers.add(new CustomerDto(customer));
        }
        return customers;
    }
    
    @Override
    public List<CustomerDto> lookup(String text) {
        return toCustomerDto(repository.lookup(text));
    }    

    @Override
    public List<CustomerDto> findAll() {
        return toCustomerDto(repository.findAll());
    }

    @Override
    @Transactional(readOnly = false)
    public CustomerDto getDefaultCustomer() {
        Customer customer = repository.findOne(0L);
        if (customer == null) {
            customer = new Customer();
            customer.setCustSid(0L);
            customer.setCustId("0");
            customer.setFirstName("No");
            customer.setLastName("Name");
            customer.setCreatedDate(new Date());
            customer.setModifiedDate(new Date());
            
            customer = repository.save(customer);
        }
        
        return new CustomerDto(customer);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(CustomerDto customer) {
        repository.save(customer.getEntity());
    }
}
