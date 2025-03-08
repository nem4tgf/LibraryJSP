package org.example.demo1.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.demo1.dao.BookDAO;
import org.example.demo1.dao.BorrowTicketDAO;
import org.example.demo1.dao.UserDAO;
import org.example.demo1.model.Book;
import org.example.demo1.model.BorrowTicket;
import org.example.demo1.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Logger;

@WebListener
public class DataInitializer implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());
    private UserDAO userDAO;
    private BookDAO bookDAO;
    private BorrowTicketDAO borrowTicketDAO;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Initializing sample data...");
        userDAO = new UserDAO();
        bookDAO = new BookDAO();
        borrowTicketDAO = new BorrowTicketDAO();

        try {
            initializeUsers();
            initializeBooks();
            initializeBorrowTickets();
            LOGGER.info("Sample data initialized successfully!");
        } catch (SQLException e) {
            LOGGER.severe("Error initializing sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application shutdown.");
    }

    private void initializeUsers() throws SQLException {
        User[] sampleUsers = {
                new User(0, "admin1", "admin123", "Nguyen Van An", "an@gmail.com", "librarian", null),
                new User(0, "student1", "student123", "Tran Thi Bich", "bich@gmail.com", "student", null),
                new User(0, "student2", "student456", "Le Van Cao", "cao@gmail.com", "student", null)
        };

        for (User user : sampleUsers) {
            User existingUser = userDAO.getUserByUsername(user.getUsername());
            if (existingUser == null) {
                userDAO.addUser(user);
                LOGGER.info("Added sample user: " + user.getUsername());
            } else {
                LOGGER.info("User already exists: " + user.getUsername());
            }
        }
    }

    private void initializeBooks() throws SQLException {
        Book[] sampleBooks = {
                new Book(0, "B001", "To Kill a Mockingbird", "Harper Lee", 10, 8, null),
                new Book(0, "B002", "1984", "George Orwell", 15, 12, null),
                new Book(0, "B003", "The Great Gatsby", "F. Scott Fitzgerald", 5, 3, null)
        };

        for (Book book : sampleBooks) {
            Book existingBook = bookDAO.getBookByCode(book.getBookCode());
            if (existingBook == null) {
                bookDAO.addBook(book);
                LOGGER.info("Added sample book: " + book.getBookCode());
            } else {
                LOGGER.info("Book already exists: " + book.getBookCode());
            }
        }
    }

    private void initializeBorrowTickets() throws SQLException {
        BorrowTicket[] sampleTickets = {
                new BorrowTicket(0, 2, 1, LocalDate.parse("2025-03-01"), LocalDate.parse("2025-03-15"), null, "borrowed", null),
                new BorrowTicket(0, 3, 2, LocalDate.parse("2025-02-20"), LocalDate.parse("2025-03-06"), LocalDate.parse("2025-03-05"), "returned", null)
        };

        for (BorrowTicket ticket : sampleTickets) {
            BorrowTicket existingTicket = borrowTicketDAO.getBorrowTicketById(ticket.getId());
            if (existingTicket == null) {
                borrowTicketDAO.addBorrowTicket(ticket);
                LOGGER.info("Added sample borrow ticket for student_id: " + ticket.getStudentId());
            } else {
                LOGGER.info("Borrow ticket already exists for student_id: " + ticket.getStudentId());
            }
        }
    }
}