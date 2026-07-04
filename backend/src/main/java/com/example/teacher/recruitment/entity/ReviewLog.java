
package com.example.teacher.recruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("review_log")
public class ReviewLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long registrationId;

    private Long reviewerId;

    private Integer reviewStatus;

    private String remark;

    private LocalDateTime reviewTime;
}
