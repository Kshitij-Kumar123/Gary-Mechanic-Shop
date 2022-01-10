package com.example.user_microservice.service;

import com.example.user_microservice.domain.AppUser;
import com.example.user_microservice.domain.Role;
import java.util.List;

//tutorial implementation of this is different and interesting take a look into it
// this is an interface! here i defined all the available functions, implementation of those funcs in the
// serviceimpl file
public interface AppUserService {
    AppUser saveSpecificUser(AppUser user);
    AppUser getSpecificUser(String username);
    Role saveSpecificRole(Role role);
    List<AppUser> getAllUsers();
    AppUser addRoleToUser(String username, String rolename);
    AppUser getUserById(Long userId) throws Exception;
}
