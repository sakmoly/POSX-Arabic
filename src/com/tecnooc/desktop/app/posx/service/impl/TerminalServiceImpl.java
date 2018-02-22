package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.TerminalDto;
import com.tecnooc.desktop.app.posx.model.Terminal;
import com.tecnooc.desktop.app.posx.repository.TerminalRepository;
import com.tecnooc.desktop.app.posx.service.TerminalService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jomit
 */
@Service
@Transactional(readOnly = true)
public class TerminalServiceImpl implements TerminalService {   
    @Autowired
    private TerminalRepository repository;

    @Override
    public TerminalDto getTerminal() {
        List<Terminal> terminals = repository.findAll();
        return terminals.isEmpty()? null: new TerminalDto(terminals.get(0));
    }    
}
