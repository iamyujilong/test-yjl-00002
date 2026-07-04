
package com.example.teacher.recruitment.controller;

import com.example.teacher.recruitment.common.Response;
import com.example.teacher.recruitment.dto.LoginDTO;
import com.example.teacher.recruitment.dto.RegisterDTO;
import com.example.teacher.recruitment.entity.User;
import com.example.teacher.recruitment.service.UserService;
import com.example.teacher.recruitment.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<Response<Map<String, Object>>> register(@RequestBody RegisterDTO dto) {
        User user = userService.register(dto.getIdCard(), dto.getName(), dto.getPhone(), dto.getPassword());
        String token = jwtUtils.generateToken(user.getId(), user.getName(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("token", token);

        return ResponseEntity.ok(Response.success("注册成功", result));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<Map<String, Object>>> login(@RequestBody LoginDTO dto) {
        User user = userService.login(dto.getIdCardOrPhone(), dto.getPassword());
        String token = jwtUtils.generateToken(user.getId(), user.getName(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("token", token);

        return ResponseEntity.ok(Response.success("登录成功", result));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<User>> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getById(userId);
        return ResponseEntity.ok(Response.success(user));
    }
}
