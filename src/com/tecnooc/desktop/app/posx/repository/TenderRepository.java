/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Tender;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jomit
 */

@Repository
public interface TenderRepository extends JpaRepository<Tender, Integer> {
    public List<Tender> findByActiveTrue();
}
