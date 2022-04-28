package com.example.backendproject.controller;

import com.example.backendproject.entity.*;
import com.example.backendproject.services.DatabaseServices;
import com.example.backendproject.services.FileUploadServices;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DatabaseController {
    private final DatabaseServices databaseServices;
    private final FileUploadServices fileUploadServices;
    public DatabaseController(DatabaseServices databaseServices,FileUploadServices fileUploadServices) {
        this.databaseServices = databaseServices;
        this.fileUploadServices=fileUploadServices;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/uploadFile")
    public ResponseEntity.BodyBuilder uploadFile(@RequestParam("pdf-file") MultipartFile file) throws IOException {
        if(selectedUser>0){
            fileUploadServices.uploadFile(file,selectedUser);
        }
        return ResponseEntity.ok();
    }
    private static int selectedUser=-10;
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getAuthors")
    public @ResponseBody ArrayList<Author> getAuthors(){
        //yazarları bütün özellikleri ile dönderir
        ArrayList<Author> authors =databaseServices.callGetAuthor(selectedUser);
        return authors;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getLessons")
    public @ResponseBody ArrayList<String> getLessons(){
        //derslerin adlarını dönderir
        ArrayList<String> lessons=databaseServices.getLessonType();
        return lessons;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getProjects")
    public @ResponseBody ArrayList<Project> getProject(){
        //projelerin büyün özelliklerini dönderir
        ArrayList<Project> projects=databaseServices.callGetProjects(selectedUser);
        return projects;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getKeywords")
    public @ResponseBody ArrayList<Keyword> getKeyWords(){
        //anahtar kelimeleri id leri ile birlikte dönderir
        ArrayList<Keyword> keywords=databaseServices.callGetKeyword(selectedUser);
        return keywords;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getDeliveryPeriods")
    public @ResponseBody ArrayList<String> getDeliveryPeriods(){
        //teslim zamanlarını string olarak dönderir eşsiz olan değerleri dönderir
        ArrayList<String> deliveryPeriods=databaseServices.getDeliveryPeriod();
        return deliveryPeriods;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getProjectsForAuthor")
    public @ResponseBody ArrayList<Project> getProjectsForAuthor(@RequestParam String nameSurnameStr){
        //yazarlar için proje listesini dönderir
        return databaseServices.callGetProjectForAuthor(nameSurnameStr,selectedUser);
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getProjectsForLesson")
    public @ResponseBody ArrayList<Project> getProjectsForLesson(@RequestParam String lesson){
        //ders tipine göre proje listesi dönderir
        return databaseServices.callGetProjectForLesson(lesson,selectedUser);
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getProjectsForDeliveryPeriod")
    public @ResponseBody ArrayList<Project> getProjectForDeliveryPeriod(@RequestParam String deliveryPeriod){
        // teslim dönemine göre proje listesi dönderir
        return databaseServices.callGetProjectForDeliveryPeriod(deliveryPeriod,selectedUser);
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getProjectsForKeyWords")
    public @ResponseBody ArrayList<Project> getProjectsForKeywords(@RequestParam String keyWord){
        //anahtar kelimelere göre proje listesi dönderir
        return databaseServices.callGetProjectForKeywords(keyWord,selectedUser);
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getAuthorsForProjectName")
    public @ResponseBody ArrayList<Author> getAuthorsForProjectName(@RequestParam String projectName){
        //proje adına göre yazar listesini dönderir
        return databaseServices.callGetAuthorForProjectName(projectName,selectedUser);
    }
    /*@CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/userLogin")
    public int  loginUser(@RequestParam String userName, @RequestParam String password){
        selectedUser=databaseServices.userLogin(userName,password);
        return selectedUser;
    }*/
    @CrossOrigin(origins = "", allowedHeaders = "")
    @PostMapping("/userLogin")
    public @ResponseBody int loginUser(@RequestParam String userName,@RequestParam  String password){
        selectedUser=databaseServices.userLogin(userName,password);
        return selectedUser;
    }
    /*@CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/adminLogin")
    public int  loginAdmin(@RequestParam String userName, @RequestParam String password){
        if(databaseServices.adminLogin(userName,password)){
            selectedUser =-10;
            return 1;
        }
        return -1;
    }*/
    @CrossOrigin(origins = "", allowedHeaders = "")
    @PostMapping("/adminLogin")
    public @ResponseBody int loginAdmin(@RequestParam String userName,@RequestParam  String password){
        if(databaseServices.adminLogin(userName,password)){
            selectedUser =-10;
            return selectedUser;
        }
        return -1;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/signUser")
    public @ResponseBody int signUser(@RequestParam String userName,@RequestParam  String password){
        int control=databaseServices.signUser(userName,password);
        if(control==1){
            return 1;
        }else if(control==0){
            return 0;
        }else {
            return -1;
        }
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getProjectForPeriodAndUserNameAndLesson")
    public @ResponseBody ArrayList<Project> getProjectForPeriodAndUserNameAndLesson(@RequestParam String deliveryPeriod
            , @RequestParam String userName ,@RequestParam String lessonType){
        //SORGU 2
        //bu sorguyu sadece admin yapabilir ve admin admin kullanıcı adına ve döneme göre hangi kullanıcının ne yüklediğini görebilir
        return databaseServices.getProjectForPeriodAndForLesson(deliveryPeriod,userName,lessonType);
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getProjectFullData")
    public @ResponseBody ProjectFullData getProjectFullData(@RequestParam int projectId){
        return databaseServices.returnProjectFullData(projectId);
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/deleteUser")
    public @ResponseBody int deleteUser(@RequestParam String userName){
        return databaseServices.deleteUser(userName);
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/updateUser")
    public @ResponseBody int updateUser(@RequestParam String newUserName ,@RequestParam String newPassword
            , @RequestParam String userName){
        return databaseServices.updateUser(newUserName,newPassword,userName);
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getUsers")
    public @ResponseBody ArrayList<User> getUsers(){
        return databaseServices.getUsers();
    }
}
