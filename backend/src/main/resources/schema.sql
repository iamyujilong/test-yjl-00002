
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `id_card` VARCHAR(18) NOT NULL UNIQUE COMMENT '身份证号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT '角色 0-普通用户 1-管理员',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_id_card` (`id_card`),
    INDEX `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `exam` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '考试名称',
    `description` TEXT COMMENT '考试描述',
    `teacher_type` TINYINT NOT NULL COMMENT '教师类型 0-中小学 1-大学',
    `start_time` DATETIME NOT NULL COMMENT '报名开始时间',
    `end_time` DATETIME NOT NULL COMMENT '报名结束时间',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态 0-未开始 1-开放中 2-已结束',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_status` (`status`),
    INDEX `idx_teacher_type` (`teacher_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试表';

CREATE TABLE IF NOT EXISTS `registration` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `exam_id` BIGINT NOT NULL COMMENT '考试ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` TINYINT NOT NULL COMMENT '性别 0-男 1-女',
    `birth_date` DATE NOT NULL COMMENT '出生日期',
    `id_card` VARCHAR(18) NOT NULL COMMENT '身份证号',
    `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
    `education` VARCHAR(50) COMMENT '学历',
    `major` VARCHAR(100) COMMENT '专业',
    `title` VARCHAR(50) NOT NULL COMMENT '职称',
    `teach_certificate_level` VARCHAR(20) COMMENT '教师资格证学段',
    `teach_certificate_subject` VARCHAR(50) COMMENT '教师资格证科目',
    `teacher_type` TINYINT NOT NULL COMMENT '教师类型 0-中小学 1-大学',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态 0-草稿 1-已提交 2-审核中 3-通过 4-不通过',
    `submit_time` DATETIME COMMENT '提交时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_exam` (`user_id`, `exam_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_exam_id` (`exam_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报名表';

CREATE TABLE IF NOT EXISTS `certificate_item` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `registration_id` BIGINT NOT NULL COMMENT '报名ID',
    `type` VARCHAR(50) NOT NULL COMMENT '证书类型 HONOR-荣誉称号 OPEN_CLASS-公开课赛课 GRADUATION-毕业年级证明',
    `name` VARCHAR(100) COMMENT '证书名称',
    `level` VARCHAR(20) COMMENT '级别 PROVINCIAL-省级 MUNICIPAL-市级 NATIONAL-国家级',
    `description` TEXT COMMENT '描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_registration_id` (`registration_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='证书项表';

CREATE TABLE IF NOT EXISTS `certificate_file` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `item_id` BIGINT NOT NULL COMMENT '证书项ID',
    `file_name` VARCHAR(200) NOT NULL COMMENT '原始文件名',
    `storage_path` VARCHAR(500) NOT NULL COMMENT '存储路径',
    `file_type` VARCHAR(20) COMMENT '文件类型',
    `file_size` BIGINT NOT NULL COMMENT '文件大小(字节)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='证书文件表';

CREATE TABLE IF NOT EXISTS `review_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `registration_id` BIGINT NOT NULL COMMENT '报名ID',
    `reviewer_id` BIGINT NOT NULL COMMENT '审核人ID',
    `review_status` TINYINT NOT NULL COMMENT '审核状态 3-通过 4-不通过',
    `remark` TEXT COMMENT '备注说明',
    `review_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
    INDEX `idx_registration_id` (`registration_id`),
    INDEX `idx_reviewer_id` (`reviewer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审核记录表';

INSERT INTO `user` (`id_card`, `name`, `phone`, `password`, `role`, `status`) VALUES ('admin', '管理员', '18888888888', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 1, 1);
