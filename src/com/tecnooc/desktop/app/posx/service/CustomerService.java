package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.dto.CustomerDto;
import java.util.List;

/**
 *
 * @author jomit
 */
public interface CustomerService {
    public void save(CustomerDto customer);
    public List<CustomerDto> lookup(String text);    
    public List<CustomerDto> findAll();
    public CustomerDto getDefaultCustomer();
}
