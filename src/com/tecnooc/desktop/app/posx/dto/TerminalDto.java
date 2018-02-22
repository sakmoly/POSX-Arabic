package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Terminal;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class TerminalDto extends Dto<Terminal> {
    private final StringProperty nameProperty;

    public TerminalDto(Terminal entity) {
        super(entity);
        
        this.nameProperty = new SimpleStringProperty(entity.getTerminalName());
    }
    
    public ReadOnlyStringProperty terminalNameProperty() {
        return nameProperty;
    }
    
    public String getTerminalName() {
        return nameProperty.get();
    }

    @Override
    public String toString() {
        return entity.getTerminalName();
    }
}
