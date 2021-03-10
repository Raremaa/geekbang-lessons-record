package com.masaiqi.geekbang.web.projects.user.repository;

import com.masaiqi.geekbang.web.projects.user.domain.User;

import java.util.Collection;

/**
 * @author masaiqi
 * @date 2021/3/1 9:20 PM
 */
public interface UserRepository {

    boolean save(User user);

    boolean deleteById(Long userId);

    boolean update(User user);

    User getById(Long userId);

    User getByNameAndPassword(String userName, String password);

    Collection<User> getAll();

}
