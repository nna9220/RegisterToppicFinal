package com.web.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/home")
public class HomeAdminController {
    @GetMapping
    public ResponseEntity<?> getHome(){
        return ResponseEntity.ok("Home admin");
    }
}
