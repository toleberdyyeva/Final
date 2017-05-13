package hello;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.http.HttpStatus;

//import hello.storage.StorageService;/**/

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Collections;


@Controller    // This means that this class is a Controller
// This means URL's start with /demo (after Application path)
public class MainController {

    private static String UPLOAD_DIR = "/Users/meraryslan/IdeaProjects/mid2/upload/files/";

    @Autowired // This means to get the bean called userRepository
    private FileMyRepository fileMyRepository;



    @RequestMapping(value = "/upload", method = RequestMethod.PUT)
    public ResponseEntity imageUpload(@RequestParam String user_id , @RequestParam("text") MultipartFile text ) {
        try {
            byte[] bytes = text.getBytes();
            String my_path = UPLOAD_DIR;
            Path path_folder = Paths.get(UPLOAD_DIR +user_id+ "/");
            if(!Files.exists(path_folder)){
                try{
                   Files.createDirectories(path_folder);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            double n = bytes.length/1024;
            if(n<20 && text.getContentType().equalsIgnoreCase("text/plain")){
                FileMy user = fileMyRepository.findByUserid(user_id);
                if(user != null ){
                    String count = String.valueOf(user.getFilepaths().size());
                    Path p = Paths.get(path_folder.toString() + "/" + "("+ count+")" +text.getOriginalFilename());
                    user.setAddPathFile(p.toString());
                    fileMyRepository.save(user);
                    Files.write(p,bytes);
                    System.out.println("saved");
                }if(user == null){
                    FileMy newUser = new FileMy();
                    Path p = Paths.get(path_folder.toString() + "/" + text.getOriginalFilename());
                    newUser.setId(user_id);
                    newUser.setAddPathFile(p.toString());
                    fileMyRepository.save(newUser);
                    Files.write(p,bytes);
                }
                return new ResponseEntity(HttpStatus.ACCEPTED);
            }else{
                System.out.println("failed");
            }
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<FileMy> getAllFiles() {
        return fileMyRepository.findAll();
    }

    @GetMapping(path="/byuserid")
    public @ResponseBody FileMy getAllFilesByUserId( @RequestParam String user_id ) {
        FileMy f = fileMyRepository.findByUserid(user_id);
        if(f != null){
            return fileMyRepository.findByUserid(user_id);
        }
        return fileMyRepository.findByUserid(user_id);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody FileMy deleteByPath( @RequestParam("path") String path , @RequestParam("user_id") String user_id) {
        FileMy m =  fileMyRepository.findByUserid(user_id);
        if(m != null  && m.getFilepaths().contains(path)){
            m.getFilepaths().remove(path);
        }
        fileMyRepository.save(m);
        return fileMyRepository.findByUserid(user_id);
    }
}