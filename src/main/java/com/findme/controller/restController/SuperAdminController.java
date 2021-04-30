package com.findme.controller.restController;

import com.findme.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superAdmin")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @Autowired
    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    @PutMapping("/makeAdmin")
    public void makeAdmin(@RequestParam String userId) throws Exception {
        superAdminService.makeAdmin(Long.parseLong(userId));
    }

    @PutMapping("/removeAdmin")
    public void removeAdmin(@RequestParam String userId) throws Exception {
        superAdminService.removeAdmin(Long.parseLong(userId));
    }
}
