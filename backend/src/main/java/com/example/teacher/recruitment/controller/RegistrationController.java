
package com.example.teacher.recruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.teacher.recruitment.common.Response;
import com.example.teacher.recruitment.entity.Registration;
import com.example.teacher.recruitment.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Response<Registration>> create(@RequestBody Registration registration) {
        Registration created = registrationService.create(registration);
        return ResponseEntity.ok(Response.success("报名成功", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Registration>> update(@PathVariable Long id, @RequestBody Registration registration) {
        registration.setId(id);
        Registration updated = registrationService.update(registration);
        return ResponseEntity.ok(Response.success("更新成功", updated));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Response<Void>> submit(@PathVariable Long id) {
        registrationService.submit(id);
        return ResponseEntity.ok(Response.success("提交成功", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        registrationService.delete(id);
        return ResponseEntity.ok(Response.success("删除成功", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Registration>> getById(@PathVariable Long id) {
        Registration registration = registrationService.getById(id);
        return ResponseEntity.ok(Response.success(registration));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<List<Registration>>> listByUserId(@PathVariable Long userId) {
        List<Registration> registrations = registrationService.listByUserId(userId);
        return ResponseEntity.ok(Response.success(registrations));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<Response<List<Registration>>> listByExamId(@PathVariable Long examId) {
        List<Registration> registrations = registrationService.listByExamId(examId);
        return ResponseEntity.ok(Response.success(registrations));
    }

    @GetMapping("/page")
    public ResponseEntity<Response<IPage<Registration>>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer teacherType,
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String teachCertificateLevel) {

        Map<String, Object> filters = new HashMap<>();
        if (status != null) filters.put("status", status);
        if (teacherType != null) filters.put("teacherType", teacherType);
        if (examId != null) filters.put("examId", examId);
        if (title != null) filters.put("title", title);
        if (minAge != null) filters.put("minAge", minAge);
        if (maxAge != null) filters.put("maxAge", maxAge);
        if (teachCertificateLevel != null) filters.put("teachCertificateLevel", teachCertificateLevel);

        IPage<Registration> page = registrationService.page(new Page<>(pageNum, pageSize), filters);
        return ResponseEntity.ok(Response.success(page));
    }

    @GetMapping("/page/status/{status}")
    public ResponseEntity<Response<IPage<Registration>>> pageByStatus(
            @PathVariable Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Registration> page = registrationService.pageByStatus(new Page<>(pageNum, pageSize), status);
        return ResponseEntity.ok(Response.success(page));
    }

    @GetMapping("/user/{userId}/exam/{examId}")
    public ResponseEntity<Response<Registration>> getByUserIdAndExamId(
            @PathVariable Long userId,
            @PathVariable Long examId) {
        Registration registration = registrationService.getByUserIdAndExamId(userId, examId);
        return ResponseEntity.ok(Response.success(registration));
    }
}
