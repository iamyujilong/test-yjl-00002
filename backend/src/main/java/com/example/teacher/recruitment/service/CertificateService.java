
package com.example.teacher.recruitment.service;

import com.example.teacher.recruitment.entity.CertificateFile;
import com.example.teacher.recruitment.entity.CertificateItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CertificateService {

    CertificateItem createItem(CertificateItem item);

    CertificateItem updateItem(CertificateItem item);

    void deleteItem(Long id);

    List<CertificateItem> listByRegistrationId(Long registrationId);

    List<CertificateItem> listByRegistrationIdAndType(Long registrationId, String type);

    CertificateFile uploadFile(Long itemId, MultipartFile file);

    List<CertificateFile> listFilesByItemId(Long itemId);

    void deleteFile(Long id);

    CertificateFile getFileById(Long id);
}
