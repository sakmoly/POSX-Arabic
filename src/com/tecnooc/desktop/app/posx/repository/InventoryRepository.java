package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Inventory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jomit
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {    
    @Query("SELECT i FROM Inventory i WHERE "
            + "i.dcsCode      LIKE CONCAT(:text, '%') OR "
            + "i.upc          LIKE CONCAT(:text, '%') OR "
            + "i.alu          LIKE CONCAT(:text, '%') OR "
            + "i.description1 LIKE CONCAT(:text, '%') OR "
            + "i.description2 LIKE CONCAT(:text, '%') OR "
            + "i.description3 LIKE CONCAT(:text, '%') OR "
            + "i.description4 LIKE CONCAT(:text, '%')")
    public List<Inventory> lookup(@Param("text") String text);
    
    @Query("SELECT i FROM Inventory i WHERE "
            + "i.itemSid      LIKE CONCAT(:text, '%') OR "
            + "i.upc          LIKE CONCAT(:text, '%') OR "
            + "i.alu          LIKE CONCAT(:text, '%')")
    public List<Inventory> lookupWeighingItem(@Param("text") String text);
}
