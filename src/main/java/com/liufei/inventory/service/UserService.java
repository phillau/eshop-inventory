package com.liufei.inventory.service;

import com.liufei.inventory.model.User;

public interface UserService {
    User findUserInfo();
    User getCachedUserInfo();
}
