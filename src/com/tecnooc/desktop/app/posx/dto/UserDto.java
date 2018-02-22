package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.User;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class UserDto extends Dto<User> {
    private final StringProperty nameProperty;

    public UserDto(User entity) {
        super(entity);
        
        this.nameProperty = new SimpleStringProperty(entity, "userName");
    }
    
    public ReadOnlyStringProperty userNameProperty() {
        return nameProperty;
    }

    @Override
    public String toString() {
        return entity.getUserName();
    }
}
