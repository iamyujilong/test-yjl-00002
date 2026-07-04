
package com.example.teacher.recruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.teacher.recruitment.entity.Exam;

import java.util.List;

public interface ExamService {

    Exam create(Exam exam);

    Exam update(Exam exam);

    void delete(Long id);

    Exam getById(Long id);

    List<Exam> listAll();

    List<Exam> listOpen();

    IPage<Exam> page(Page<Exam> page);
}
