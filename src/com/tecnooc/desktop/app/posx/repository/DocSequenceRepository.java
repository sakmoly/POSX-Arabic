/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.DocSequence;
import com.tecnooc.desktop.app.posx.model.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jomit
 */
@Repository
public interface DocSequenceRepository extends JpaRepository<DocSequence, Integer> {
    public List<DocSequence> findByStoreLogrefAndTerminalCode(Store storeLogref, String terminalCode);
}
