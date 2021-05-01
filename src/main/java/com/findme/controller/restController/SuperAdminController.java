package com.findme.controller.restController;

import com.findme.model.UserRole;
import com.findme.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superAdmin")
public class SuperAdminController {

    private final RoleService roleService;

    @Autowired
    public SuperAdminController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PutMapping("/makeAdmin")
    public void makeAdmin(@RequestParam String userId) throws Exception {

        roleService.addRoleByUserId(Long.parseLong(userId), UserRole.ADMIN);
    }

    @PutMapping("/removeAdmin")
    public void removeAdmin(@RequestParam String userId) throws Exception {

        roleService.removeRoleByUserId(Long.parseLong(userId), UserRole.ADMIN);
    }
}
