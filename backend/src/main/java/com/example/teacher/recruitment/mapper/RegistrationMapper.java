
package com.example.teacher.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.teacher.recruitment.entity.Registration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RegistrationMapper extends BaseMapper<Registration> {

    @Select("SELECT r.* FROM registration r WHERE r.user_id = #{userId}")
    List<Registration> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT r.* FROM registration r WHERE r.exam_id = #{examId}")
    List<Registration> selectByExamId(@Param("examId") Long examId);
}
