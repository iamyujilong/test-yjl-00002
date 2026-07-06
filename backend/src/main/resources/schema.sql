CREATE TABLE IF NOT EXISTS `user` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `id_card` VARCHAR(18) NOT NULL UNIQUE,
    `name` VARCHAR(50) NOT NULL,
    `phone` VARCHAR(11) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `role` INTEGER NOT NULL DEFAULT 0,
    `status` INTEGER NOT NULL DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS `idx_id_card` ON `user` (`id_card`);
CREATE INDEX IF NOT EXISTS `idx_phone` ON `user` (`phone`);

CREATE TABLE IF NOT EXISTS `exam` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `teacher_type` INTEGER NOT NULL,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `status` INTEGER NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS `idx_exam_status` ON `exam` (`status`);
CREATE INDEX IF NOT EXISTS `idx_teacher_type` ON `exam` (`teacher_type`);

CREATE TABLE IF NOT EXISTS `registration` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `user_id` INTEGER NOT NULL,
    `exam_id` INTEGER NOT NULL,
    `name` VARCHAR(50) NOT NULL,
    `gender` INTEGER NOT NULL,
    `birth_date` DATE NOT NULL,
    `id_card` VARCHAR(18) NOT NULL,
    `phone` VARCHAR(11) NOT NULL,
    `education` VARCHAR(50),
    `major` VARCHAR(100),
    `title` VARCHAR(50) NOT NULL,
    `teach_certificate_level` VARCHAR(20),
    `teach_certificate_subject` VARCHAR(50),
    `teacher_type` INTEGER NOT NULL,
    `status` INTEGER NOT NULL DEFAULT 0,
    `submit_time` DATETIME,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(`user_id`, `exam_id`)
);

CREATE INDEX IF NOT EXISTS `idx_reg_user_id` ON `registration` (`user_id`);
CREATE INDEX IF NOT EXISTS `idx_reg_exam_id` ON `registration` (`exam_id`);
CREATE INDEX IF NOT EXISTS `idx_reg_status` ON `registration` (`status`);

CREATE TABLE IF NOT EXISTS `certificate_item` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `registration_id` INTEGER NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `name` VARCHAR(100),
    `level` VARCHAR(20),
    `description` TEXT,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS `idx_cert_item_reg_id` ON `certificate_item` (`registration_id`);

CREATE TABLE IF NOT EXISTS `certificate_file` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `item_id` INTEGER NOT NULL,
    `file_name` VARCHAR(200) NOT NULL,
    `storage_path` VARCHAR(500) NOT NULL,
    `file_type` VARCHAR(20),
    `file_size` INTEGER NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS `idx_cert_file_item_id` ON `certificate_file` (`item_id`);

CREATE TABLE IF NOT EXISTS `review_log` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `registration_id` INTEGER NOT NULL,
    `reviewer_id` INTEGER NOT NULL,
    `review_status` INTEGER NOT NULL,
    `remark` TEXT,
    `review_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS `idx_review_reg_id` ON `review_log` (`registration_id`);
CREATE INDEX IF NOT EXISTS `idx_review_reviewer_id` ON `review_log` (`reviewer_id`);

INSERT OR IGNORE INTO `user` (`id_card`, `name`, `phone`, `password`, `role`, `status`) VALUES ('admin', '管理员', '18888888888', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 1, 1);
