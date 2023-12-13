package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.entity.*;
import com.web.repository.CommentRepository;
import com.web.repository.PersonRepository;
import com.web.repository.TaskRepository;
import com.web.service.FileMaterialService;
import com.web.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/student/comment")
public class StudentAddCommentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentAddCommentController.class);
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private FileMaterialService fileMaterialService;
    @PostMapping("/create/{taskId}")
    public ModelAndView createComment(@PathVariable int taskId,
                                      @RequestParam("content") String content,
                                      @RequestParam("fileInput") List<MultipartFile> files,
                                      HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            Comment newComment = new Comment();
            newComment.setContent(content);
            newComment.setPoster(personCurrent);
            Task existTask = taskRepository.findById(taskId).orElse(null);
            newComment.setTaskId(existTask);
            LocalDateTime nowDate = LocalDateTime.now();
            Date dateSubmit = Date.from(nowDate.atZone(ZoneId.systemDefault()).toInstant());
            newComment.setDateSubmit(dateSubmit);
            var comment = commentRepository.save(newComment);
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String fileName = fileMaterialService.storeFile(file);
                        FileComment newFile = new FileComment();
                        newFile.setName(fileName);
                        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/api/student/comment/fileUpload/")
                                .path(fileName)
                                .toUriString();
                        newFile.setUrl(fileDownloadUri);
                        newFile.setCommentId(comment);
                        newFile.setTaskId(comment.getTaskId());
                        var fileSave = fileMaterialService.uploadFile(newFile);
                        List<FileComment> fileList = comment.getFileComments();
                        if (fileList == null) {
                            fileList = new ArrayList<>();
                        }
                        fileList.add(fileSave);
                        comment.setFileComments(fileList);
                        commentRepository.save(comment);
                    }
                }
            }
            String referer = "http://localhost:5000/api/student/task/detail/" + taskId;
            return new ModelAndView("redirect:"+referer);
        }else{
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }



}
