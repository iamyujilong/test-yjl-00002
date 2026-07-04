
package com.example.teacher.recruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.teacher.recruitment.entity.Registration;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RegistrationService {

    Registration create(Registration registration);

    Registration update(Registration registration);

    void submit(Long id);

    void delete(Long id);

    Registration getById(Long id);

    Registration getByUserIdAndExamId(Long userId, Long examId);

    List<Registration> listByUserId(Long userId);

    List<Registration> listByExamId(Long examId);

    IPage<Registration> page(Page<Registration> page, Map<String, Object> filters);

    IPage<Registration> pageByStatus(Page<Registration> page, Integer status);
}
