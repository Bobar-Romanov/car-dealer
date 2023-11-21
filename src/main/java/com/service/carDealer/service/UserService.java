package com.service.carDealer.service;

import com.service.carDealer.model.UserDTO;
import com.service.carDealer.repository.UserRepository;
import com.service.carDealer.util.RegForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByLogin(username);
    }

    public boolean existByLogin(String login){
        return userRepository.existByLogin(login);
    }

    public boolean existByPhone(String phone){
        return  userRepository.existByPhone(phone);
    }

    public boolean regUser(RegForm form){
        return userRepository.saveUser(form.getLogin(), passwordEncoder.encode(form.getPassword()), form.getFullName(), form.getPhone());
    }

    public ArrayList<UserDTO> getAll(Long id){
        return userRepository.getAllUsers().stream().
                filter(c -> !c.getId().equals(id)).
                collect(Collectors.toCollection(ArrayList::new));
    }
}
