
package com.example.teacher.recruitment.service.impl;

import com.example.teacher.recruitment.entity.*;
import com.example.teacher.recruitment.mapper.*;
import com.example.teacher.recruitment.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final RegistrationMapper registrationMapper;
    private final CertificateItemMapper certificateItemMapper;
    private final CertificateFileMapper certificateFileMapper;
    private final ReviewLogMapper reviewLogMapper;
    private final ExamMapper examMapper;
    private final UserMapper userMapper;

    public ReviewServiceImpl(RegistrationMapper registrationMapper,
                             CertificateItemMapper certificateItemMapper,
                             CertificateFileMapper certificateFileMapper,
                             ReviewLogMapper reviewLogMapper,
                             ExamMapper examMapper,
                             UserMapper userMapper) {
        this.registrationMapper = registrationMapper;
        this.certificateItemMapper = certificateItemMapper;
        this.certificateFileMapper = certificateFileMapper;
        this.reviewLogMapper = reviewLogMapper;
        this.examMapper = examMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void review(Long registrationId, Long reviewerId, Integer status, String remark) {
        Registration registration = registrationMapper.selectById(registrationId);
        if (registration == null) {
            throw new RuntimeException("报名记录不存在");
        }

        registration.setStatus(status);
        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.updateById(registration);

        ReviewLog reviewLog = new ReviewLog();
        reviewLog.setRegistrationId(registrationId);
        reviewLog.setReviewerId(reviewerId);
        reviewLog.setReviewStatus(status);
        reviewLog.setRemark(remark);
        reviewLog.setReviewTime(LocalDateTime.now());
        reviewLogMapper.insert(reviewLog);
    }

    @Override
    public List<ReviewLog> getReviewLogs(Long registrationId) {
        return reviewLogMapper.selectByRegistrationId(registrationId);
    }

    @Override
    public Map<String, Object> getRegistrationDetail(Long registrationId) {
        Map<String, Object> detail = new HashMap<>();

        Registration registration = registrationMapper.selectById(registrationId);
        if (registration == null) {
            return detail;
        }

        detail.put("registration", registration);

        Exam exam = examMapper.selectById(registration.getExamId());
        detail.put("exam", exam);

        User user = userMapper.selectById(registration.getUserId());
        detail.put("user", user);

        List<CertificateItem> items = certificateItemMapper.selectByRegistrationId(registrationId);
        List<Map<String, Object>> certificateList = new java.util.ArrayList<>();
        for (CertificateItem item : items) {
            List<CertificateFile> files = certificateFileMapper.selectByItemId(item.getId());
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("registrationId", item.getRegistrationId());
            itemMap.put("type", item.getType());
            itemMap.put("name", item.getName());
            itemMap.put("level", item.getLevel());
            itemMap.put("description", item.getDescription());
            itemMap.put("createTime", item.getCreateTime());
            itemMap.put("files", files);
            certificateList.add(itemMap);
        }
        detail.put("certificates", certificateList);

        List<ReviewLog> logs = reviewLogMapper.selectByRegistrationId(registrationId);
        detail.put("reviewLogs", logs);

        return detail;
    }
}
