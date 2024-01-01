package com.web.service;

import com.web.entity.FileComment;
import com.web.exception.MyFileNotFoundException;
import com.web.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileMaterialService {
    @Autowired
    FileRepository fileMaterialRepository;

    // Đường dẫn lưu trữ file
    private final Path fileStorageLocation;

    // Phương thức lưu trữ file
    public String storeFile(MultipartFile file) {
        // Chuẩn hóa tên file
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Kiểm tra xem tên file có chứa ký tự không hợp lệ không
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Sao chép file đến vị trí đích (thay thế file đã tồn tại cùng tên)
            File folder = new File(String.valueOf(this.fileStorageLocation));
            for (File e : Objects.requireNonNull(folder.listFiles())) {
                if (e.getName().equals(fileName)) {
                    String suffix = fileName.substring(fileName.lastIndexOf("."));
                    String name = fileName.substring(0, fileName.lastIndexOf("."));
                    name += System.currentTimeMillis();
                    fileName = name + suffix;
                    break;
                }
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    // Phương thức tải file dưới dạng Resource
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    // Phương thức lưu thông tin file vào cơ sở dữ liệu
    public FileComment uploadFile(FileComment fileMaterial) {
        return fileMaterialRepository.save(fileMaterial);
    }

    // Khởi tạo và cài đặt đường dẫn lưu trữ file
    public FileMaterialService() {
        this.fileStorageLocation = Paths.get("F:/")
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
}
