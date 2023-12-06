package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.entity.SchoolYear;
import com.web.mapper.SchoolYearMapper;
import com.web.dto.request.SchoolYearRequest;
import com.web.repository.PersonRepository;
import com.web.repository.SchoolYearRepository;
import com.web.service.Admin.SchoolYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/schoolYear")
public class SchoolYearController {
    @Autowired
    private SchoolYearService schoolYearService;
    @Autowired
    private SchoolYearMapper schoolYearMapper;
    @Autowired
    private SchoolYearRepository schoolYearRepository;
    @Autowired
    private PersonRepository personRepository;


    @GetMapping("/list")
    public ResponseEntity<?> getAllSchoolYear(){
        /*if (CheckedPermission.isAdmin(personRepository)){*/
            List<SchoolYear> schoolYears = schoolYearService.findAll();
            return ResponseEntity.ok(schoolYearMapper.toSchoolYearListDTO(schoolYears));
        /*}else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
    }

    @PostMapping("/create")
    public ResponseEntity<?> saveSchoolYear(@RequestBody SchoolYearRequest schoolYearRequest){
        /*if (CheckedPermission.isAdmin(personRepository)) {*/
            return new ResponseEntity<>(schoolYearService.createSchoolYear(schoolYearRequest), HttpStatus.CREATED);

        /*}else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
        /*
        {
            "year":"2020-2024"
        }
        */
    }

    @PostMapping("/edit/{yearId}")
    public ResponseEntity<?> updateYear(@PathVariable int yearId, @RequestBody SchoolYear updateSchoolYear){
        /*if (CheckedPermission.isAdmin(personRepository)) {*/
            SchoolYear existSchoolYear = schoolYearRepository.findById(yearId).orElse(null);
            if (existSchoolYear != null) {
                existSchoolYear.setYear(updateSchoolYear.getYear());
                return new ResponseEntity<>(schoolYearService.editSchoolYear(existSchoolYear), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        /*}else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
        /*
        {
            "yearId":1
            "year":"2020-2024"
        }
        */
    }

}
