package com.studentsystem.studentSystem.Service;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
public interface StorageService {
    void init();
	String store(MultipartFile file);
    Path load(String filename) ;
    Resource loadFile(String fileName);
}
