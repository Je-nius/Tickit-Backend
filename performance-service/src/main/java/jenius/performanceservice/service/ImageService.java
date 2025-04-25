package jenius.performanceservice.service;

import jenius.performanceservice.domain.Image;
import jenius.performanceservice.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public Image createImage(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String saveFileName = createSaveFileName(originalFilename);
        Path dirPath = Paths.get(uploadPath);
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(saveFileName);
        multipartFile.transferTo(filePath);

        Image image = Image.builder()
                .fileName(originalFilename)
                .savedFileName(saveFileName)
                .contentType(multipartFile.getContentType())
                .build();

        return imageRepository.save(image);
    }

    private String createSaveFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자명 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    // fullPath 만들기
    private String getFullPath(String filename) {
        return uploadPath + filename;
    }


}
