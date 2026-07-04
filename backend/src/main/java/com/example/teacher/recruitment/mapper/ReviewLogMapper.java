
package com.example.teacher.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.teacher.recruitment.entity.ReviewLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReviewLogMapper extends BaseMapper<ReviewLog> {

    @Select("SELECT rl.* FROM review_log rl WHERE rl.registration_id = #{registrationId} ORDER BY rl.review_time DESC")
    List<ReviewLog> selectByRegistrationId(@Param("registrationId") Long registrationId);
}
