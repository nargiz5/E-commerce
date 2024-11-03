package com.demo.dao;

import com.demo.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDao {
    private final Session session;
    // Constructor to initialize UserDao with a Hibernate session
    public UserDao(Session session) {
        this.session = session;
    }

    // Method to update an existing user in the database
    public void updateUser(User user) {
        Transaction transaction = session.beginTransaction();
        try {
            session.update(user);
            transaction.commit();// Commit the transaction to persist the changes
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Handle exceptions appropriately
        }
    }
    // Method to retrieve a user by their ID
    public User getUserById(Long userId) {
        return session.get(User.class, userId);
    }
}
