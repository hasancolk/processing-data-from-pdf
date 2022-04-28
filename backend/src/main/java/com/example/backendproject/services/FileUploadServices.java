package com.example.backendproject.services;

import com.example.backendproject.program.FindInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Service
public class FileUploadServices {
    FindInfo findInfo;
    public void uploadFile(MultipartFile file , int selectedUserId) throws IOException {
        file.transferTo(new File("C:\\Server\\UploadFiles\\"+file.getOriginalFilename()));
        findInfo=new FindInfo();
        findInfo.findFromPdf("C:\\Server\\UploadFiles\\"+file.getOriginalFilename(),selectedUserId);
    }
}
