/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.InvoiceItemNumber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jomit
 */
public interface InvoiceItemNumberRepository extends JpaRepository<InvoiceItemNumber, Long> {
    
}
