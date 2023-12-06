package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.entity.Person;
import com.web.repository.PersonRepository;
import com.web.service.Admin.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/admin/users")
public class    PersonController {
    @Autowired
    PersonService personService;
    //@Autowired
    //PersonMapper personMapper;
    @Autowired
    private PersonRepository personRepository;



//    @PostMapping("/login")
    @GetMapping("/list")
    public ResponseEntity<?> getUserExisted(@RequestParam(name = "token", required = false) String token,HttpServletRequest request){
        // Thực hiện kiểm tra token hoặc xử lý logic bảo mật tại đây
        if (isValidToken(token)) {
            // Nếu token hợp lệ, thực hiện lấy danh sách người dùng
            List<Person> persons = personRepository.findAll();
            return ResponseEntity.ok(persons);
        } else {
            // Nếu token không hợp lệ, trả về lỗi 403 Forbidden
            return ResponseEntity.status(403).body("Invalid token" + token);
        }
    }

    private boolean isValidToken(String token) {
        // Thực hiện kiểm tra token tại đây, có thể sử dụng thư viện bảo mật, so sánh giá trị, vv.
        // Trong ví dụ này, tạm thời giả sử token là hợp lệ nếu không null hoặc trống
        return token != null && !token.isEmpty();
    }

    @GetMapping("/current")
    public ResponseEntity<?> getUser(){
       /* if (CheckedPermission.isAdmin(personRepository)) {*/
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Person currentUser = (Person) authentication.getPrincipal();
            return ResponseEntity.ok(currentUser);
        /*}
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
    }
}
