/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.DocSequenceDto;
import com.tecnooc.desktop.app.posx.dto.StoreDto;
import com.tecnooc.desktop.app.posx.dto.TerminalDto;
import com.tecnooc.desktop.app.posx.model.DocSequence;
import com.tecnooc.desktop.app.posx.repository.DocSequenceRepository;
import com.tecnooc.desktop.app.posx.service.DocSequenceService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class DocSequenceServiceImpl implements DocSequenceService {
    @Autowired
    DocSequenceRepository repository;

    @Override
    @Transactional(readOnly = false)
    public DocSequenceDto find(StoreDto store, TerminalDto terminal) {
        List<DocSequence> sequences = repository.findByStoreLogrefAndTerminalCode(store.getEntity(), terminal.getEntity().getTerminalCode());
        DocSequence sequence;
        if (sequences.isEmpty()) {
            sequence = new DocSequence();
            sequence.setStoreLogref(store.getEntity());
            sequence.setTerminalCode(terminal.getEntity().getTerminalCode());
            sequence.setDocSeqNextValue(101);
            
            sequence = repository.save(sequence);
        } else {
            sequence = sequences.get(0);
        }
        
        return new DocSequenceDto(sequence);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(DocSequenceDto docSequenceDto) {
        repository.save(docSequenceDto.getEntity());
    }
}
