package org.example.demo1.dao;

import org.example.demo1.model.Book;
import org.example.demo1.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private static final String SELECT_BY_CODE = "SELECT id, book_code, title, author, total_quantity, available_quantity, created_at FROM books WHERE book_code = ?";
    private static final String SELECT_BY_ID = "SELECT id, book_code, title, author, total_quantity, available_quantity, created_at FROM books WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, book_code, title, author, total_quantity, available_quantity, created_at FROM books";
    private static final String SEARCH_BY_KEYWORD = "SELECT id, book_code, title, author, total_quantity, available_quantity, created_at FROM books WHERE title LIKE ? OR author LIKE ?";
    private static final String INSERT = "INSERT INTO books (book_code, title, author, total_quantity, available_quantity) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE books SET title = ?, author = ?, total_quantity = ?, available_quantity = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM books WHERE id = ?";

    public Book getBookByCode(String bookCode) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CODE)) {
            stmt.setString(1, bookCode);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToBook(rs) : null;
            }
        }
    }

    public Book getBookById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToBook(rs) : null;
            }
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            return books;
        }
    }

    public List<Book> searchBooksByKeyword(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_KEYWORD)) {
            String searchTerm = "%" + keyword + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
            return books;
        }
    }

    public void addBook(Book book) throws SQLException {
        validateBook(book);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            stmt.setString(1, book.getBookCode());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setInt(4, book.getTotalQuantity());
            stmt.setInt(5, book.getAvailableQuantity());
            stmt.executeUpdate();
        }
    }

    public void updateBook(Book book) throws SQLException {
        validateBook(book);
        if (book.getId() <= 0) throw new IllegalArgumentException("Invalid book ID");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getTotalQuantity());
            stmt.setInt(4, book.getAvailableQuantity());
            stmt.setInt(5, book.getId());
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No book found with id: " + book.getId());
        }
    }

    public void deleteBook(int bookId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, bookId);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No book found with id: " + bookId);
        }
    }

    private void validateBook(Book book) {
        if (book == null || book.getBookCode() == null || book.getTitle() == null ||
                book.getTotalQuantity() < 0 || book.getAvailableQuantity() < 0 ||
                book.getAvailableQuantity() > book.getTotalQuantity()) {
            throw new IllegalArgumentException("Invalid book data");
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("book_code"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("total_quantity"),
                rs.getInt("available_quantity"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
        );
    }
}