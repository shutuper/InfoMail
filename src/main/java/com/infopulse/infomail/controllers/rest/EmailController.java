package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.email.EmailDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/emails")
public class EmailController {

    @PostMapping
    public ResponseEntity<EmailDTO> addEmail(@RequestBody EmailDTO email) {
        System.out.println("email = " + email);
        return new ResponseEntity<>(email, HttpStatus.CREATED);
    }

}
