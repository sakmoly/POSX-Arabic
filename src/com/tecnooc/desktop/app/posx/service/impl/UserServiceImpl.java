package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.UserDto;
import com.tecnooc.desktop.app.posx.model.User;
import com.tecnooc.desktop.app.posx.repository.UserRepository;
import com.tecnooc.desktop.app.posx.service.UserService;
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
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;
    
    @Override
    public UserDto login(String username, String password) {
        List<User> users = repository.findByUserNameAndPasswordAndActiveTrue(username, password);
        return users.isEmpty() ? null: new UserDto(users.get(0));
    }

    @Override
    public UserDto findUser(Integer userId) {
        User user = repository.findOne(userId);
        return user == null ? null : new UserDto(user);
    }
}
