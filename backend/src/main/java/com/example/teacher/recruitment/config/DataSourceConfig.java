package com.example.teacher.recruitment.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() throws IOException {
        String dbPath = datasourceUrl.replace("jdbc:sqlite:", "");
        Path dbFilePath = Paths.get(dbPath);
        Path dataDir = dbFilePath.getParent();
        
        if (dataDir != null && !Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }
        
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
    }
}
