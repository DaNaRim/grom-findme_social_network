package com.findme.controller.restController;

import com.findme.model.RoleName;
import com.findme.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/superAdmin")
public class SuperAdminController {

    private final RoleService roleService;

    @Autowired
    public SuperAdminController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/makeAdmin")
    @ResponseStatus(HttpStatus.CREATED)
    public void makeAdmin(@RequestParam String userId) throws Exception {

        roleService.addRoleByUserId(Long.parseLong(userId), RoleName.ADMIN);
    }

    @DeleteMapping("/removeAdmin")
    public void removeAdmin(@RequestParam String userId) throws Exception {

        roleService.removeRoleByUserId(Long.parseLong(userId), RoleName.ADMIN);
    }
}
