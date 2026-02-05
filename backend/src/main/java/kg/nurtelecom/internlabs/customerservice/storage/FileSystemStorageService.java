package kg.nurtelecom.internlabs.customerservice.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path root;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.root = Paths.get(properties.getLocation());

        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new StorageException("Could not init storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }

        String ext = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + ext;

        try {
            Path destination = root.resolve(filename).normalize();

            if (!destination.getParent().equals(root)) {
                throw new StorageException("Cannot store file outside root");
            }

            Files.copy(file.getInputStream(), destination);

            return filename;

        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }

            throw new StorageFileNotFoundException("File not found: " + filename);

        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("File not found: " + filename);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Files.deleteIfExists(root.resolve(filename));
        } catch (IOException e) {
            throw new StorageException("Failed to delete file", e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int i = filename.lastIndexOf('.');
        return i >= 0 ? filename.substring(i) : "";
    }
}
