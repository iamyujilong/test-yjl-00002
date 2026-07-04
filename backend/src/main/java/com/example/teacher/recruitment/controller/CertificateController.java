
package com.example.teacher.recruitment.controller;

import com.example.teacher.recruitment.common.Response;
import com.example.teacher.recruitment.entity.CertificateFile;
import com.example.teacher.recruitment.entity.CertificateItem;
import com.example.teacher.recruitment.service.CertificateService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/items")
    public ResponseEntity<Response<CertificateItem>> createItem(@RequestBody CertificateItem item) {
        CertificateItem created = certificateService.createItem(item);
        return ResponseEntity.ok(Response.success("创建成功", created));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Response<CertificateItem>> updateItem(@PathVariable Long id, @RequestBody CertificateItem item) {
        item.setId(id);
        CertificateItem updated = certificateService.updateItem(item);
        return ResponseEntity.ok(Response.success("更新成功", updated));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Response<Void>> deleteItem(@PathVariable Long id) {
        certificateService.deleteItem(id);
        return ResponseEntity.ok(Response.success("删除成功", null));
    }

    @GetMapping("/items/registration/{registrationId}")
    public ResponseEntity<Response<List<CertificateItem>>> listByRegistrationId(@PathVariable Long registrationId) {
        List<CertificateItem> items = certificateService.listByRegistrationId(registrationId);
        return ResponseEntity.ok(Response.success(items));
    }

    @GetMapping("/items/registration/{registrationId}/type/{type}")
    public ResponseEntity<Response<List<CertificateItem>>> listByRegistrationIdAndType(
            @PathVariable Long registrationId, @PathVariable String type) {
        List<CertificateItem> items = certificateService.listByRegistrationIdAndType(registrationId, type);
        return ResponseEntity.ok(Response.success(items));
    }

    @PostMapping("/files/{itemId}")
    public ResponseEntity<Response<CertificateFile>> uploadFile(
            @PathVariable Long itemId,
            @RequestParam("file") MultipartFile file) {
        CertificateFile uploaded = certificateService.uploadFile(itemId, file);
        return ResponseEntity.ok(Response.success("上传成功", uploaded));
    }

    @GetMapping("/files/item/{itemId}")
    public ResponseEntity<Response<List<CertificateFile>>> listFilesByItemId(@PathVariable Long itemId) {
        List<CertificateFile> files = certificateService.listFilesByItemId(itemId);
        return ResponseEntity.ok(Response.success(files));
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<Response<Void>> deleteFile(@PathVariable Long id) {
        certificateService.deleteFile(id);
        return ResponseEntity.ok(Response.success("删除成功", null));
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Response<CertificateFile>> getFileById(@PathVariable Long id) {
        CertificateFile file = certificateService.getFileById(id);
        return ResponseEntity.ok(Response.success(file));
    }

    @GetMapping("/files/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        CertificateFile certFile = certificateService.getFileById(id);
        if (certFile == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(certFile.getStoragePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        
        String contentType = switch (certFile.getFileType().toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + certFile.getFileName() + "\"")
                .body(resource);
    }
}
