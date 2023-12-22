package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.entity.*;
import com.web.repository.*;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/student/task")
public class StudentAddTaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/new")
    public ModelAndView getNewTask(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            ModelAndView modelAndView = new ModelAndView("student_addTask");
            Student currentStudent = studentRepository.findById(personCurrent.getPersonId()).orElse(null);
            Subject currentSubject = subjectRepository.findById(currentStudent.getSubjectId().getSubjectId()).orElse(null);
            List<Student> studentList = new ArrayList<>();
            if (currentSubject.getStudent1()!=null){
                studentList.add(currentSubject.getStudentId1());
            }
            if (currentSubject.getStudent2()!=null){
                studentList.add(currentSubject.getStudentId2());
            }
            modelAndView.addObject("student",currentStudent);
            modelAndView.addObject("listStudentGroup",studentList);
            return modelAndView;
        }else{
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @GetMapping("/list")
    public ModelAndView getListTask(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            ModelAndView modelAndView = new ModelAndView("QuanLyDeTai");
            Student currentStudent = studentRepository.findById(personCurrent.getPersonId()).orElse(null);
            Subject currentSubject = subjectRepository.findById(currentStudent.getSubjectId().getSubjectId()).orElse(null);
            List<Task> taskList = currentSubject.getTasks();
            modelAndView.addObject("listTask",taskList);
            modelAndView.addObject("person", personCurrent);
            return modelAndView;
        }else{
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/updateStatus/{id}")
    public ModelAndView updateStatus(HttpSession session, @PathVariable int id, @RequestParam String status, HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            Student currentStudent = studentRepository.findById(personCurrent.getPersonId()).orElse(null);
            Subject currentSubject = subjectRepository.findById(currentStudent.getSubjectId().getSubjectId()).orElse(null);
            Task existeTask = taskRepository.findById(id).orElse(null);
            if (existeTask!=null){
                existeTask.setStatus(status);
                taskRepository.save(existeTask);
            }
            String referer = "";
            String oldReferer = request.getHeader("Referer");
            if (oldReferer.equals(Contains.URL_LOCAL + "/api/student/task/list")) {
                referer = Contains.URL_LOCAL + "/api/student/task/list";
            }else if (oldReferer.equals(Contains.URL_LOCAL + "/api/student/task/detail/"+existeTask.getTaskId())){
                referer = Contains.URL_LOCAL + "/api/student/task/detail/"+existeTask.getTaskId();
            }
            return new ModelAndView("redirect:"+referer);
        }else{
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/create")
    public  ModelAndView createTask(HttpSession session,
                                    @RequestParam("requirement") String requirement,
                                    @RequestParam("timeStart") Date timeStart,
                                    @RequestParam("timeEnd") Date timeEnd,
                                    @RequestParam("assignTo") String assignTo,
                                    HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            Student currentStudent = studentRepository.findById(personCurrent.getPersonId()).orElse(null);
            Subject currentSubject = subjectRepository.findById(currentStudent.getSubjectId().getSubjectId()).orElse(null);
            Task newTask = new Task();
            List<Task> listTask = new ArrayList<>();
            newTask.setCreateBy(personCurrent);
            newTask.setInstructorId(currentStudent.getSubjectId().getInstructorId());
            newTask.setRequirement(requirement);
            newTask.setSubjectId(currentStudent.getSubjectId());
            newTask.setTimeStart(timeStart);
            newTask.setTimeEnd(timeEnd);
            Student existStudent = studentRepository.findById(assignTo).orElse(null);
            newTask.setAssignTo(existStudent);
            newTask.setStatus("MustDo");
            var task = taskRepository.save(newTask);
            listTask.add(task);
            currentSubject.setTasks(listTask);
            String referer = Contains.URL_LOCAL+ "/api/student/task/list";
            return new ModelAndView("redirect:"+referer);
        }else{
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @GetMapping("/detail/{taskId}")
    public ModelAndView getDetail(HttpSession session, @PathVariable int taskId){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            ModelAndView modelAndView = new ModelAndView("student_detailTask");
            Task currentTask = taskRepository.findById(taskId).orElse(null);
            List<FileComment> fileCommentList = fileRepository.findAllByTask(currentTask);
            List<Comment> commentList = currentTask.getComments();
            modelAndView.addObject("task", currentTask);
            modelAndView.addObject("listFile", fileCommentList);
            modelAndView.addObject("listComment", commentList);
            modelAndView.addObject("person", personCurrent);
            return modelAndView;
        }else{
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}