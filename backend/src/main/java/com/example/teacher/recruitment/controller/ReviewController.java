
package com.example.teacher.recruitment.controller;

import com.example.teacher.recruitment.common.Response;
import com.example.teacher.recruitment.entity.ReviewLog;
import com.example.teacher.recruitment.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{registrationId}")
    public ResponseEntity<Response<Void>> review(
            @PathVariable Long registrationId,
            @RequestParam Long reviewerId,
            @RequestParam Integer status,
            @RequestParam(required = false) String remark) {
        reviewService.review(registrationId, reviewerId, status, remark);
        return ResponseEntity.ok(Response.success("审核完成", null));
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<Response<List<ReviewLog>>> getReviewLogs(@PathVariable Long registrationId) {
        List<ReviewLog> logs = reviewService.getReviewLogs(registrationId);
        return ResponseEntity.ok(Response.success(logs));
    }

    @GetMapping("/detail/{registrationId}")
    public ResponseEntity<Response<Map<String, Object>>> getRegistrationDetail(@PathVariable Long registrationId) {
        Map<String, Object> detail = reviewService.getRegistrationDetail(registrationId);
        return ResponseEntity.ok(Response.success(detail));
    }
}
