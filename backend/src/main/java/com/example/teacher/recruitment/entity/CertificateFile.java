
package com.example.teacher.recruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("certificate_file")
public class CertificateFile {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long itemId;

    private String fileName;

    private String storagePath;

    private String fileType;

    private Long fileSize;

    private LocalDateTime createTime;
}
