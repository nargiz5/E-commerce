package com.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id // Specify the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use auto-increment for ID
    private Long id;

    @Column(name = "user_id", nullable = false) // Map this field to the "user_id" column
    private Long userId;  // ID of the user who placed the order

    @Column(name = "product_id", nullable = false) // Map this field to the "product_id" column
    private Long productId; // ID of the product being ordered

    @Column(name = "order_date") // Map this field to the "order_date" column
    private java.sql.Timestamp orderDate; // Timestamp of when the order was placed
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public java.sql.Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(java.sql.Timestamp orderDate) {
        this.orderDate = orderDate;
    }
}
