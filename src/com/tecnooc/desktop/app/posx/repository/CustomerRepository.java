package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Customer;
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
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.active=true AND ("
            + "CONCAT(c.firstName, ' ', c.lastName)   LIKE CONCAT(:text, '%') OR "
            + "c.firstName  LIKE CONCAT(:text, '%') OR "
            + "c.lastName   LIKE CONCAT(:text, '%') OR "
            + "c.mob        LIKE CONCAT(:text, '%') OR "
            + "c.telephone1 LIKE CONCAT(:text, '%') OR "
            + "c.telephone2 LIKE CONCAT(:text, '%') OR "
            + "c.emailAddr  LIKE CONCAT(:text, '%'))")
    public List<Customer> lookup(@Param("text") String text);
}
