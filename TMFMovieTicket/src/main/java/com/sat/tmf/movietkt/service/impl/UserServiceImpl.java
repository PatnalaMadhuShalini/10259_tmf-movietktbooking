package com.sat.tmf.movietkt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sat.tmf.movietkt.dao.UserDao;
import com.sat.tmf.movietkt.dao.UserRepository;
import com.sat.tmf.movietkt.entities.User;
import com.sat.tmf.movietkt.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    // Keep UserDao for compatibility in other places; prefer UserRepository for persistence here
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        // Check uniqueness using repository (DB-backed)
        if (userRepository.findByUsername(user.getUsername()) != null)
            throw new RuntimeException("Username already exists!");
        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException("Email already exists!");

        // Encode password and persist
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.saveAndFlush(user);
        return saved;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUserProfile(String username, User updatedUser) {
        User existing = userRepository.findByUsername(username);
        existing.setFullName(updatedUser.getFullName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPhone(updatedUser.getPhone());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(existing);
        return existing;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}