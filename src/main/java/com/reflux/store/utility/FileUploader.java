package com.reflux.store.utility;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUploader {

    public static String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null) {
            throw new IOException("Invalid file");
        }

        String filenameWithoutExt = originalFilename.substring(originalFilename.lastIndexOf("."));
        String randomFilename = UUID.randomUUID().toString();
        String filename = randomFilename.concat(filenameWithoutExt);
        String filePath = path + File.pathSeparator + filename;

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));
        return filename;
    }

}
