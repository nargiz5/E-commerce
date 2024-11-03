package com.demo.model;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Entity
@Table(name = "users") // Ensure this matches your database table
public class User {
    @Id // Specify the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use auto-increment for ID
    private Long id;

    @Column(nullable = false, unique = true) // Ensure usernames are unique
    private String username;

    @Column(nullable = false) // Store hashed password
    private String password;

    @Column(nullable = false) // Ensure wallet balance is set
    private double wallet;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Hash the password before storing it
        this.password = hashPassword(password);
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    // Helper method to hash the password
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Method to check if the password matches the hashed password
    public boolean checkPassword(String plaintextPassword) {
        return BCrypt.checkpw(plaintextPassword, this.password);
    }
}
