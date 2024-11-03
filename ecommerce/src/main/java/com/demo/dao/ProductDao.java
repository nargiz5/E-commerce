package com.demo.dao;

import com.demo.model.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductDao {
    private final Session session;

    // Constructor to initialize ProductDao with a Hibernate session
    public ProductDao(Session session) {
        this.session = session;
    }

    // Method to retrieve all products from the database
    public List<Product> getAllProducts() {
        Transaction transaction = session.beginTransaction();// Start a new transaction
        List<Product> productList = null;// Initialize the product list

        try {
            // Create an HQL query to fetch all products
            productList = session.createQuery("FROM Product", Product.class).getResultList();
            transaction.commit();// Commit the transaction to persist any changes
            System.out.println("Product List Size: " + productList.size()); // Log the size
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Print stack trace for debugging
        }
        return productList;
    }

    // Method to retrieve a specific product by its ID
    public Product getProductById(Long productId) {
        Transaction transaction = session.beginTransaction();// Start a new transaction
        Product product = null;// Initialize the product
        try {
            // Fetch the product from the database using its ID
            product = session.get(Product.class, productId); // Fetch the product by ID
            transaction.commit();// Commit the transaction to persist any changes
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Print stack trace for debugging
        }
        return product; // Return the found product or null if not found
    }
}
