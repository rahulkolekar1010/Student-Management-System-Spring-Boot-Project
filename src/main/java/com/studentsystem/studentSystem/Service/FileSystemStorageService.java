package com.studentsystem.studentSystem.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import com.studentsystem.studentSystem.Exception.StorageException;;
@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    public FileSystemStorageService() {
        this.rootLocation=Paths.get("uploads");
    }

    @Override
    public void init() {
        try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
        
    }

    @Override
    public String store(MultipartFile file) {
        String newname="";
        // TODO Auto-generated method stub
        try{
        if(file.isEmpty()){
            throw new StorageException("Cannot upload empty file");
        }
        String extension = file.getOriginalFilename().substring( file.getOriginalFilename().lastIndexOf(".") + 1);
         newname="Img-"+Math.round(Math.random()*1000000)+"."+extension;
        Path destinationFile = this.rootLocation.resolve(
					Paths.get(newname))
					.normalize().toAbsolutePath();
                   
                    if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                        throw new StorageException(
                                "Cannot store file outside current directory.");
                    }
                    try (InputStream inputStream = file.getInputStream()) {

                        Files.copy(inputStream, destinationFile,
                            StandardCopyOption.REPLACE_EXISTING);
                    }
       
    }	catch (IOException e) {
        throw new StorageException("Failed to store file.", e);
    }
    return newname;
}
// @Override
// public Stream<Path> loadAll() {
//     try {
//         return Files.walk(this.rootLocation, 1)
//             .filter(path -> !path.equals(this.rootLocation))
//             .map(this.rootLocation::relativize);
//     }
//     catch (IOException e) {
//         throw new StorageException("Failed to read stored files", e);
//     }
    @Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}
    public Resource loadFile(String fileName) throws RuntimeException {
        Path filePath = this.rootLocation.resolve(fileName).normalize();
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL: " + e.getMessage());
        }
    
        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }
    
        return resource;
    }

}
