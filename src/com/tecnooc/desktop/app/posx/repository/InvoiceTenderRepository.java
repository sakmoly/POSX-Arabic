/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Invoice;
import com.tecnooc.desktop.app.posx.model.InvoiceTender;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jomit
 */
@Repository
public interface InvoiceTenderRepository extends JpaRepository<InvoiceTender, Integer> {
    public List<InvoiceTender> findByInvcSid(Invoice invoice);
}
