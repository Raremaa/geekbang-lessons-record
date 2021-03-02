package com.masaiqi.geekbang.web.projects.user.service.impl;

import com.masaiqi.geekbang.web.projects.user.domain.User;
import com.masaiqi.geekbang.web.projects.user.repository.DatabaseUserRepository;
import com.masaiqi.geekbang.web.projects.user.repository.UserRepository;
import com.masaiqi.geekbang.web.projects.user.service.UserService;

/**
 * @author masaiqi
 * @date 2021/3/2 2:02 PM
 */
public class IUserService implements UserService {

    private UserRepository userRepository = new DatabaseUserRepository();

    @Override
    public boolean register(User user) {
        // 省略查重......
        return userRepository.save(user);
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return userRepository.getByNameAndPassword(name, password);
    }
}
