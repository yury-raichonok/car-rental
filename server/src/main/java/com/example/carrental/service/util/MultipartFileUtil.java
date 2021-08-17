package com.example.carrental.service.util;

import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public final class MultipartFileUtil {

  private MultipartFileUtil() {
  }

  public static void validateMultipartImageFile(MultipartFile file) {
    if (file.isEmpty()) {
      log.error("Cannot upload empty file [{}]", file.getOriginalFilename());
      throw new IllegalStateException(String.format("Cannot upload empty file [%s]",
          file.getOriginalFilename()));
    }
    if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType())
        .contains(file.getContentType())) {
      log.error("File must be an image [{}]", file.getContentType());
      throw new IllegalStateException(String.format("File must be an image [%s]",
          file.getContentType()));
    }
  }
}
