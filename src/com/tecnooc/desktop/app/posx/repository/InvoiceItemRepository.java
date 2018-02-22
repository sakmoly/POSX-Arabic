/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Invoice;
import com.tecnooc.desktop.app.posx.model.InvoiceItem;
import com.tecnooc.desktop.app.posx.model.InvoiceItemPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jomit
 */
@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, InvoiceItemPK> {
    public List<InvoiceItem> findByInvoice(Invoice invoice);
}
