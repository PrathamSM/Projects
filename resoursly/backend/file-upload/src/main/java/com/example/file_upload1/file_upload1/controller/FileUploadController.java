package com.example.file_upload1.file_upload1.controller;



//
//import com.example.file_upload1.file_upload1.dto.CreateProfileReq;
//import com.example.file_upload1.file_upload1.feign.ProfileClient;
//import com.example.file_upload1.file_upload1.service.FileProcessingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/file-upload")
//public class FileUploadController {
//
//    @Autowired
//    private FileProcessingService fileProcessingService;
//
//    @Autowired
//    private ProfileClient profileClient;
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            // Extract data from the file
//            CreateProfileReq createProfileReq = fileProcessingService.extractData(file);
//
//            // Send data to Profile Microservice
//            ResponseEntity<?> response = profileClient.createProfile(createProfileReq);
//
//            return ResponseEntity.ok(response.getBody());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }
//}



// text _this  database, postman feign client add check ^|

import com.example.file_upload1.file_upload1.dto.CreateProfileReq;
import com.example.file_upload1.file_upload1.service.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin("http://localhost:5173")
public class FileUploadController {

    @Autowired
    private FileProcessingService fileProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<List<CreateProfileReq>> uploadResumes(@RequestParam("files") List<MultipartFile> files) {
        try {
            // Process files and store in the database
            List<CreateProfileReq> profiles = fileProcessingService.processAndStoreFiles(files);

            // Return the extracted profiles as the response
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            // Handle any exceptions during file processing
            return ResponseEntity.badRequest().body(null);
        }

    }

}


