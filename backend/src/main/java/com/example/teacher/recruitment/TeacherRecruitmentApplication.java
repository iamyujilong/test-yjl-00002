
package com.example.teacher.recruitment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.teacher.recruitment.mapper")
@EnableScheduling
public class TeacherRecruitmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherRecruitmentApplication.class, args);
    }
}
