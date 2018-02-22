/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service;

/**
 *
 * @author jomit
 */
public interface InvoiceItemNumberService {
    public void insert(Long invcSid, Long itemSid, Long inventoryNumberSid);
}
