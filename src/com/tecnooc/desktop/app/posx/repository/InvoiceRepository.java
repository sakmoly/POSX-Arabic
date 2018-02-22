/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Invoice;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jomit
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    public List<Invoice> findByHeldTrue();
    public List<Invoice> findByHeldFalseAndInvcSidBetween(Long start, Long end);
    public List<Invoice> findByInvcSidGreaterThanAndInvcSidLessThan(Long start, Long end);
}
