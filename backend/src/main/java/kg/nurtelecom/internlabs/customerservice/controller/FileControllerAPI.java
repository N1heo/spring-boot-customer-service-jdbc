package kg.nurtelecom.internlabs.customerservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import kg.nurtelecom.internlabs.customerservice.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
public class FileControllerAPI {

    private final StorageService storageService;

    public FileControllerAPI(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(
            @PathVariable String filename,
            HttpServletRequest request
    ) {

        Resource file = storageService.loadAsResource(filename);

        String contentType = null;

        try {
            contentType = request.getServletContext()
                    .getMimeType(file.getFile().getAbsolutePath());
        } catch (Exception ignored) {}

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}

