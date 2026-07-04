
package com.example.teacher.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.teacher.recruitment.entity.Exam;
import com.example.teacher.recruitment.entity.Registration;
import com.example.teacher.recruitment.mapper.ExamMapper;
import com.example.teacher.recruitment.mapper.RegistrationMapper;
import com.example.teacher.recruitment.service.RegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMapper registrationMapper;
    private final ExamMapper examMapper;

    public RegistrationServiceImpl(RegistrationMapper registrationMapper, ExamMapper examMapper) {
        this.registrationMapper = registrationMapper;
        this.examMapper = examMapper;
    }

    @Override
    @Transactional
    public Registration create(Registration registration) {
        Exam exam = examMapper.selectById(registration.getExamId());
        if (exam == null || exam.getStatus() != 1) {
            throw new RuntimeException("考试不存在或未开放报名");
        }

        if (getByUserIdAndExamId(registration.getUserId(), registration.getExamId()) != null) {
            throw new RuntimeException("您已报名该考试");
        }

        registration.setStatus(0);
        registration.setTeacherType(exam.getTeacherType());
        registration.setCreateTime(LocalDateTime.now());
        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.insert(registration);
        return registration;
    }

    @Override
    @Transactional
    public Registration update(Registration registration) {
        Registration existing = registrationMapper.selectById(registration.getId());
        if (existing == null) {
            throw new RuntimeException("报名记录不存在");
        }
        if (existing.getStatus() >= 1) {
            throw new RuntimeException("报名已提交，无法修改");
        }

        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.updateById(registration);
        return registration;
    }

    @Override
    @Transactional
    public void submit(Long id) {
        Registration registration = registrationMapper.selectById(id);
        if (registration == null) {
            throw new RuntimeException("报名记录不存在");
        }
        if (registration.getStatus() >= 1) {
            throw new RuntimeException("报名已提交");
        }

        registration.setStatus(1);
        registration.setSubmitTime(LocalDateTime.now());
        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.updateById(registration);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Registration registration = registrationMapper.selectById(id);
        if (registration != null && registration.getStatus() >= 1) {
            throw new RuntimeException("报名已提交，无法删除");
        }
        registrationMapper.deleteById(id);
    }

    @Override
    public Registration getById(Long id) {
        return registrationMapper.selectById(id);
    }

    @Override
    public Registration getByUserIdAndExamId(Long userId, Long examId) {
        LambdaQueryWrapper<Registration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Registration::getUserId, userId)
                .eq(Registration::getExamId, examId);
        return registrationMapper.selectOne(wrapper);
    }

    @Override
    public List<Registration> listByUserId(Long userId) {
        return registrationMapper.selectByUserId(userId);
    }

    @Override
    public List<Registration> listByExamId(Long examId) {
        return registrationMapper.selectByExamId(examId);
    }

    @Override
    public IPage<Registration> page(Page<Registration> page, Map<String, Object> filters) {
        LambdaQueryWrapper<Registration> wrapper = new LambdaQueryWrapper<>();

        if (filters.containsKey("status")) {
            wrapper.eq(Registration::getStatus, filters.get("status"));
        }
        if (filters.containsKey("teacherType")) {
            wrapper.eq(Registration::getTeacherType, filters.get("teacherType"));
        }
        if (filters.containsKey("examId")) {
            wrapper.eq(Registration::getExamId, filters.get("examId"));
        }
        if (filters.containsKey("title")) {
            wrapper.like(Registration::getTitle, filters.get("title"));
        }
        if (filters.containsKey("minAge")) {
            int minAge = Integer.parseInt(filters.get("minAge").toString());
            LocalDate maxBirthDate = LocalDate.now().minusYears(minAge);
            wrapper.ge(Registration::getBirthDate, maxBirthDate);
        }
        if (filters.containsKey("maxAge")) {
            int maxAge = Integer.parseInt(filters.get("maxAge").toString());
            LocalDate minBirthDate = LocalDate.now().minusYears(maxAge);
            wrapper.le(Registration::getBirthDate, minBirthDate);
        }
        if (filters.containsKey("teachCertificateLevel")) {
            wrapper.eq(Registration::getTeachCertificateLevel, filters.get("teachCertificateLevel"));
        }

        wrapper.orderByDesc(Registration::getSubmitTime);
        return registrationMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Registration> pageByStatus(Page<Registration> page, Integer status) {
        LambdaQueryWrapper<Registration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Registration::getStatus, status);
        wrapper.orderByDesc(Registration::getSubmitTime);
        return registrationMapper.selectPage(page, wrapper);
    }
}
