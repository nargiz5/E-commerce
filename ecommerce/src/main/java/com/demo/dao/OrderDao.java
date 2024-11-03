package com.demo.dao;

import com.demo.model.Order;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class OrderDao {
    private final Session session;// Store the Hibernate session
    // Constructor to initialize the OrderDao with a Hibernate session
    public OrderDao(Session session) {
        this.session = session;
    }
    // Method to retrieve orders associated with a specific user ID
    public List<Order> getOrdersByUserId(Long userId) {
        // Create an HQL query to select orders for the given user ID
        Query<Order> query = session.createQuery("FROM Order WHERE user_id = :userId", Order.class);
        query.setParameter("userId", userId);// Set the user ID parameter
        return query.getResultList();// Execute the query and return the list of orders
    }

    // Method to save a new order to the database
    public void saveOrder(Order order) {
        Transaction transaction = session.beginTransaction();// Start a new transaction
        try {
            session.save(order);// Save the order object to the database
            transaction.commit();// Commit the transaction to persist changes
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();// Rollback the transaction in case of an error
            }
            throw e; // Handle the exception as needed
        }
    }
}
