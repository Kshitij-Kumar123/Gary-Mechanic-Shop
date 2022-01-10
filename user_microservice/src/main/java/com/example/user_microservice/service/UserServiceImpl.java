package com.example.user_microservice.service;

import com.example.user_microservice.domain.AppUser;
import com.example.user_microservice.domain.Role;
import com.example.user_microservice.repository.AppUserRepository;
import com.example.user_microservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor // sets the class repo constructor without the code
@Transactional
@Slf4j
// I give functions in the appuser service INTERFACE SOME MEANING OR FUNCTION
public class UserServiceImpl implements AppUserService, UserDetailsService {
    @Autowired
    private final AppUserRepository appUserRepository;
    @Autowired
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        AppUser user = appUserRepository.findByUsername(username);

        System.out.println("user = " + user);
        if (user == null) {
            log.error("username and user maybe idk not in database");
            throw new UsernameNotFoundException("user not found with name" + username);
        } else {
            log.info("found in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                user.getRoles().forEach(role -> {
             authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        log.info("saving user password");

        return new org.springframework.security.core.userdetails.User(user.getUsername(), passwordEncoder.encode(user.getPassword()), authorities);
    }

    @Override
    public AppUser getSpecificUser(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser getUserById(Long userId) throws Exception {
        System.out.println("userId = " + userId);
        return appUserRepository.findById(userId).orElseThrow(() -> new Exception("user not found with userId: " + userId));
    }

    @Override
    public AppUser saveSpecificUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public Role saveSpecificRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser addRoleToUser(String username, String rolename) {
        System.out.println("username = " + username);
        
        AppUser user = appUserRepository.findByUsername(username);
        Role role = roleRepository.findByName(rolename);
        
        System.out.println("rolename = " + rolename);
        System.out.println("role = " + role);
        user.getRoles().add(role);

        return user;
    }
}
