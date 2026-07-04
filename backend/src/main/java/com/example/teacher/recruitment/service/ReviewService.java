
package com.example.teacher.recruitment.service;

import com.example.teacher.recruitment.entity.ReviewLog;

import java.util.List;
import java.util.Map;

public interface ReviewService {

    void review(Long registrationId, Long reviewerId, Integer status, String remark);

    List<ReviewLog> getReviewLogs(Long registrationId);

    Map<String, Object> getRegistrationDetail(Long registrationId);
}
