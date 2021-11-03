package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.models.ServerInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/information")
public class InformationController {

    @GetMapping
    public ResponseEntity getInformation() {
        return ResponseEntity.ok(new ServerInformation("Welcome to the InfoMail project!"));
    }

}
