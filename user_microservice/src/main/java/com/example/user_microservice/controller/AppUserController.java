package com.example.user_microservice.controller;

import com.example.user_microservice.domain.AppUser;
import com.example.user_microservice.domain.Role;
import com.example.user_microservice.service.AppUserService;
import com.example.user_microservice.service.RoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> findAllUsers() {
        return ResponseEntity.ok().body(appUserService.getAllUsers());
    }

    @GetMapping("/find")
    public ResponseEntity<AppUser> findByUsername(@RequestParam(name = "userId") Long userId) throws Exception {
        return ResponseEntity.ok().body(appUserService.getUserById(userId));
    }

    @PostMapping("/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser userToSave) {
//        why do i need this uri?
//        response entity allows status code assignment easily
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveSpecificUser(userToSave));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role roleToSave) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

        return ResponseEntity.created(uri).body(appUserService.saveSpecificRole(roleToSave));
    }


    @PostMapping("/role/addtouser")
    public ResponseEntity<AppUser> addRoleToUser(@RequestBody RoleToUserForm form) {
        System.out.println("form = " + form);
        return ResponseEntity.ok().body(appUserService.addRoleToUser(form.getUsername(), form.getRolename()));
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String rolename;
}