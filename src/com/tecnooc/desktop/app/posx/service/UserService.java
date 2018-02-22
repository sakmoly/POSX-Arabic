package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.dto.UserDto;

/**
 *
 * @author jomit
 */
public interface UserService {
    public UserDto login(String username, String password);
    public UserDto findUser(Integer userId);
}
