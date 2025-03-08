package org.example.demo1.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo1.dao.BookDAO;
import org.example.demo1.model.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BookServlet.class.getName());
    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "list";

        try {
            if ("list".equals(action)) {
                List<Book> books = bookDAO.getAllBooks();
                request.setAttribute("books", books);
                request.getRequestDispatcher("book-list.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            LOGGER.severe("Database error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                Book book = new Book();
                book.setBookCode(validateParameter(request.getParameter("bookCode"), "Book Code"));
                book.setTitle(validateParameter(request.getParameter("title"), "Title"));
                book.setAuthor(request.getParameter("author"));
                int totalQty = Integer.parseInt(validateParameter(request.getParameter("totalQuantity"), "Total Quantity"));
                if (totalQty < 0) throw new IllegalArgumentException("Total quantity cannot be negative");
                book.setTotalQuantity(totalQty);
                book.setAvailableQuantity(totalQty);

                bookDAO.addBook(book);
                response.sendRedirect("books");
            } else if ("delete".equals(action)) {
                int bookId = Integer.parseInt(validateParameter(request.getParameter("bookId"), "Book ID"));
                bookDAO.deleteBook(bookId);
                response.sendRedirect("books");
            }
        } catch (SQLException e) {
            LOGGER.severe("Database error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private String validateParameter(String param, String fieldName) {
        if (param == null || param.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return param.trim();
    }
}