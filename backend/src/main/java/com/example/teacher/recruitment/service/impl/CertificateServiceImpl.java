
package com.example.teacher.recruitment.service.impl;

import com.example.teacher.recruitment.entity.CertificateFile;
import com.example.teacher.recruitment.entity.CertificateItem;
import com.example.teacher.recruitment.entity.Registration;
import com.example.teacher.recruitment.mapper.CertificateFileMapper;
import com.example.teacher.recruitment.mapper.CertificateItemMapper;
import com.example.teacher.recruitment.mapper.RegistrationMapper;
import com.example.teacher.recruitment.service.CertificateService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateItemMapper certificateItemMapper;
    private final CertificateFileMapper certificateFileMapper;
    private final RegistrationMapper registrationMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "pdf");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public CertificateServiceImpl(CertificateItemMapper certificateItemMapper,
                                   CertificateFileMapper certificateFileMapper,
                                   RegistrationMapper registrationMapper) {
        this.certificateItemMapper = certificateItemMapper;
        this.certificateFileMapper = certificateFileMapper;
        this.registrationMapper = registrationMapper;
    }

    @Override
    @Transactional
    public CertificateItem createItem(CertificateItem item) {
        Registration registration = registrationMapper.selectById(item.getRegistrationId());
        if (registration == null || registration.getStatus() >= 1) {
            throw new RuntimeException("报名不存在或已提交，无法添加证书");
        }
        item.setCreateTime(LocalDateTime.now());
        certificateItemMapper.insert(item);
        return item;
    }

    @Override
    @Transactional
    public CertificateItem updateItem(CertificateItem item) {
        CertificateItem existing = certificateItemMapper.selectById(item.getId());
        if (existing == null) {
            throw new RuntimeException("证书项不存在");
        }
        Registration registration = registrationMapper.selectById(existing.getRegistrationId());
        if (registration != null && registration.getStatus() >= 1) {
            throw new RuntimeException("报名已提交，无法修改证书");
        }
        certificateItemMapper.updateById(item);
        return item;
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        CertificateItem item = certificateItemMapper.selectById(id);
        if (item == null) {
            throw new RuntimeException("证书项不存在");
        }
        Registration registration = registrationMapper.selectById(item.getRegistrationId());
        if (registration != null && registration.getStatus() >= 1) {
            throw new RuntimeException("报名已提交，无法删除证书");
        }
        List<CertificateFile> files = certificateFileMapper.selectByItemId(id);
        for (CertificateFile file : files) {
            FileUtils.deleteQuietly(new File(file.getStoragePath()));
            certificateFileMapper.deleteById(file.getId());
        }
        certificateItemMapper.deleteById(id);
    }

    @Override
    public List<CertificateItem> listByRegistrationId(Long registrationId) {
        return certificateItemMapper.selectByRegistrationId(registrationId);
    }

    @Override
    public List<CertificateItem> listByRegistrationIdAndType(Long registrationId, String type) {
        return certificateItemMapper.selectByRegistrationIdAndType(registrationId, type);
    }

    @Override
    @Transactional
    public CertificateFile uploadFile(Long itemId, MultipartFile file) {
        CertificateItem item = certificateItemMapper.selectById(itemId);
        if (item == null) {
            throw new RuntimeException("证书项不存在");
        }
        Registration registration = registrationMapper.selectById(item.getRegistrationId());
        if (registration != null && registration.getStatus() >= 1) {
            throw new RuntimeException("报名已提交，无法上传文件");
        }

        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
        String newFilename = UUID.randomUUID().toString() + "." + extension;

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File destFile = new File(uploadDir, newFilename);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }

        CertificateFile certificateFile = new CertificateFile();
        certificateFile.setItemId(itemId);
        certificateFile.setFileName(originalFilename);
        certificateFile.setStoragePath(destFile.getAbsolutePath());
        certificateFile.setFileType(extension);
        certificateFile.setFileSize(file.getSize());
        certificateFile.setCreateTime(LocalDateTime.now());

        certificateFileMapper.insert(certificateFile);
        return certificateFile;
    }

    @Override
    public List<CertificateFile> listFilesByItemId(Long itemId) {
        return certificateFileMapper.selectByItemId(itemId);
    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        CertificateFile file = certificateFileMapper.selectById(id);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }
        CertificateItem item = certificateItemMapper.selectById(file.getItemId());
        if (item != null) {
            Registration registration = registrationMapper.selectById(item.getRegistrationId());
            if (registration != null && registration.getStatus() >= 1) {
                throw new RuntimeException("报名已提交，无法删除文件");
            }
        }
        FileUtils.deleteQuietly(new File(file.getStoragePath()));
        certificateFileMapper.deleteById(id);
    }

    @Override
    public CertificateFile getFileById(Long id) {
        return certificateFileMapper.selectById(id);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
            throw new RuntimeException("无效的文件名");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new RuntimeException("文件格式不支持，仅支持JPG/PNG/PDF格式");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("文件大小超过限制，单文件最大5MB");
        }
    }
}
