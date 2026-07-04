
package com.example.teacher.recruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("certificate_item")
public class CertificateItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long registrationId;

    private String type;

    private String name;

    private String level;

    private String description;

    private LocalDateTime createTime;
}
