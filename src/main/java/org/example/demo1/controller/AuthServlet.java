package org.example.demo1.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo1.dao.UserDAO;
import org.example.demo1.model.User;
import org.example.demo1.utils.JWTUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AuthServlet.class.getName());
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("true".equals(request.getParameter("logout"))) {
            request.getSession().invalidate();
            Cookie jwtCookie = new Cookie("jwt_token", "");
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(0);
            response.addCookie(jwtCookie);
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Username and password are required");
            }

            User user = userDAO.getUserByUsername(username.trim());
            if (user == null || !BCrypt.checkpw(password.trim(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid credentials");
            }

            String token = JWTUtils.generateToken(user.getUsername(), user.getRole());
            Cookie jwtCookie = new Cookie("jwt_token", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(10 * 60);
            response.addCookie(jwtCookie);

            request.getSession().setAttribute("user", user);
            // Sửa điều hướng: Chuyển sinh viên đến /borrow thay vì student.jsp
            response.sendRedirect("librarian".equals(user.getRole()) ? "librarian.jsp" : "borrow");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.severe("Authentication error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }
}