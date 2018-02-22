package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Zout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jomit
 */
@Repository
public interface ZoutRepository extends JpaRepository<Zout, Long> {
    @Query("SELECT z FROM Zout z WHERE z.zoutSid IN (SELECT MAX(z.zoutSid) FROM Zout z)")
    public Zout findLastReport();
}
