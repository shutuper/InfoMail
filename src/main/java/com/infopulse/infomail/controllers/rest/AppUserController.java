package com.infopulse.infomail.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class AppUserController {

    @GetMapping(path = "email")
    public Map<String, String> getUserEmail(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return new HashMap<>(){{put("email", email);}};
    }
}
