package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.dto.DocSequenceDto;
import com.tecnooc.desktop.app.posx.dto.StoreDto;
import com.tecnooc.desktop.app.posx.dto.TerminalDto;

/**
 *
 * @author jomit
 */
public interface DocSequenceService {
    public DocSequenceDto find(StoreDto store, TerminalDto terminal);
    public void update(DocSequenceDto docSequenceDto);
}
