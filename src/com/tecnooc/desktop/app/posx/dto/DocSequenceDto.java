package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.DocSequence;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author jomit
 */
public class DocSequenceDto extends Dto<DocSequence> {
    public DocSequenceDto(DocSequence entity) {
        super(entity);
    }
    
    public Integer nextValue() {
        return entity.getDocSeqNextValue();
    }
    
    public void increment() {
        int currentValue = entity.getDocSeqNextValue();
        currentValue++;
        
        entity.setDocSeqNextValue(currentValue);
    }
}
