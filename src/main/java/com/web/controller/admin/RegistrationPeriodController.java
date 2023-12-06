package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.entity.RegistrationPeriod;
import com.web.mapper.RegistrationPeriodMapper;
import com.web.dto.request.RegistrationPeriodRequest;
import com.web.repository.PersonRepository;
import com.web.repository.RegistrationPeriodRepository;
import com.web.service.Admin.RegistrationPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/Period")
public class RegistrationPeriodController {
    @Autowired
    private RegistrationPeriodService registrationPeriodService;
    @Autowired
    private RegistrationPeriodMapper registrationPeriodMapper;
    @Autowired
    private RegistrationPeriodRepository registrationPeriodRepository;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/list")
    public ResponseEntity<?> findAllExisted(){
       /* if (CheckedPermission.isAdmin(personRepository)) {*/
            List<RegistrationPeriod> registrationPeriods = registrationPeriodService.findAll();
            return ResponseEntity.ok(registrationPeriodMapper.toRegistrationPeriodListDTO(registrationPeriods));
        /*}
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
    }

    @PostMapping("/create")
    public ResponseEntity<?> savePeriod(@RequestBody RegistrationPeriodRequest registrationPeriodRequest){
        /*if (CheckedPermission.isAdmin(personRepository)) {*/
            return new ResponseEntity<>(registrationPeriodService.createPeriod(registrationPeriodRequest), HttpStatus.CREATED);
        /*{
            "periodId": 0,
                "registrationTimeStart": "2023-11-09",
                "registrationTimeEnd": "2023-11-15",
                "registrationName": "Đợt 1"
        }*/
        /*}
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
    }

    @PutMapping("/edit/{periodId}")
    public ResponseEntity<?> updatePeriod(@PathVariable int periodId, @RequestBody RegistrationPeriod registrationPeriodRequest){
        RegistrationPeriod existRegistrationPeriod = registrationPeriodRepository.findById(periodId).orElse(null);
        /*if (CheckedPermission.isAdmin(personRepository)) {*/
            if (existRegistrationPeriod != null) {
                RegistrationPeriod updatePeriod = registrationPeriodService.editPeriod(existRegistrationPeriod);
                return new ResponseEntity<>(updatePeriod, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        /*}else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
    }
}
