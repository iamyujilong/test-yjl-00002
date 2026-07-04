
package com.example.teacher.recruitment.service;

import com.example.teacher.recruitment.entity.User;

public interface UserService {

    User register(String idCard, String name, String phone, String password);

    User login(String idCardOrPhone, String password);

    User getById(Long id);

    User getByIdCard(String idCard);

    User getByPhone(String phone);
}
