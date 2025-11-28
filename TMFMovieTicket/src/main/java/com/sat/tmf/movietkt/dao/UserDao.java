package com.sat.tmf.movietkt.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
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

    // Removed PasswordEncoder injection and authenticate() to avoid circular
    // bean dependency with SecurityConfig -> CustomUserDetailsService -> UserService
    // Authentication is handled by CustomUserDetailsService using UserService.findByUsername

}