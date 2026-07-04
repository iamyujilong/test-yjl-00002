
package com.example.teacher.recruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("registration")
public class Registration {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long examId;

    private String name;

    private Integer gender;

    private LocalDate birthDate;

    private String idCard;

    private String phone;

    private String education;

    private String major;

    private String title;

    private String teachCertificateLevel;

    private String teachCertificateSubject;

    private Integer teacherType;

    private Integer status;

    private LocalDateTime submitTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
