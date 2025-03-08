package org.example.demo1.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo1.utils.JWTUtils;

import java.io.IOException;

@WebFilter(
        filterName = "JwtFilter",
        urlPatterns = {
                "/librarian.jsp",
                "/student.jsp",
                "/books",
                "/borrow",
                "/users"
        }
)
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String token = getTokenFromCookie(httpReq);
        if (token == null) {
            redirectToLogin(httpRes, httpReq.getContextPath());
            return;
        }

        try {
            DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
            String username = decodedJWT.getSubject();
            String role = decodedJWT.getClaim("role").asString();

            if (username == null || role == null) {
                redirectToLogin(httpRes, httpReq.getContextPath(), "Invalid token data");
                return;
            }

            httpReq.setAttribute("username", username);
            httpReq.setAttribute("role", role);

            if (!hasPermission(httpReq, role)) {
                httpRes.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Insufficient permissions");
                return;
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            redirectToLogin(httpRes, httpReq.getContextPath(), "token_invalid");
        }
    }

    private String getTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("jwt_token".equals(cookie.getName()) && !cookie.getValue().isEmpty()) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean hasPermission(HttpServletRequest req, String role) {
        String uri = req.getRequestURI();
        String action = req.getParameter("action");

        if (uri.endsWith("/librarian.jsp") || uri.contains("/books") || uri.contains("/users") ||
                (uri.contains("/borrow") && "return".equals(action))) {
            return "librarian".equals(role);
        }

        if (uri.endsWith("/student.jsp") || (uri.contains("/borrow") && "borrow".equals(action))) {
            return "student".equals(role);
        }

        return true; // Các trường hợp khác mặc định cho phép nếu token hợp lệ
    }

    private void redirectToLogin(HttpServletResponse res, String contextPath, String error) throws IOException {
        res.sendRedirect(contextPath + "/auth?error=" + error);
    }

    private void redirectToLogin(HttpServletResponse res, String contextPath) throws IOException {
        res.sendRedirect(contextPath + "/auth");
    }
}