package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.entity.*;
import com.web.repository.*;
import com.web.service.MailServiceImpl;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private MailServiceImpl mailService;

    @GetMapping("/new")
    public ModelAndView getNewTask(HttpSession session) {
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            ModelAndView modelAndView = new ModelAndView("student_addTask");
            Student currentStudent = studentRepository.findById(personCurrent.getPersonId()).orElse(null);
            Subject currentSubject = subjectRepository.findById(currentStudent.getSubjectId().getSubjectId()).orElse(null);
            List<Student> studentList = new ArrayList<>();
            if (currentSubject.getStudent1() != null) {
                studentList.add(currentSubject.getStudentId1());
            }
            if (currentSubject.getStudent2() != null) {
                studentList.add(currentSubject.getStudentId2());
            }
            modelAndView.addObject("student", currentStudent);
            modelAndView.addObject("listStudentGroup", studentList);
            return modelAndView;
        } else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @GetMapping("/list")
    public ModelAndView getListTask(HttpSession session) {
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            ModelAndView modelAndView = new ModelAndView("QuanLyDeTai");
            Student currentStudent = studentRepository.findById(personCurrent.getPersonId()).orElse(null);
            Subject currentSubject = subjectRepository.findById(currentStudent.getSubjectId().getSubjectId()).orElse(null);
            List<Task> taskList = currentSubject.getTasks();
            modelAndView.addObject("listTask", taskList);
            modelAndView.addObject("person", personCurrent);
            return modelAndView;
        } else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }


    @PostMapping("/create")
    public ModelAndView createTask(HttpSession session,
                                   @RequestParam("requirement") String requirement,
                                   @RequestParam("timeStart") Date timeStart,
                                   @RequestParam("timeEnd") Date timeEnd,
                                   @RequestParam("assignTo") String assignTo,
                                   HttpServletRequest request) {
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

            // Lấy thông tin sinh viên được chọn từ danh sách sinh viên
            Student existStudent = studentRepository.findById(assignTo).orElse(null);
            newTask.setAssignTo(existStudent);
            newTask.setStatus("MustDo");
            var task = taskRepository.save(newTask);
            listTask.add(task);
            currentSubject.setTasks(listTask);
            String referer = Contains.URL_LOCAL + "/api/student/task/list";
            return new ModelAndView("redirect:" + referer);
        } else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    // Phương thức để lấy danh sách sinh viên cho việc tạo task
    private List<Student> getStudentListForTaskCreation(Subject currentSubject) {
        List<Student> studentList = new ArrayList<>();
        if (currentSubject.getStudent1() != null) {
            studentList.add(currentSubject.getStudentId1());
        }
        if (currentSubject.getStudent2() != null) {
            studentList.add(currentSubject.getStudentId2());
        }
        // Có thể thêm các điều kiện khác nếu còn sinh viên khác

        return studentList;
    }

    @PostMapping("/updateStatus/{id}")
    public ModelAndView updateStatus(HttpSession session, @PathVariable int id,@RequestParam String selectedOption) {
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            Task existTask = taskRepository.findById(id).orElse(null);
            if (existTask!=null){
                Subject existSubject = subjectRepository.findById(existTask.getSubjectId().getSubjectId()).orElse(null);
                existTask.setStatus(selectedOption);
                taskRepository.save(existTask);
                MailStructure newMail = new MailStructure();
                String subject = "Change status task " + existTask.getRequirement() ;
                String messenger = "Topic: " + existSubject.getSubjectName()+"\n" +
                        "Change by: " + personCurrent.getUsername() + "\n"
                        + "Change status task " + existTask.getRequirement() + " to " + selectedOption;
                newMail.setSubject(subject);
                newMail.setSubject(messenger);
                if (personCurrent.getPersonId().equals(existSubject.getStudentId1().getStudentId())) {
                    if (existSubject.getStudentId2()!=null) {
                        mailService.sendMail(existSubject.getStudentId2().getPerson().getUsername(), existSubject.getInstructorId().getPerson().getUsername(), subject, messenger);
                    }else {
                        mailService.sendMailNull(existSubject.getInstructorId().getPerson().getUsername(),subject,messenger);
                    }
                }else {
                    if (existSubject.getStudentId1()!=null) {
                        mailService.sendMail(existSubject.getStudentId1().getPerson().getUsername(), existSubject.getInstructorId().getPerson().getUsername(), subject, messenger);
                    }else {
                        mailService.sendMailNull(existSubject.getInstructorId().getPerson().getUsername(),subject,messenger);
                    }
                }
            }

            String referer = Contains.URL_LOCAL + "/api/student/task/detail/"+existTask.getTaskId();
            return new ModelAndView("redirect:" + referer);
        } else {
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
            List<String> options = Arrays.asList("MustDo", "Doing", "Closed");
            List<FileComment> fileCommentList = fileRepository.findAllByTask(currentTask);
            List<Comment> commentList = currentTask.getComments();
            modelAndView.addObject("task", currentTask);
            modelAndView.addObject("listFile", fileCommentList);
            modelAndView.addObject("listComment", commentList);
            modelAndView.addObject("person", personCurrent);
            modelAndView.addObject("options",options);
            return modelAndView;
        }else{
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}