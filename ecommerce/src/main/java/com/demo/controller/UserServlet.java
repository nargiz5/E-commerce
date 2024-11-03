

package com.demo.controller;

import com.demo.model.User;
import com.demo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/UserServlet")// Map this servlet to the URL pattern /UserServlet
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");// Get the action parameter from the request
        // Handle registration or login based on the action parameter
        if ("register".equals(action)) {
            registerUser(request, response);// Call method to handle user registration
        } else if ("login".equals(action)) {
            loginUser(request, response);}// Call method to handle user login

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            logoutUser(request, response); // Call method to handle user logout
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Sanitize and validate user input
        String username = sanitizeInput(request.getParameter("username"));
        String password = sanitizeInput(request.getParameter("password"));
        if (!isValidInput(username) || !isValidInput(password)) {
            response.sendRedirect("login.jsp?error=Invalid input.");
            return;
        }
        // Log registration attempt
        System.out.println("Attempting to register user: " + username);

        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // Use hashed password
            user.setWallet(500);// Set initial wallet balance
            // Check for existing user
            User existingUser = (User) session.createQuery("FROM User WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();

            if (existingUser != null) {
                response.sendRedirect("index.jsp?error=Username already exists.");
                return;
            }

            session.save(user); // Save the new user to the database
            transaction.commit();// Commit the transaction
            System.out.println("User registered successfully: " + username);
            response.sendRedirect("login.jsp?msg=Registration successful! Please log in.");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Registration failed for user: " + username + ". Error: " + e.getMessage());
            response.sendRedirect("index.jsp?error=Registration failed.");
        } finally {
            session.close();
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Sanitize and validate user input
        String username = sanitizeInput(request.getParameter("username"));
        String password = sanitizeInput(request.getParameter("password"));
        if (!isValidInput(username) || !isValidInput(password)) {
            response.sendRedirect("login.jsp?error=Invalid input.");
            return;
        }
        Session session = HibernateUtil.openSession();
        try {
            // Fetch user from the database based on username
            User user = (User) session.createQuery("FROM User WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();

            // Check password against the hashed password
            if (user != null && user.checkPassword(password)) { // Use hashedPassword comparison
                HttpSession httpSession = request.getSession();
                String sessionToken = UUID.randomUUID().toString(); // Generate session token
                httpSession.setAttribute("user", user);// Store the user object in session
                httpSession.setAttribute("sessionToken", sessionToken);// Store the session token


                // Send the session token as a cookie
                Cookie cookie = new Cookie("sessionToken", sessionToken);
                cookie.setHttpOnly(true);// Prevent access to the cookie via JavaScript
                cookie.setPath("/");// Make cookie accessible across the entire application
                response.addCookie(cookie);// Add cookie to the response

                response.sendRedirect(request.getContextPath() + "/products"); // Redirect to products page
            } else {
                response.sendRedirect("login.jsp?error=Invalid username or password.");
            }
        } catch (Exception e) {
            response.sendRedirect("login.jsp?error=An error occurred during login.");
        } finally {
            session.close();
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate(); // Invalidate the session
        }
        // Remove the session token cookie
        Cookie cookie = new Cookie("sessionToken", null);
        cookie.setMaxAge(0); // Set to expire immediately
        cookie.setPath("/");// Make sure it can be removed from the entire application
        response.addCookie(cookie);

        response.sendRedirect("index.jsp");
    }

    private boolean isValidInput(String input) {
        // Basic validation: check length and allowed characters
        return input != null && input.length() > 0 && input.matches("^[a-zA-Z0-9_]*$"); // Allows alphanumeric and underscore
    }
    private String sanitizeInput(String input) {
        // Simple sanitization: trim whitespace and remove HTML tags
        if (input != null) {
            return input.trim().replaceAll("<[^>]*>", ""); // Remove HTML tags
        }
        return null;
    }
}
