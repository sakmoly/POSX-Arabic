package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jomit
 */
public interface TerminalRepository extends JpaRepository<Terminal, Integer> {
    
}
