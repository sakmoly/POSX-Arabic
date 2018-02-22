/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jomit
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    public List<User> findByUserNameAndPasswordAndActiveTrue(String userName, String password);
}
