
package com.example.teacher.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.teacher.recruitment.entity.Exam;
import com.example.teacher.recruitment.mapper.ExamMapper;
import com.example.teacher.recruitment.service.ExamService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamMapper examMapper;

    public ExamServiceImpl(ExamMapper examMapper) {
        this.examMapper = examMapper;
    }

    @Override
    @Transactional
    public Exam create(Exam exam) {
        exam.setStatus(0);
        exam.setCreateTime(LocalDateTime.now());
        exam.setUpdateTime(LocalDateTime.now());
        examMapper.insert(exam);
        updateStatus();
        return exam;
    }

    @Override
    @Transactional
    public Exam update(Exam exam) {
        exam.setUpdateTime(LocalDateTime.now());
        examMapper.updateById(exam);
        updateStatus();
        return exam;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        examMapper.deleteById(id);
    }

    @Override
    public Exam getById(Long id) {
        return examMapper.selectById(id);
    }

    @Override
    public List<Exam> listAll() {
        return examMapper.selectList(null);
    }

    @Override
    public List<Exam> listOpen() {
        updateStatus();
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Exam::getStatus, 1);
        return examMapper.selectList(wrapper);
    }

    @Override
    public IPage<Exam> page(Page<Exam> page) {
        updateStatus();
        return examMapper.selectPage(page, null);
    }

    private void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<Exam> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Exam::getStatus, 0)
                .le(Exam::getStartTime, now);
        examMapper.update(new Exam() {{ setStatus(1); }}, wrapper1);

        LambdaQueryWrapper<Exam> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Exam::getStatus, 1)
                .lt(Exam::getEndTime, now);
        examMapper.update(new Exam() {{ setStatus(2); }}, wrapper2);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void scheduledUpdateStatus() {
        updateStatus();
    }
}
