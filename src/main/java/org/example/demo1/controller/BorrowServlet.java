package org.example.demo1.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo1.dao.BookDAO;
import org.example.demo1.dao.BorrowTicketDAO;
import org.example.demo1.dao.UserDAO;
import org.example.demo1.model.Book;
import org.example.demo1.model.BorrowTicket;
import org.example.demo1.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet("/borrow")
public class BorrowServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BorrowServlet.class.getName());
    private BorrowTicketDAO borrowTicketDAO;
    private BookDAO bookDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        borrowTicketDAO = new BorrowTicketDAO();
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            LOGGER.info("User is null, redirecting to auth");
            response.sendRedirect("auth");
            return;
        }

        String role = user.getRole();
        String action = request.getParameter("action") != null ? request.getParameter("action") : "";
        LOGGER.info("User role: " + role + ", Action: " + action);

        try {
            if ("list".equals(action)) {
                List<BorrowTicket> tickets = "librarian".equals(role) ?
                        borrowTicketDAO.getAllBorrowTickets() :
                        borrowTicketDAO.getBorrowTicketsByStudentId(user.getId());

                Map<Integer, String> studentNames = new HashMap<>();
                Map<Integer, String> bookTitles = new HashMap<>();
                for (BorrowTicket ticket : tickets) {
                    User student = userDAO.getUserById(ticket.getStudentId());
                    Book book = bookDAO.getBookById(ticket.getBookId());
                    studentNames.put(ticket.getStudentId(), student != null ? student.getFullName() : "Unknown");
                    bookTitles.put(ticket.getBookId(), book != null ? book.getTitle() : "Unknown");
                }

                request.setAttribute("tickets", tickets);
                request.setAttribute("studentNames", studentNames);
                request.setAttribute("bookTitles", bookTitles);
                request.setAttribute("role", role);
                request.getRequestDispatcher("borrow-list.jsp").forward(request, response);
            } else if ("student".equals(role)) {
                List<Book> books = bookDAO.getAllBooks();
                LOGGER.info("Books fetched for student: " + books.size());
                request.setAttribute("books", books);
                request.getRequestDispatcher("student.jsp").forward(request, response); // Đổi thành student.jsp
            } else {
                LOGGER.info("Access denied for role: " + role);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            }
        } catch (SQLException e) {
            LOGGER.severe("Database error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect("auth");
            return;
        }

        String role = user.getRole();
        String action = request.getParameter("action");

        try {
            if ("borrow".equals(action) && "student".equals(role)) {
                int bookId = Integer.parseInt(validateParameter(request.getParameter("bookId"), "Book ID"));
                Book book = bookDAO.getBookById(bookId);
                if (book == null || book.getAvailableQuantity() <= 0) {
                    request.setAttribute("error", "Book not available");
                    doGet(request, response);
                    return;
                }

                BorrowTicket ticket = new BorrowTicket();
                ticket.setStudentId(user.getId());
                ticket.setBookId(bookId);
                ticket.setBorrowDate(LocalDate.now());
                ticket.setDueDate(LocalDate.now().plusWeeks(2));
                ticket.setStatus("borrowed");

                borrowTicketDAO.addBorrowTicket(ticket);
                book.setAvailableQuantity(book.getAvailableQuantity() - 1);
                bookDAO.updateBook(book);
                response.sendRedirect("borrow?action=list");

            } else if ("return".equals(action) && "librarian".equals(role)) {
                int ticketId = Integer.parseInt(validateParameter(request.getParameter("ticketId"), "Ticket ID"));
                BorrowTicket ticket = borrowTicketDAO.getBorrowTicketById(ticketId);
                if (ticket == null || !"borrowed".equals(ticket.getStatus())) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ticket");
                    return;
                }

                ticket.setReturnDate(LocalDate.now());
                ticket.setStatus("returned");
                borrowTicketDAO.updateBorrowTicket(ticket);

                Book book = bookDAO.getBookById(ticket.getBookId());
                book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                bookDAO.updateBook(book);
                response.sendRedirect("borrow?action=list");
            }
        } catch (SQLException e) {
            LOGGER.severe("Database error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
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