package vn.hoidanit.jobhunter.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.springframework.http.HttpHeaders;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.jobhunter.domain.dto.response.file.ResFileUploadDTO;
import vn.hoidanit.jobhunter.exception.StorageException;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.FileService;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @Value("${hoidanit.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<RestResponse<ResFileUploadDTO>> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {

        // validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please upload a file");
        }

        // check extensions
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "png", "jpeg", "doc", "docx");

        boolean isValidExtensions = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValidExtensions) {
            throw new StorageException("Invalid file extensions. Only allows [pdf, jpg, png, jpeg, doc, docx]");
        }

        // check and create a directory if not exists
        this.fileService.createDirectory(baseURI + folder);
        // store file
        String uploadedFile = this.fileService.store(file, folder);

        // response
        ResFileUploadDTO res = new ResFileUploadDTO(uploadedFile, Instant.now());
        return ResponseFactory.success(res, "Upload file successfully");
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> download(
            @RequestParam(name = "fileName") String fileName,
            @RequestParam(name = "folder") String folder)
            throws StorageException, URISyntaxException, FileNotFoundException {

        if (fileName == null || folder == null) {
            throw new StorageException("Missing required params");
        }

        // check file exist (and not a directory)
        long fileLength = this.fileService.getFileLength(fileName, folder);

        if (fileLength == 0) {
            throw new StorageException("File with name = " + fileName + " not found");
        }

        // download a file
        InputStreamResource resource = this.fileService.getReSource(fileName, folder);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
