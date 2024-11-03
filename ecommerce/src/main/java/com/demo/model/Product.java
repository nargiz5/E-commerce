package com.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "products") // Ensure this matches your database table
public class Product {
    @Id // Specify the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use auto-increment for ID
    private Long id;

    @Column(nullable = false) // Map this field to a non-nullable column
    private String name; // Name of the product

    @Column(nullable = false) // Map this field to a non-nullable column
    private double price; // Price of the product

    @Column(nullable = false) // Map this field to a non-nullable column
    private String description; // Description of the product


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
