package com.sat.tmf.movietkt.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.sat.tmf.movietkt.entities.User;

/**
 * UserDao — DAO layer for User entity using Hibernate SessionFactory.
 * Extends GenericDao for CRUD operations and adds custom queries.
 */
@Repository
public class UserDao extends GenericDao<User, Integer> {

    public UserDao() {
        super(User.class);
    }

    /**
     * Find a user by username.
     *
     * @param username the username to search
     * @return User or null if not found
     */
    public User findByUsername(String username) {
        Session session = getSession();
        Query<User> query = session.createQuery("from User where username = :uname", User.class);
        query.setParameter("uname", username);
        return query.uniqueResult();
    }

    /**
     * Find a user by email.
     *
     * @param email the email to search
     * @return User or null if not found
     */
    public User findByEmail(String email) {
        Session session = getSession();
        Query<User> query = session.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    /**
     * Return all users.
     */
    public List<User> findAllUsers() {
        Session session = getSession();
        return session.createQuery("from User", User.class).list();
    }

    /**
     * Authenticate user by username and password (plain — encode in service layer).
     *
     * @param username user’s username
     * @param password plain password (compare encoded in service)
     * @return matching User or null
     */
//    public User authenticate(String username, String password) {
//        Session session = getSession();
//        Query<User> query = session.createQuery("from User where username = :uname and password = :pwd", User.class);
//        query.setParameter("uname", username);
//        query.setParameter("pwd", password);
//        return query.uniqueResult();
//    }
    @Autowired
    private PasswordEncoder passwordEncoder; // inject the bean

    public User authenticate(String username, String rawPassword) {
        User user = findByUsername(username);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

}
