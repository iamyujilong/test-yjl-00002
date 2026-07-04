
package com.example.teacher.recruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.teacher.recruitment.common.Response;
import com.example.teacher.recruitment.entity.Exam;
import com.example.teacher.recruitment.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<Response<Exam>> create(@RequestBody Exam exam) {
        Exam created = examService.create(exam);
        return ResponseEntity.ok(Response.success("创建成功", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Exam>> update(@PathVariable Long id, @RequestBody Exam exam) {
        exam.setId(id);
        Exam updated = examService.update(exam);
        return ResponseEntity.ok(Response.success("更新成功", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        examService.delete(id);
        return ResponseEntity.ok(Response.success("删除成功", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Exam>> getById(@PathVariable Long id) {
        Exam exam = examService.getById(id);
        return ResponseEntity.ok(Response.success(exam));
    }

    @GetMapping
    public ResponseEntity<Response<List<Exam>>> listAll() {
        List<Exam> exams = examService.listAll();
        return ResponseEntity.ok(Response.success(exams));
    }

    @GetMapping("/open")
    public ResponseEntity<Response<List<Exam>>> listOpen() {
        List<Exam> exams = examService.listOpen();
        return ResponseEntity.ok(Response.success(exams));
    }

    @GetMapping("/page")
    public ResponseEntity<Response<IPage<Exam>>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Exam> page = examService.page(new Page<>(pageNum, pageSize));
        return ResponseEntity.ok(Response.success(page));
    }
}
