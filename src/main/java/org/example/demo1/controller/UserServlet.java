package org.example.demo1.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo1.dao.UserDAO;
import org.example.demo1.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "list";

        if ("list".equals(action)) {
            try {
                List<User> users = userDAO.getAllUsers();
                request.setAttribute("users", users);
                request.getRequestDispatcher("user-list.jsp").forward(request, response);
            } catch (SQLException e) {
                LOGGER.severe("Error fetching users: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            try {
                User user = new User();
                user.setUsername(validateParameter(request.getParameter("username"), "Username"));
                user.setPassword(validateParameter(request.getParameter("password"), "Password"));
                user.setFullName(validateParameter(request.getParameter("fullName"), "Full Name"));
                user.setEmail(request.getParameter("email"));
                String role = validateParameter(request.getParameter("role"), "Role");
                if (!"librarian".equals(role) && !"student".equals(role)) {
                    throw new IllegalArgumentException("Invalid role");
                }
                user.setRole(role);

                userDAO.addUser(user);
                response.sendRedirect("users");
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (SQLException e) {
                LOGGER.severe("Error adding user: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        }
    }

    private String validateParameter(String param, String fieldName) {
        if (param == null || param.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return param.trim();
    }
}