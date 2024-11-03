
package com.demo.controller;

import com.demo.dao.ProductDao;
import com.demo.model.Product;
import com.demo.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/ProductServlet")// Map this servlet to the URL pattern /ProductServlet
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current HTTP session; 'false' means do not create a new session if it doesn't exist
        HttpSession httpSession = request.getSession(false);
        String sessionToken = null;

        // Get the session token from the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Check for the sessionToken cookie
                if ("sessionToken".equals(cookie.getName())) {
                    sessionToken = cookie.getValue();// Store the value of the sessionToken
                    break;
                }
            }
        }
        // Check if the session and sessionToken are valid
        if (httpSession != null && sessionToken != null) {
            String storedToken = (String) httpSession.getAttribute("sessionToken");

            // Validate the session token against the stored token
            if (!sessionToken.equals(storedToken)) {
                response.sendRedirect("login.jsp?error=Session invalid.");
                return;
            }

            // Proceed with fetching products
            Session session = HibernateUtil.openSession();// Open a Hibernate session
            try {
                ProductDao productDao = new ProductDao(session);// Create a ProductDao instance
                List<Product> productList = productDao.getAllProducts(); // Fetch all products
                System.out.println("Fetched Products: " + productList); // Log for debugging
                request.setAttribute("productList", productList); // Set the product list in request scope
                // Forward the request to productList.jsp to display products
                request.getRequestDispatcher("productList.jsp").forward(request, response); // Forward to JSP
            } catch (Exception e) {
                // Handle any exceptions and redirect to an error page
                response.sendRedirect("error.jsp?error=Could not retrieve products."); // Error handling
            } finally {
                session.close();// Ensure the session is closed to release resources
            }
        } else {
            // Redirect to login page if session or sessionToken is invalid or missing
            response.sendRedirect("login.jsp?error=Please log in.");
        }
    }
}
