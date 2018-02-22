/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Inventory;
import com.tecnooc.desktop.app.posx.model.InventoryNumber;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jomit
 */
public interface InventoryNumberRepository extends JpaRepository<InventoryNumber, Long> {
    public List<InventoryNumber> findBySerialLotNumberAndActiveTrue(String number);
    public List<InventoryNumber> findByInventoryAndActiveTrue(Inventory inventory);
}
