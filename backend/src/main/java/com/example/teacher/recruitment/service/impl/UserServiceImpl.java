
package com.example.teacher.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.teacher.recruitment.entity.User;
import com.example.teacher.recruitment.mapper.UserMapper;
import com.example.teacher.recruitment.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public User register(String idCard, String name, String phone, String password) {
        if (getByIdCard(idCard) != null) {
            throw new RuntimeException("该身份证号已注册");
        }
        if (getByPhone(phone) != null) {
            throw new RuntimeException("该手机号已注册");
        }

        User user = new User();
        user.setIdCard(idCard);
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(0);
        user.setStatus(1);

        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String idCardOrPhone, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getIdCard, idCardOrPhone).or().eq(User::getPhone, idCardOrPhone);
        User user = userMapper.selectOne(wrapper);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        return user;
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getByIdCard(String idCard) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getIdCard, idCard);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(wrapper);
    }
}
